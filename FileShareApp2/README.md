# 🔐 FileShare App

A lightweight **Android application** built with **Kotlin** and **Firebase**, enabling users to **upload**, **share**, and **view files** seamlessly across devices.

---

## ✨ Features

* 🔑 **Login / Sign-Up** using Email & Password (Firebase Authentication)
* ☁️ **Upload Files** — supports images, PDFs, and videos
* 👁️ **Public / Private Visibility** settings for shared files
* 📂 **List & Download** uploaded files with real-time sync
* 🔄 **Firestore Integration** for live updates and metadata tracking

---

## 🧠 How It Works

1. **Login Screen** → User enters email & password → auto-register if new
2. **Main Screen** → Displays personal uploads and public files
3. **Upload Flow** → Select a file → choose visibility → upload → auto-save to Firestore
4. **File Access** → Tap any item to view or download directly

---

## ⚙️ Firebase Setup

1. **Enable** Firebase Authentication → *Email/Password*
2. **Create Firestore Collection** → `files`
3. **Set Up Firebase Storage** → path: `uploads/{userId}/`

Each Firestore document structure:

```json
{
  "fileName": "sample.pdf",
  "downloadUrl": "https://firebasestorage.googleapis.com/...",
  "visibility": "PUBLIC",
  "ownerUid": "user123"
}
```

---

## 📂 Project Structure

```
app/
├─ MainActivity.kt           # Handles login and file list UI
├─ FileShareViewModel.kt     # ViewModel for authentication, upload, and Firestore logic
└─ ui/theme/                 # Colors, typography, and design system
```

---

## 🧰 Tech Stack

| Component        | Technology                        |
| ---------------- | --------------------------------- |
| **Language**     | Kotlin                            |
| **Architecture** | MVVM (with ViewModel + StateFlow) |
| **UI Toolkit**   | Jetpack Compose                   |
| **Backend**      | Firebase Auth, Firestore, Storage |
| **Async Tools**  | Kotlin Coroutines, StateFlow      |

---

## 🚀 Run the App

```bash
# 1. Add your Firebase config
Place google-services.json in /app

# 2. Sync Gradle
Click “Sync Now” in Android Studio

# 3. Run the app
▶️ Build and launch on device or emulator
```

---

## 🧑‍💻 Future Enhancements

* 🗑️ Delete or rename uploaded files
* 🔍 Search and filter functionality
* 🖼️ File preview (for images & documents)
* 🔗 Shareable download links

---

## ❤️ Built With

**Kotlin + Firebase + Jetpack Compose**
Clean architecture, real-time updates, and a smooth user experience.
© 2025 **FileShare App Team**
