# 📱 MVVM Firebase App

An Android app built with **Kotlin**, demonstrating **MVVM (Model–View–ViewModel)** architecture with **Firebase Authentication** and **Cloud Firestore** integration.

---

## ✨ Features

* 🔐 **Email/Password Authentication** (Login & Registration)
* 🔁 **Auto-login & Logout Support**
* 🔥 **Real-time Firestore Item List**
* 📋 **RecyclerView** powered by **LiveData** for instant UI updates
* 💬 **User-friendly error messages & validation**

---

## 🧩 Architecture — MVVM Pattern

| Layer          | Responsibility                                        |
| :------------- | :---------------------------------------------------- |
| **View**       | Activities, Fragments, Adapters – handle UI rendering |
| **ViewModel**  | Contains business logic and LiveData states           |
| **Repository** | Abstracts Firebase operations and provides data       |
| **Model**      | Defines data structures like `Item`                   |

---

## 📁 Project Structure

```
data/
 ├─ model/
 │   └─ Item.kt
 └─ repository/
     └─ ItemRepository.kt

viewmodel/
 ├─ AuthViewModel.kt
 └─ ItemListViewModel.kt

ui/
 ├─ auth/
 │   ├─ LoginActivity.kt
 │   └─ LoginFragment.kt
 ├─ list/
 │   ├─ MainActivity.kt
 │   └─ ListFragment.kt
 └─ adapter/
     └─ ItemAdapter.kt
```

---

## 🧰 Tech Stack

| Component        | Technology                          |
| :--------------- | :---------------------------------- |
| **Language**     | Kotlin                              |
| **Backend**      | Firebase Authentication + Firestore |
| **UI**           | ViewBinding + Material Design       |
| **Architecture** | MVVM + Repository Pattern           |
| **Reactivity**   | LiveData + ViewModel                |

---

## ⚙️ Firebase Setup

1. Enable **Email/Password Authentication** in Firebase Console

2. Create a **Firestore collection** named `items`

3. Add sample documents:

   ```json
   {
     "title": "Learn Kotlin",
     "description": "Master Kotlin for Android"
   }
   ```

4. Add your `google-services.json` file to the `/app` directory

---

## 🚀 How to Run

1. Open the project in **Android Studio**
2. Sync Gradle files
3. Connect your Firebase project
4. Run the app on an emulator or device

---

## 🧭 App Flow

* 👤 **New User** → Register → Auto-login → View items
* 🔄 **Returning User** → Auto-login → Directly opens main screen
* 🚪 **Logout** → Returns to Login screen

---

## 🧠 Core Concepts

| Concept                | Benefit                               |
| :--------------------- | :------------------------------------ |
| **MVVM**               | Clean, modular, testable code         |
| **LiveData**           | Reactive & lifecycle-aware UI updates |
| **Repository Pattern** | Decouples data layer from UI          |
| **ViewBinding**        | Type-safe access to UI elements       |

---

## 🎯 Learning Outcomes

✅ Understand **MVVM architecture**
✅ Implement **Firebase Auth & Firestore**
✅ Use **LiveData** & **ViewModel** effectively
✅ Build **clean, maintainable Android apps**

---

