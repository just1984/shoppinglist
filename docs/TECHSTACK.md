# Tech Stack - GiftTrack Android App

## Übersicht

GiftTrack ist eine native Android-App entwickelt in **Kotlin** für Geschenkplanung und Einkaufsübersicht aus verschiedenen Online-Shops.

---

## 🎯 Plattform & Sprache

### Android
- **Target SDK**: Android 14 (API Level 34)
- **Min SDK**: Android 8.0 (API Level 26)
- **Programmiersprache**: **Kotlin 1.9+**
- **Build System**: Gradle (Kotlin DSL)

---

## 🏗️ Architektur

### Clean Architecture + MVVM
- **Presentation Layer**: Jetpack Compose + ViewModel
- **Domain Layer**: Use Cases / Interactors
- **Data Layer**: Repositories, Data Sources (Remote & Local)

### Modularisierung
```
app/
├── feature/          # Feature-Module (gifts, orders, recipients, tracking)
├── core/
│   ├── ui/          # Gemeinsame UI-Komponenten
│   ├── data/        # Datenbank, Netzwerk
│   ├── domain/      # Business Logic
│   └── common/      # Utilities, Extensions
```

---

## 🎨 UI Framework

### Jetpack Compose (Modern Declarative UI)
- **Material 3 Design** (Material You)
- **Compose Navigation** für Screen-Navigation
- **Compose State Management** (StateFlow, State Hoisting)

#### Vorteile:
- Weniger Boilerplate Code
- Reactive UI Updates
- Moderne, intuitive Entwicklung
- Preview Support

---

## 🔧 Core Libraries

### Android Jetpack Components

| Komponente | Verwendung |
|------------|------------|
| **Jetpack Compose** | UI Framework |
| **ViewModel** | UI State Management |
| **Room Database** | Lokale SQLite-Datenbank |
| **WorkManager** | Background Jobs (Tracking-Updates) |
| **DataStore** | Key-Value Storage (Preferences) |
| **Navigation Component** | App-Navigation |
| **Lifecycle** | Lifecycle-aware Components |
| **Paging 3** | Effizientes Laden großer Listen |

---

## 🌐 Backend & Synchronisation

### Supabase (Backend-as-a-Service)
- **Supabase Kotlin Client**
- **PostgreSQL Database** (Cloud)
- **Realtime Subscriptions** (für Statusupdates)
- **Authentication** (Email, OAuth)
- **Storage** (Produktbilder)

### Alternative/Ergänzung:
- **Firebase Cloud Messaging (FCM)** für Push-Benachrichtigungen

---

## 🔄 Netzwerk & API

### HTTP Client
- **Retrofit 2** oder **Ktor Client**
  - REST API Kommunikation
  - JSON Parsing mit **Kotlinx Serialization** oder **Moshi**

### API Integration
- Versanddienstleister APIs (DHL, DPD, Hermes, etc.)
- Shop-APIs (falls verfügbar)

---

## ⚡ Asynchrone Programmierung

### Kotlin Coroutines & Flow
- **Coroutines** für asynchrone Operationen
- **Flow** für reaktive Datenströme
- **StateFlow/SharedFlow** für UI State Management

```kotlin
// Beispiel
viewModelScope.launch {
    ordersRepository.getOrders()
        .collect { orders ->
            _uiState.value = UiState.Success(orders)
        }
}
```

---

## 💉 Dependency Injection

### Hilt (empfohlen)
- **Dagger Hilt** - Android-optimiertes DI Framework
- Compile-time Safety
- Integration mit Jetpack

**Alternative**: Koin (falls leichtgewichtiger bevorzugt)

---

## 💾 Lokale Datenspeicherung

### Room Database
- Lokale SQLite-Datenbank
- Offline-First Architektur
- Type-safe SQL Queries
- Flow/LiveData Support

### DataStore
- Verschlüsselte Preferences (Security Library)
- Proto DataStore für komplexe Objekte

### Struktur:
```
Entities:
- Order (Bestellung)
- Product (Produkt)
- Recipient (Empfänger)
- TrackingInfo (Sendestatus)
- Shop (Online-Shop)
```

---

## 🔐 Sicherheit & Datenschutz

### Verschlüsselung
- **Android Security Library** (EncryptedSharedPreferences)
- **SQLCipher** (optional für verschlüsselte Room DB)
- **HTTPS** für alle Netzwerkverbindungen

