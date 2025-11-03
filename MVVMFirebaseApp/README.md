MVVM Firebase App 📱

An Android app built with Kotlin, showcasing MVVM architecture using Firebase Authentication and Firestore.

✨ Features

🔐 Email/Password login & registration

🔁 Auto-login & logout support

🔥 Real-time Firestore item list

📋 RecyclerView with LiveData updates

💬 User-friendly error handling

🧩 Architecture (MVVM)
View       → Activities, Fragments, Adapters  
ViewModel  → Business logic, LiveData  
Repository → Firebase data abstraction  
Model      → Data classes (Item)

📁 Project Structure
data/
├─ model/Item.kt
└─ repository/ItemRepository.kt
viewmodel/
├─ AuthViewModel.kt
└─ ItemListViewModel.kt
ui/
├─ auth/ (LoginActivity, LoginFragment)
├─ list/ (MainActivity, ListFragment)
└─ adapter/ItemAdapter.kt

🧰 Tech Stack

Language: Kotlin

Backend: Firebase Auth + Firestore

UI: ViewBinding + Material Design

Architecture: MVVM + Repository

Reactive: LiveData, ViewModel

⚙️ Firebase Setup

Enable Email/Password Authentication

Create Firestore collection items

Add sample documents:

{ "title": "Learn Kotlin", "description": "Master Kotlin for Android" }

🚀 Run the App

Add google-services.json in /app

Sync Gradle → Run project

🧭 Flow

New User: Register → Auto-login → View items

Returning User: Auto-login → Main screen

Logout: Back to login

🧠 Key Concepts
Concept	Benefit
MVVM	Clean, modular, testable
LiveData	Reactive UI updates
Repository	Data abstraction
ViewBinding	Type-safe views
🎯 Learning Outcomes

✅ Understand MVVM architecture
✅ Integrate Firebase Auth + Firestore
✅ Use LiveData & ViewModel
✅ Build clean, maintainable Android apps