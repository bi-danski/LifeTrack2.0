# LifeTrack 2.0 - Project Analysis & Overview

## Project Summary

**LifeTrack 2.0** is a comprehensive **Android health tracking application** built with **Kotlin** and **Jetpack Compose**. It's a full-featured healthcare management platform that allows users to monitor their vital signs, manage medical appointments, track prescriptions, access AI-powered health insights, and communicate with healthcare professionals.

---

## 🏗️ Architecture Overview

### Architecture Pattern: **MVVM (Model-View-ViewModel)**

The project follows a clean, layered architecture:

```
UI Layer (Composable Screens)
    ↓
Presenter Layer (ViewModel - State Management)
    ↓
Repository Layer (Data Abstraction)
    ↓
Data Layer (Local DB, Remote API, DataStore)
```

### Key Architectural Layers:

1. **UI Layer** (`ui/`)
   - Jetpack Compose screens and components
   - Material Design 3 components
   - Navigation with Compose Navigation

2. **Presenter Layer** (`presenter/`)
   - ViewModels using AndroidX Lifecycle
   - State management with StateFlow/MutableStateFlow
   - Event handling with Channels

3. **Repository Layer** (`model/repository/`)
   - Abstracts data sources (local & remote)
   - Authentication, User data, Chat messages
   - Prescription and appointment data

4. **Data Layer** (`model/`)
   - **Room Database** for local persistence
   - **Ktor HTTP Client** for API communication
   - **DataStore** for preferences and session management
   - **Mock Data** for development/testing

---

## 📱 Key Features

### 1. **User Authentication & Session Management**
   - **Login/Signup** with email and password
   - **Token-based authentication** (Access + Refresh tokens)
   - **Session persistence** in DataStore
   - **Auto token refresh** with Ktor interceptors
   - Automatic logout on invalid tokens

### 2. **Health Vitals Tracking**
   - **Real-time vitals display:**
     - Heart Rate (BPM)
     - Blood Pressure (Systolic/Diastolic)
     - SpO2 (Oxygen Saturation)
     - Temperature
     - ECG Waveform data
   - **Activity Metrics:**
     - Step count with goals
     - Distance traveled
     - Elevation gain
     - Calories burned
     - Active minutes
     - Cadence and intensity levels
   - **Respiratory Metrics:**
     - SpO2 percentage
     - VO2 Max
     - Breaths per minute
     - Respiratory effort
   - **Sleep & Recovery:**
     - Sleep duration and quality score
     - REM and deep sleep duration
     - Skin temperature offset
     - Stress and readiness scores

### 3. **Prescription Management**
   - View active and expired prescriptions
   - Track medication dosage and instructions
   - Refill status tracking with progress indicators
   - Medication history
   - Doctor information and dates

### 4. **Appointment Scheduling**
   - Upcoming and past appointments
   - Doctor profiles with specialties and ratings
   - Hospital locations
   - Appointment status tracking (Upcoming, Attended, Cancelled, etc.)
   - Book and reschedule appointments
   - Appointment history timeline

### 5. **AI-Powered Health Assistant - "ALMA"**
   - Real-time chat with an AI health assistant
   - Multiple chat sessions management
   - Rename and delete chat histories
   - Local chat message storage (Room DB)
   - Integrates with backend `/alma/chat` endpoint

### 6. **Medical Analytics & Visualization**
   - **Blood Pressure trends** with line charts
   - **Respiratory data** visualization
   - **Vitals correlation analysis**
   - Multiple chart types (Line, Bar, Stacked)
   - Uses **Vico charting library** for visualization

### 7. **Medical Records**
   - **Medical visits** with diagnoses and treatment details
   - **Lab test results** with numeric and text results
   - **Attachments** (images, documents) for medical records
   - Verified records status

### 8. **Health Alerts & Notifications**
   - **Epidemic alerts** with severity levels and precautions
   - **Health recommendations** based on alerts
   - Push notifications capability
   - Alerts screen with detailed information

### 9. **User Profile & Settings**
   - User information: name, email, phone, LifeTrack ID
   - Profile customization
   - Settings: notifications, animations, reminders, data consent
   - Last active status tracking