### Authentifizierung
- **Supabase Auth** (OAuth, Email/Password)
- **Biometric Authentication** (Fingerprint, Face)

### Datenschutz
- **DSGVO-konform**
- Lokale Datenspeicherung als Standard
- Opt-in für Cloud-Sync
- Keine Weitergabe an Dritte

---

## 📱 Notifications & Background Work

### WorkManager
- Periodisches Tracking-Update
- One-time Tasks (z.B. Benachrichtigungen)
- Constraints (Netzwerk, Akku)

### Push Notifications
- **Firebase Cloud Messaging (FCM)**
- Lokale Notifications für Statusänderungen

---

## 🧪 Testing

### Unit Tests
- **JUnit 5**
- **MockK** (Mocking für Kotlin)
- **Turbine** (Flow Testing)
- **Truth** (Assertions)

### UI Tests
- **Compose Testing** (semantics-based testing)
- **Espresso** (falls nötig)

### Integration Tests
- **Room Testing**
- **Retrofit MockWebServer**

---

## 🛠️ Build & Tooling

### Build Configuration
- **Gradle Kotlin DSL**
- **Version Catalogs** (libs.versions.toml)
- **Build Types**: Debug, Release
- **Product Flavors**: Free, Pro

### Code Quality
- **Detekt** - Static Code Analysis
- **ktlint** - Code Formatting
- **Android Lint**

### CI/CD
- **GitHub Actions**
- Automated Testing
- Release Builds

---

## 📦 Abhängigkeiten-Übersicht

```kotlin
// Core
- Kotlin 1.9+
- Coroutines 1.7+
- Kotlinx Serialization

// UI
- Jetpack Compose (BOM)
- Material 3
- Compose Navigation
- Coil (Image Loading)

// Architecture
- Lifecycle ViewModel
- Hilt

// Database
- Room 2.6+
- DataStore 1.0+

// Network
- Retrofit 2.9+
- OkHttp 4.12+
- Kotlinx Serialization

// Backend
- Supabase Kotlin Client

// Background
- WorkManager 2.9+

// Testing
- JUnit 5
- MockK
- Turbine
- Compose UI Test
```

---

## 🎯 Feature-spezifische Libraries

### Barcode/QR-Code Scanning
- **ML Kit Barcode Scanning** (für Trackingnummern)

### Image Handling
- **Coil** (Image Loading mit Compose Support)

### PDF Export (Future)
- **iText** oder **PDFBox Android**

### CSV Export
- **Apache Commons CSV**

---

## 📊 Analytics & Monitoring (Optional)

- **Firebase Analytics** (Nutzerverhalten)
- **Crashlytics** (Crash Reporting)
- **Sentry** (Alternative für Error Tracking)

---

## 🚀 Deployment

### App Distribution
- **Google Play Store** (Primary)
- **F-Droid** (Optional, Open Source)

### Versioning
- Semantic Versioning (MAJOR.MINOR.PATCH)
- Version Code Auto-Increment

---

## 📝 Notizen zur Implementierung

### MVP Priorisierung
1. ✅ Lokale Datenhaltung (Room)
2. ✅ Manuelle Bestellungseingabe (Compose UI)
3. ✅ Empfängerverwaltung
4. ✅ Basic Tracking Integration
5. ⏭️ Supabase Sync (später)
6. ⏭️ Auto-Import (Future Release)

### Offline-First Ansatz
- Alle Daten primär lokal speichern
- Optionale Cloud-Sync
- Konflikt-Auflösung bei Sync

---

## 🔄 Migration & Updates

- **Room AutoMigration** Support
- **Backwards Compatibility** für min SDK 26
- **Gradual Rollout** über Play Store

---

## ✅ Zusammenfassung

**GiftTrack** wird eine moderne, performante und sichere Android-App mit:
- **Kotlin** + **Jetpack Compose** (State-of-the-art UI)
- **Clean Architecture** (Wartbarkeit, Testbarkeit)
- **Offline-First** (Room Database)
- **Supabase Backend** (Cloud-Sync optional)
- **DSGVO-konform** (Privacy by Design)

Dieser Tech Stack ermöglicht schnelle Entwicklung, exzellente Performance und beste Android User Experience.
