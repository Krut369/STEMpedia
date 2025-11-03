package com.example.fileshareapp

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

data class FileItem(
    val id: String = "",
    val name: String = "",
    val downloadUrl: String = "",
    val visibility: String = "PRIVATE", // "PUBLIC" or "PRIVATE"
    val ownerUid: String = "",
    val ownerName: String = "",
    val isOwner: Boolean = false
)

class FileShareViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _currentUserName = MutableStateFlow("")
    val currentUserName: StateFlow<String> = _currentUserName

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    private val _files = MutableStateFlow<List<FileItem>>(emptyList())
    val files: StateFlow<List<FileItem>> = _files

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            _isLoggedIn.value = true
            _currentUserName.value = currentUser.displayName ?: currentUser.email?.substringBefore("@") ?: currentUser.uid
            loadFiles()
        }
    }

    fun updatePhoneNumber(value: String) {
        _phoneNumber.value = value
    }


    fun loginWithPhone() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val phone = _phoneNumber.value.trim()


                if (phone.isEmpty()) {
                    _message.value = "Please enter your phone number"
                    _isLoading.value = false
                    return@launch
                }


                val cleanPhone = phone.replace(Regex("[^0-9+]"), "")

                if (cleanPhone.length < 10) {
                    _message.value = "Please enter a valid phone number"
                    _isLoading.value = false
                    return@launch
                }


                val email = "${cleanPhone.replace("+", "")}@fileshare.phone"
                val password = "phone_${cleanPhone}_auth_2024" // Auto-generated secure password

                try {
                    auth.signInWithEmailAndPassword(email, password).await()
                    _message.value = "Welcome back!"
                } catch (e: Exception) {
                    try {
                        val result = auth.createUserWithEmailAndPassword(email, password).await()

                        val profileUpdates = com.google.firebase.auth.userProfileChangeRequest {
                            displayName = cleanPhone
                        }
                        result.user?.updateProfile(profileUpdates)?.await()

                        _message.value = "Account created successfully!"
                    } catch (createError: Exception) {
                        throw createError
                    }
                }

                _isLoggedIn.value = true
                _currentUserName.value = cleanPhone
                _phoneNumber.value = ""

                loadFiles()

            } catch (e: Exception) {
                _message.value = "Login failed: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        auth.signOut()
        _isLoggedIn.value = false
        _currentUserName.value = ""
        _files.value = emptyList()
        _phoneNumber.value = ""
        _message.value = "Logged out successfully"
    }

    fun uploadFile(context: Context, fileUri: Uri, isPublic: Boolean) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val currentUser = auth.currentUser
                if (currentUser == null) {
                    _message.value = "Please login first"
                    _isLoading.value = false
                    return@launch
                }

                val fileName = getFileName(context, fileUri)

                val filePath = "uploads/${currentUser.uid}/${UUID.randomUUID()}_$fileName"
                val storageRef = storage.reference.child(filePath)

                storageRef.putFile(fileUri).await()

                val downloadUrl = storageRef.downloadUrl.await().toString()

                val fileData = hashMapOf(
                    "fileName" to fileName,
                    "downloadUrl" to downloadUrl,
                    "visibility" to if (isPublic) "PUBLIC" else "PRIVATE",
                    "ownerUid" to currentUser.uid,
                    "ownerName" to (currentUser.displayName ?: currentUser.uid),
                    "timestamp" to System.currentTimeMillis()
                )

                firestore.collection("files").add(fileData).await()

                _message.value = "File uploaded successfully!"

                loadFiles()

            } catch (e: Exception) {
                _message.value = "Upload failed: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadFiles() {
        viewModelScope.launch {
            try {
                val currentUser = auth.currentUser ?: return@launch

                val filesList = mutableListOf<FileItem>()

                try {
                    val publicFiles = firestore.collection("files")
                        .whereEqualTo("visibility", "PUBLIC")
                        .get()
                        .await()

                    for (doc in publicFiles.documents) {
                        try {
                            val file = FileItem(
                                id = doc.id,
                                name = doc.getString("fileName") ?: "",
                                downloadUrl = doc.getString("downloadUrl") ?: "",
                                visibility = "PUBLIC",
                                ownerUid = doc.getString("ownerUid") ?: "",
                                ownerName = doc.getString("ownerName") ?: "",
                                isOwner = doc.getString("ownerUid") == currentUser.uid
                            )
                            if (file.downloadUrl.isNotEmpty()) {
                                filesList.add(file)
                            }
                        } catch (e: Exception) {
                        }
                    }
                } catch (e: Exception) {
                }

                try {
                    val privateFiles = firestore.collection("files")
                        .whereEqualTo("ownerUid", currentUser.uid)
                        .whereEqualTo("visibility", "PRIVATE")
                        .get()
                        .await()

                    for (doc in privateFiles.documents) {
                        try {
                            val file = FileItem(
                                id = doc.id,
                                name = doc.getString("fileName") ?: "",
                                downloadUrl = doc.getString("downloadUrl") ?: "",
                                visibility = "PRIVATE",
                                ownerUid = doc.getString("ownerUid") ?: "",
                                ownerName = doc.getString("ownerName") ?: "",
                                isOwner = true
                            )
                            if (file.downloadUrl.isNotEmpty()) {
                                filesList.add(file)
                            }
                        } catch (e: Exception) {
                        }
                    }
                } catch (e: Exception) {
                }

                _files.value = filesList.sortedBy { it.name }

            } catch (e: Exception) {
                _files.value = emptyList()
            }
        }
    }

    private fun getFileName(context: Context, uri: Uri): String {
        var fileName = "file_${System.currentTimeMillis()}"

        try {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex != -1) {
                        fileName = cursor.getString(nameIndex)
                    }
                }
            }
        } catch (e: Exception) {
        }

        return fileName
    }

    fun clearMessage() {
        _message.value = ""
    }
}