### 10. **Telemedicine Integration**
   - View available doctors/specialists
   - Doctor ratings and experience
   - Hospital information
   - Consultation booking

---

## 🛠️ Technology Stack

### Core Framework
- **Kotlin** (Language)
- **Jetpack Compose** (UI Framework)
- **AndroidX** (Core libraries)

### Architecture & DI
- **Koin** (Dependency Injection)
- **Coroutines** (Async operations)
- **StateFlow/MutableStateFlow** (Reactive state management)

### Networking & API
- **Ktor Client** (HTTP client with auth interceptors)
- **Kotlinx Serialization** (JSON serialization)
- **Bearer Token Authentication** (OAuth-style)

### Local Storage
- **Room Database** (Relational data)
- **DataStore** (Preferences)

### UI Components
- **Material Design 3** (Material 3 design system)
- **Vico Charts** (Medical data visualization)
- **Compose Navigation** (Screen navigation)
- **Coil** (Image loading)

### Build System
- **Gradle KTS** (Build configuration)
- **Version Catalog** (Dependency management)
- **Proguard** (Code obfuscation)

### Testing
- **JUnit 4** (Unit testing)
- **Espresso** (UI testing)

---

## 📂 Project Structure

```
LifeTrack2.0/
├── app/
│   ├── src/main/
│   │   ├── java/org/lifetrack/ltapp/
│   │   │   ├── MainActivity.kt                 # Entry point
│   │   │   ├── di/
│   │   │   │   ├── KoinModule.kt               # DI configuration
│   │   │   │   └── KoinApplication.kt          # App initialization
│   │   │   ├── ui/
│   │   │   │   ├── screens/                    # All composable screens
│   │   │   │   │   ├── HomeScreen.kt
│   │   │   │   │   ├── VitalScreen.kt
│   │   │   │   │   ├── AppointScreen.kt
│   │   │   │   │   ├── PrescriptScreen.kt
│   │   │   │   │   ├── AlmaScreen.kt           # AI Assistant
│   │   │   │   │   ├── AnalyticScreen.kt
│   │   │   │   │   ├── LoginScreen.kt
│   │   │   │   │   └── ...more screens
│   │   │   │   ├── components/                 # Reusable UI components
│   │   │   │   ├── theme/                      # Material 3 theme
│   │   │   │   └── navigation/
│   │   │   │       └── LTNavigation.kt         # Navigation graph
│   │   │   ├── presenter/                      # ViewModels
│   │   │   │   ├── AuthPresenter.kt
│   │   │   │   ├── UserPresenter.kt
│   │   │   │   ├── ChatPresenter.kt
│   │   │   │   ├── PrescPresenter.kt
│   │   │   │   └── ...more presenters
│   │   │   ├── model/
│   │   │   │   ├── data/
│   │   │   │   │   ├── dclass/                 # Data classes
│   │   │   │   │   ├── dto/                    # Network DTOs
│   │   │   │   │   └── mock/                   # Mock data
│   │   │   │   ├── repository/                 # Repository interfaces & impl
│   │   │   │   ├── roomdb/                     # Room database
│   │   │   │   └── datastore/                  # DataStore preferences
│   │   │   ├── network/
│   │   │   │   ├── KtorHttpClient.kt           # HTTP client setup
│   │   │   │   └── NetworkObserver.kt          # Connectivity observer
│   │   │   ├── service/
│   │   │   │   ├── SessionManager.kt           # Session handling
│   │   │   │   └── AlmaService.kt              # AI Assistant API
│   │   │   ├── core/
│   │   │   │   ├── notification/               # Notifications
│   │   │   │   ├── broadcast/                  # Broadcast receivers
│   │   │   │   ├── security/                   # Security utilities
│   │   │   │   └── events/                     # UI events
│   │   │   └── utility/                        # Helper functions
│   │   ├── res/                                # Resources (drawables, strings, etc.)
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts
└── gradle.properties
```

---

## 🔄 Data Flow Example: Login Process

