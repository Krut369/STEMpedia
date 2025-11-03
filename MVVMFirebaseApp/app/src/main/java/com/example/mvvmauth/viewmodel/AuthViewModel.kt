package com.example.mvvmauth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        object Success : AuthState()
        data class Error(val message: String) : AuthState()
    }

    fun login(email: String, password: String) {
        // Validate input
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Email and password cannot be empty")
            return
        }

        _authState.value = AuthState.Loading

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // Authentication successful
                _authState.value = AuthState.Success
            }
            .addOnFailureListener { exception ->
                // Authentication failed - post error message
                _authState.value = AuthState.Error(
                    exception.message ?: "Authentication failed"
                )
            }
    }

    fun register(email: String, password: String) {
        // Validate input
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Email and password cannot be empty")
            return
        }

        if (password.length < 6) {
            _authState.value = AuthState.Error("Password must be at least 6 characters")
            return
        }

        _authState.value = AuthState.Loading

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _authState.value = AuthState.Success
            }
            .addOnFailureListener { exception ->
                _authState.value = AuthState.Error(
                    exception.message ?: "Registration failed"
                )
            }
    }
    fun resetState() {
        _authState.value = AuthState.Idle
    }
}

