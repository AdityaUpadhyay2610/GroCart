# 🛒GroCart Shopping App

A **basic shopping app** built with **Kotlin + Jetpack Compose**, showcasing multi-screen navigation, state management, and **Firebase** integration (OTP auth in testing mode + Realtime Database). This is part of my learning journey — I’ll keep improving it over time.

<p align="left">
  <a href="#features"><img alt="features" src="https://img.shields.io/badge/App-Features-4caf50"></a>
  <a href="#tech-stack"><img alt="tech" src="https://img.shields.io/badge/Tech-Kotlin%20%7C%20Compose%20%7C%20Firebase-2196f3"></a>
  <a href="#roadmap--future"><img alt="status" src="https://img.shields.io/badge/Status-Learning%20in%20public-f39c12"></a>
</p>

---

## ✨ Features

* **Multi-Screen Navigation**

  * `OfferScreen`: View ongoing offers.
  * `CategoryScreen`: Browse product categories.
  * `ItemScreen`: See items related to the selected category.
  * `CartScreen`: Review items added to the cart.
  * `LoginScreen`: User login via phone number.
  * `OtpVerificationScreen`: Enter and verify OTP for authentication.
  * `ErrorScreen`: Handles login/authentication errors gracefully.
* **Core Functionality**

  * Tap a **category** → navigates to its **related items**.
  * **Add items to cart** and view a running list.
  * **Item cards** built with Compose components.
* **Firebase Integration**

  * **OTP authentication** via Firebase Auth (currently **testing mode**).
  * **Realtime Database** to store and fetch items.

---

## 🧱 Tech Stack

* **Language:** Kotlin
* **UI:** Jetpack Compose, Material 3
* **Navigation:** `androidx.navigation.compose`
* **State:** Compose `State`, `ViewModel`
* **Backend:** Firebase Authentication (Phone/OTP), Firebase Realtime Database
* **Build:** Gradle (AGP 8+), compileSdk 35

---

## 📁 Project Structure (example)

```
app/
├─ data/
│  ├─ model/            # Item, Category data classes
│  ├─ repo/             # Firebase repositories
│  └─ datasource/       # Realtime DB paths & mapping
├─ ui/
│  ├─ screens/
│  │  ├─ OfferScreen.kt
│  │  ├─ CategoryScreen.kt
│  │  ├─ ItemScreen.kt
│  │  ├─ CartScreen.kt
│  │  ├─ LoginScreen.kt
│  │  ├─ OtpVerificationScreen.kt
│  │  └─ ErrorScreen.kt
│  ├─ components/       # Reusable Compose UI (ItemCard, CategoryChip, etc.)
│  └─ theme/
├─ navigation/
│  └─ NavGraph.kt       # All routes & arguments
└─ viewmodel/
   ├─ AuthViewModel.kt
   └─ ShopViewModel.kt
```

---

## 🔐 Firebase Setup (Quick Start)

1. Create a Firebase project → add Android app → download **`google-services.json`** to `app/`.
2. Enable **Phone** provider in **Authentication**.
3. (Testing mode) Add test phone numbers in **Authentication → Sign-in method → Phone**.
4. Enable **Realtime Database** and start in **test mode** (or configure secure rules).

**Example Realtime Database structure:**

```json
{
  "categories": {
    "fruits": { "name": "Fruits", "image": "fruits.png" },
    "bread":  { "name": "Bread & Biscuits", "image": "bread.png" }
  },
  "items": {
    "fruits": {
      "apple":   { "id": "apple",   "title": "Apple",  "price": 60,  "image": "apple.png" },
      "banana":  { "id": "banana",  "title": "Banana", "price": 30,  "image": "banana.png" }
    },
    "bread": {
      "toast":   { "id": "toast",   "title": "Toast",  "price": 45,  "image": "toast.png" }
    }
  }
}
```

**Minimal Database Rules (dev/testing only):**

```json
{
  "rules": {
    ".read": true,
    ".write": true
  }
}
```

> ⚠️ For production, restrict reads/writes per user and validate schemas.

---

## ▶️ Run Locally

1. **Clone** the repo:

   ```bash
   git clone https://github.com/AdityaUpadhyay2610/GroCart.git
   cd GroCart
   ```
2. Open in **Android Studio Jellyfish/Koala** or newer.
3. Add `app/google-services.json` from Firebase console.
4. **Sync Gradle** and **Run** on device/emulator (minSdk 28).

**Required Gradle plugins/snippets:**

```kotlin
// settings.gradle
pluginManagement {
  repositories { gradlePluginPortal(); google(); mavenCentral() }
}
```

```kotlin
// app/build.gradle.kts (excerpt)
plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  id("com.google.gms.google-services")
  id("org.jetbrains.kotlin.plugin.compose")
}

android {
  namespace = "com.example.first"
  compileSdk = 35

  defaultConfig {
    applicationId = "com.example.first"
    minSdk = 28
    targetSdk = 35
    versionCode = 1
    versionName = "1.0"
  }
}

dependencies {
  implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
  implementation("com.google.firebase:firebase-auth-ktx")
  implementation("com.google.firebase:firebase-database-ktx")
  implementation("androidx.activity:activity-compose:1.9.2")
  implementation("androidx.compose.ui:ui:1.7.0")
  implementation("androidx.compose.material3:material3:1.3.0")
  implementation("androidx.navigation:navigation-compose:2.8.0")
}
```

---

## 🧭 Navigation (example)

```kotlin
sealed class Screen(val route: String) {
    data object Offer: Screen("offer")
    data object Categories: Screen("categories")
    data object Items: Screen("items/{category}") { const val ARG = "category" }
    data object Cart: Screen("cart")
    data object Login: Screen("login")
    data object Otp: Screen("otp")
    data object Error: Screen("error")
}
```

* Clicking a **category** navigates: `items/<categoryId>` → `ItemScreen` loads items for that key.
* Login flow navigates through **Login → OTP Verification → (Error if failed)**.

---

## 🧪 OTP Auth (Testing Mode Tips)

* Add **test phone numbers** in Firebase (no real SMS required).
* For real devices, set **SHA-1/256** in Firebase project settings for SMS Retriever API.
* Use `FirebaseAuth.getInstance()` with `PhoneAuthOptions` and handle `onVerificationCompleted` / `onCodeSent`.

---

## APK for Testing
(https://github.com/AdityaUpadhyay2610/GroCart/releases/download/Version1/app-debug.apk)

---

## 🧭 Learning Notes / What I’m Practicing

* Building UI with **Composable functions** and state hoisting.
* **Navigation** with arguments between screens.
* **Realtime Database** reads/writes & data mapping.
* Structuring app with **ViewModel + Repository**.
* Handling **OTP Login, Verification, and Error flows**.

---

## 🗺️ Roadmap / Future

* 🔍 Search & filtering
* ❤️ Wishlist / Favorites
* 🛒 Checkout & payment flow
* 📦 Order history & order details
* 👤 Profile screen & address management
* 🎨 Animations, theming, dark mode
* 🌐 API integration for dynamic products
* 🧪 UI tests & instrumentation tests

---

## 🤝 Contributing

Suggestions and PRs are welcome! If you find a bug or have an idea, please open an issue.

---

## 🙌 Acknowledgements

* Android Dev & Jetpack Compose docs
* Firebase for Auth & Realtime Database

---

### ⭐ If you like this project, consider giving it a star!


### ⭐ If you like this project, consider giving it a star!