1. **User Input** → `LoginScreen` captures email & password
2. **UI State Update** → `AuthPresenter.login()` called
3. **API Call** → `AuthRepository.login()` sends to `/auth/login` endpoint
4. **Token Storage** → `PreferenceRepository` stores access & refresh tokens in DataStore
5. **Session Update** → `SessionManager` updates session state
6. **Navigation** → App navigates to home graph based on `SessionStatus.LOGGED_IN`

---

## 🔐 Security Features

1. **Token Management:**
   - Access tokens for API requests
   - Refresh tokens for session renewal
   - Automatic token rotation when expired

2. **Network Security:**
   - Bearer token in Authorization header
   - Ktor interceptors for auth handling
   - Request/response validation

3. **Local Storage:**
   - Encrypted DataStore preferences
   - Isolated Room database

4. **Obfuscation:**
   - ProGuard rules for release builds

---

## 🔗 Backend Integration

The app communicates with a backend API:

```
POST /auth/login          → User authentication
POST /auth/signup         → User registration
POST /auth/refresh        → Token refresh
POST /auth/logout         → Session termination
GET  /user/info           → User profile data
GET  /user/appointments   → User appointments
POST /alma/chat           → AI assistant endpoint
PUT  /user/update         → Profile updates
POST /auth/password-reset → Password recovery
```

---

## 🎨 UI/UX Design

- **Material Design 3** with dynamic colors
- **Glass Morphism** cards for modern look
- **Smooth Animations** using Compose Animation APIs
- **Dark/Light Theme** support
- **Responsive Layouts** for various screen sizes
- **BottomBar Navigation** for main sections
- **Bottom Sheet** dialogs for actions

---

## 📊 State Management

- **StateFlow** for reactive state
- **MutableStateFlow** for mutable state
- **Channels** for one-time events
- **SavedStateHandle** for screen arguments preservation
- **Koin** for dependency injection

---

## 🚀 App Flow

1. **Splash Screen** (Auto-hide after 3 seconds)
2. **Session Check** (SessionManager validates tokens)
3. **Auth State Decision:**
   - If logged out → `auth_graph` (Login/Signup screens)
   - If logged in → `home_graph` (Main app screens)
4. **Main Navigation:**
   - Home (Dashboard with vitals, activities)
   - Analytics (Charts and medical data)
   - Prescriptions (Medication management)
   - Appointments (Doctor visits)
   - ALMA (AI Assistant)
   - Chats (Message history)
   - Profile (User information)
   - Settings/Menu

---

## 📝 Key Screens

| Screen | Purpose |
|--------|---------|
| **HomeScreen** | Dashboard with vitals, activities, quick stats |
| **VitalScreen** | Detailed vital signs with charts and metrics |
| **AppointScreen** | Appointment booking and history |
| **PrescriptScreen** | Prescription management and tracking |
| **AlmaScreen** | AI health assistant chat interface |
| **AnalyticScreen** | Medical data visualization and trends |
| **ProfileScreen** | User information and profile management |
| **LoginScreen** | User authentication |
| **SignupScreen** | New user registration |

---

## 🔔 Notifications

- Push notifications support (requires Alarm Manager)
- Reminder scheduling via BroadcastReceiver
- Notification handling with Channels

---

## 💾 Local Database

**Room Database Schema:**
- `demChats` table: Stores ALMA chat messages with:
  - Message text
  - Sender (Patient/Assistant)
  - Timestamp
  - Chat type and session ID
  - Auto-incrementing ID

---

## 🎯 Development Notes

1. **Mock Data**: Extensive mock data available in `LtMockData.kt` for development
2. **Compose Metrics**: Enabled for performance monitoring
3. **ProGuard**: Active in release builds for code protection
4. **Coroutine Scopes**: Custom scope via Koin for lifecycle management
5. **Error Handling**: Network errors, validation errors, and user feedback mechanisms

---

## 📄 Configuration Files

- **build.gradle.kts**: App dependencies and build configuration
- **keystore.properties**: Signing key configuration
- **AndroidManifest.xml**: App permissions and metadata
- **proguard-rules.pro**: Code obfuscation rules

---

## ⚖️ Legal

**Proprietary and Confidential** - All rights reserved. The source code is provided for viewing purposes only. No reproduction, modification, or redistribution without explicit permission.

---