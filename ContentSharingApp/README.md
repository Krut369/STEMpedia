Content Sharing App

An Android app built with Kotlin and Firebase to showcase dynamic educational content tiles, web pages, and YouTube videos in a clean, responsive layout.

✨ Features

6 dynamic tiles (2×3 grid) fetched from Firebase Firestore

Firebase Storage for media assets

WebView integration for web content

YouTube embedding support

MVVM architecture with Repository pattern

Material Design & responsive landscape UI

🧠 Tech Stack
Layer	Technology
Language	Kotlin
Architecture	MVVM
Backend	Firebase (Firestore + Storage)
UI	RecyclerView + ViewBinding
Min SDK	24 (Android 7.0)
📂 Structure
app/
├─ model/TileItem.kt
├─ repository/TileRepository.kt
├─ viewmodel/MainViewModel.kt
├─ adapter/TileAdapter.kt
├─ MainActivity.kt
└─ WebViewActivity.kt

⚙️ Firebase Setup

Create Firebase project → add Android app

Download google-services.json → place in /app

Enable Firestore + Storage

Create collection home_tiles with fields:
title, type, targetUrl, youtubeId, imageUrl, order, visible

🚀 Run Instructions
# Build and run
./gradlew installDebug

# Clean build files
./gradlew clean

🧩 Core Functionality

Loads tiles dynamically from Firestore

Displays images via Glide from Firebase Storage

Opens WebView or YouTube player based on content type

Handles missing/broken images gracefully

🧰 Dependencies

Firebase Firestore & Storage

AndroidX RecyclerView, Material, ViewBinding

Glide (for image loading)

Kotlin Coroutines

Built with ❤️ using Kotlin + MVVM + Firebase
© 2025 Content Sharing App Team