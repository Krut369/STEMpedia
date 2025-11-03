package com.example.fileshareapp

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fileshareapp.ui.theme.FileShareAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FileShareAppTheme {
                FileShareApp()
            }
        }
    }
}

@Composable
fun FileShareApp(viewModel: FileShareViewModel = viewModel()) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val message by viewModel.message.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            snackbarHostState.showSnackbar(message)
            viewModel.clearMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            // Show login or main screen based on login status
            if (isLoggedIn) {
                MainScreen(viewModel)
            } else {
                LoginScreen(viewModel)
            }

            // Show loading indicator
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoginScreen(viewModel: FileShareViewModel) {
    val phoneNumber by viewModel.phoneNumber.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "FileShare App",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Login with Phone Number",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { viewModel.updatePhoneNumber(it) },
            label = { Text("Phone Number") },
            placeholder = { Text("+1 234 567 8900") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Enter your phone number (with or without country code)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.loginWithPhone() },
            modifier = Modifier.fillMaxWidth(),
            enabled = phoneNumber.isNotEmpty()
        ) {
            Text("Login / Sign Up")
        }

        Spacer(modifier = Modifier.height(24.dp))


        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "First time? An account will be created automatically!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun MainScreen(viewModel: FileShareViewModel) {
    val currentUserName by viewModel.currentUserName.collectAsState()
    val files by viewModel.files.collectAsState()
    val context = LocalContext.current

    var selectedVisibility by remember { mutableStateOf(false) } // false = private, true = public
    
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            viewModel.uploadFile(context, uri, isPublic = selectedVisibility)
        }
    }

    var showVisibilityDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Welcome!",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = currentUserName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            TextButton(onClick = { viewModel.logout() }) {
                Text("Logout")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))


        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Upload a File",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Choose files to share with others",
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { showVisibilityDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Choose File")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))


        Text(
            text = "Your Files & Public Files",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(12.dp))


        if (files.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No files yet. Upload your first file!",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(files) { file ->
                    FileItemCard(file)
                }
            }
        }
    }
    if (showVisibilityDialog) {
        AlertDialog(
            onDismissRequest = { showVisibilityDialog = false },
            title = { Text("Choose Visibility") },
            text = { 
                Column {
                    Text("Select who can see this file:")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Private: Only you can see it", style = MaterialTheme.typography.bodySmall)
                    Text("• Public: Everyone can see it", style = MaterialTheme.typography.bodySmall)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedVisibility = false // Private
                        showVisibilityDialog = false
                        filePickerLauncher.launch(arrayOf("*/*"))
                    }
                ) {
                    Text("Private")
                }
            },
            dismissButton = {
                Row {
                    Button(
                        onClick = {
                            selectedVisibility = true // Public
                            showVisibilityDialog = false
                            filePickerLauncher.launch(arrayOf("*/*"))
                        }
                    ) {
                        Text("Public")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = { showVisibilityDialog = false }) {
                        Text("Cancel")
                    }
                }
            }
        )
    }
}

@Composable
fun FileItemCard(file: FileItem) {
    val uriHandler = LocalUriHandler.current

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = file.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))


            Surface(
                color = if (file.visibility == "PUBLIC") 
                    MaterialTheme.colorScheme.primaryContainer 
                else 
                    MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = file.visibility,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))


            Text(
                text = if (file.isOwner) "Owner: You" else "Owner: ${file.ownerName}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))


            Button(
                onClick = { uriHandler.openUri(file.downloadUrl) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Open/Download")
            }
        }
    }
}
