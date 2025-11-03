FileShare App 🔐

A simple Android app to upload, share, and view files using Firebase.

✨ Features

🔑 Login / Sign-Up with username & password (Firebase Auth)

☁️ Upload files (images, PDFs, videos, etc.) to Firebase Storage

👁️ Public / Private visibility for files

📂 List & Download uploaded files

🔄 Real-time sync with Firebase Firestore

🧠 How It Works

Login Screen: Enter username + password → auto-register new users

Main Screen: See your files + public files from others

Upload: Choose file → set visibility → upload → auto-save to Firestore

View / Download: Tap to open any file directly

⚙️ Firebase Setup

Authentication: Email/Password enabled

Firestore: Collection → files

Storage: Folder → uploads/{userId}/

Each document:

{
"fileName": "sample.pdf",
"downloadUrl": "...",
"visibility": "PUBLIC",
"ownerUid": "user123"
}

🧩 Project Structure
app/
├─ MainActivity.kt         # Login + File list UI
├─ FileShareViewModel.kt   # Handles login, upload, and data
└─ ui/theme/               # Colors, typography

🧰 Key Tools

Kotlin + Jetpack Compose

Firebase Auth, Firestore, Storage

Coroutines + StateFlow

🚀 Run the App

Add google-services.json to /app

Sync Gradle

Run the project ▶️

🧑‍💻 Next Steps  

Delete or rename files

Add search & filters

File preview for images

Share download links

Built with ❤️ using Kotlin + Firebase