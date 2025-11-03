# 📱 Content Sharing App

An Android application built with **Kotlin** and **Firebase**, designed to deliver **dynamic educational content tiles**, **web pages**, and **YouTube videos** through a clean, responsive, and engaging layout.

---

## ✨ Features

* 🧩 **6 Dynamic Tiles (2×3 Grid)** loaded from Firestore
* ☁️ **Firebase Storage Integration** for media and thumbnails
* 🌐 **WebView Support** for opening content links
* ▶️ **YouTube Embedding** for video tiles
* 🧠 **MVVM Architecture** with Repository Pattern
* 🎨 **Material Design** with responsive landscape UI

---

## 🧰 Tech Stack

| Layer            | Technology                     |
| :--------------- | :----------------------------- |
| **Language**     | Kotlin                         |
| **Architecture** | MVVM                           |
| **Backend**      | Firebase (Firestore + Storage) |
| **UI**           | RecyclerView + ViewBinding     |
| **Min SDK**      | 24 (Android 7.0)               |

---

## 📂 Project Structure

```
app/
├─ model/
│   └─ TileItem.kt
├─ repository/
│   └─ TileRepository.kt
├─ viewmodel/
│   └─ MainViewModel.kt
├─ adapter/
│   └─ TileAdapter.kt
├─ MainActivity.kt
└─ WebViewActivity.kt
```

---

## ⚙️ Firebase Setup

1. Create a **Firebase Project** → Add your Android app
2. Download `google-services.json` → place it in `/app`
3. Enable **Cloud Firestore** and **Firebase Storage**
4. Create a Firestore collection named **`home_tiles`** with the following fields:

   ```json
   {
     "title": "Intro to AI",
     "type": "youtube",
     "targetUrl": "https://example.com",
     "youtubeId": "mOYN9HlfTgo",
     "imageUrl": "https://firebasestorage.googleapis.com/...",
     "order": 1,
     "visible": true
   }
   ```

---

## 🚀 Run Instructions

```bash
# Build and run the app
./gradlew installDebug

# Clean build files
./gradlew clean
```

---

## 🧩 Core Functionality

* Dynamically loads tiles from **Firestore**
* Displays tile images using **Glide**
* Opens **WebView** or **YouTube Player** based on tile type
* Handles **missing or broken images** gracefully
* Responsive grid layout optimized for tablets and phones

---

## 🧱 Dependencies

* **Firebase Firestore** & **Firebase Storage**
* **AndroidX RecyclerView**, **Material Components**, **ViewBinding**
* **Glide** (for image loading)
* **Kotlin Coroutines** (for background operations)

---
