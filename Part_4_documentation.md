## 🧠 Overview
This document outlines the technical approach and architectural decisions made while building the Teebay Android application — a platform where users can rent, buy, or sell products. The project follows the MVVM repository pattern using Kotlin, and interacts with a Django-based backend and Firebase Cloud Messaging.

## 🔐 PART 1: Login & Registration (with Biometrics)

### 🔧 Implementation Details
- The app provides both user registration and login functionalities.
- After a successful login or registration, credentials are securely stored using EncryptedSharedPreferences (androidx.security.crypto).
- Biometric authentication is implemented using:
    ```scss
    implementation("androidx.biometric:biometric:1.2.0")
    ```
- Biometric login is disabled by default and only enabled after manual login.
- Upon biometric authentication success, the stored credentials are retrieved and sent to the login API.

### 🛑 Error Handling
- If biometric authentication fails or is canceled, the user is notified via a Toast with a relevant message.
- Biometric prompts only show if a user is already registered and has opted in.

## 📦 PART 2: Product Management (Add/Edit/Delete)

### 🧭 Multi-Page Form
- The product creation flow is built using ViewPager2 + FragmentStateAdapter.
- Each step of the form is implemented as a separate Fragment, with navigation between steps and state persisted via activityViewModels.

### 📥 Data Validation
- Field-level validations (e.g., required fields) are implemented in the Repository layer before making API requests.

### 🗂 Category Selection
- Products can be categorized into a single category (per wireframe).
- A dropdown spinner is used for category selection.

### ✏️ Edit & 🗑 Delete
- Users can only modify or delete their own products.
- This is enforced by filtering products based on the logged-in user’s ID.
- Product updates are done using a PATCH request via Retrofit.

## 💳 PART 3: Rent and Buy/Sell Features

### 🗃 Listing All Products
- All products (from all users) are fetched and displayed using a RecyclerView.
- Large datasets are efficiently rendered using view recycling and pagination readiness.

### 💰 Purchase & Rent
- On "Buy" or "Rent", a transaction request is sent to the backend.
- Product rent time selection is handled via DatePickerDialog + TimePickerDialog.
- Validation ensures start time is earlier than end time.

### 📊 My Orders View
- A bottom navigation bar includes a My Orders section.
- Inside this section:
    - ViewPager2 + TabLayout displays two tabs: Purchased and Rented.
    - Fetched transactions are filtered accordingly and displayed in their respective tabs.

### 🧠 Nested API Call Handling
- Each transaction provides only a product_id, requiring a secondary call to fetch product details.
- These calls are executed using coroutines with supervisorScope to avoid failure propagation.
- All API interactions are scoped under viewModelScope.

## 🔔 FCM Push Notifications

### 🔧 Firebase Setup
- Firebase was configured using the provided JSON.
- Messaging is handled using FirebaseMessagingService.

### 🎯 Navigation from Notification
- Notifications include the relevant product_id.
- Clicking the notification launches the app and navigates to the product detail page using a PendingIntent.

### ❌ Error Handling
- If the product is not found or an error occurs during navigation, the user receives a Toast message.

## 🏗️ App Architecture

### 📐 Design Pattern
- Follows MVVM with Repository pattern.
- Network layer includes:
    - Retrofit for API calls.
    - OkHttp with logging interceptor.
- UI layer uses:
    - LiveData and ViewModel.
    - Glide for image loading.
    - Sealed classes for managing UI states (ResponseState).

### 🧪 UI/UX State Management
- Each API response is mapped to a unified Result<T> using a mapResult() extension.
- UI reacts to:
    - Loading state: ProgressBar or Shimmer.
    - Success state: Data display.
    - Failure state: User-friendly error messages.