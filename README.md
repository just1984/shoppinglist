# 🎁 GiftTrack

**Geschenkplanung & Einkaufsübersicht für Android**

Eine native Android-App zur zentralen Verwaltung von Online-Einkäufen aus verschiedenen Shops mit Fokus auf Geschenkplanung durch Empfängerzuordnung.

## 📱 Features (Geplant)

- **Multi-Shop Einkaufsübersicht**: Zentrale Verwaltung aller Online-Bestellungen
- **Geschenkplanung**: Zuordnung von Produkten zu Empfängern
- **Sendestatus-Tracking**: Automatisches Tracking mit Push-Benachrichtigungen
- **Datenschutz & Sicherheit**: Lokale Speicherung, DSGVO-konform
- **Offline-First**: Volle Funktionalität ohne Internetverbindung

## 🛠️ Tech Stack

- **Kotlin** 1.9.22
- **Jetpack Compose** (Material 3)
- **Clean Architecture** + **MVVM**
- **Hilt** (Dependency Injection)
- **Room Database** (Lokale Datenhaltung)
- **Coroutines & Flow** (Asynchrone Programmierung)
- **Retrofit** (Netzwerk)
- **WorkManager** (Background Jobs)
- **Supabase** (Backend - geplant)

## 🚀 Setup

### Voraussetzungen

- Android Studio Hedgehog (2023.1.1) oder neuer
- JDK 17
- Android SDK 34
- Min SDK: 26 (Android 8.0)

### Build

```bash
# Projekt klonen
git clone <repository-url>
cd shoppinglist

# Dependencies installieren und bauen
./gradlew build

# App auf Emulator/Gerät installieren
./gradlew installDebug
```

## 📁 Projektstruktur

```
GiftTrack/
├── app/                          # Main Application Module
│   ├── src/main/
│   │   ├── java/com/gifttrack/app/
│   │   │   ├── ui/theme/         # Compose Theme
│   │   │   ├── MainActivity.kt
│   │   │   └── GiftTrackApplication.kt
│   │   ├── res/                  # Android Ressourcen
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── core/                         # Core Modules
│   ├── common/                   # Common utilities (Android)
│   ├── domain/                   # Business logic (Pure Kotlin)
│   ├── data/                     # Repository implementations
│   ├── database/                 # Room database
│   ├── network/                  # Retrofit API clients
│   └── ui/                       # Shared UI components
├── feature/                      # Feature Modules
│   ├── orders/                   # Order management
│   ├── recipients/               # Recipient management
│   ├── tracking/                 # Package tracking
│   └── settings/                 # App settings
├── claude/                       # Projektplanung
│   ├── epics/                    # Epic-Dokumente
│   ├── stories/                  # User Stories
│   └── PROGRESS.md               # Progress Tracking
├── docs/
│   ├── PRD.md                    # Product Requirements Document
│   └── TECHSTACK.md              # Tech Stack Dokumentation
└── ARCHITECTURE.md               # Architecture Documentation
```

Siehe [ARCHITECTURE.md](ARCHITECTURE.md) für Details zur Clean Architecture.

## 📖 Dokumentation

- [Product Requirements Document (PRD)](docs/PRD.md)
- [Tech Stack](docs/TECHSTACK.md)
- **[Architecture Documentation](ARCHITECTURE.md)** - Clean Architecture & Module Structure
- **[Hilt Dependency Injection](docs/HILT_DI.md)** - DI Setup & Best Practices
- **[Navigation](docs/NAVIGATION.md)** - Compose Navigation Setup
- [Development Progress](claude/PROGRESS.md)
- [Epics & User Stories](claude/README.md)

## 🎯 Entwicklungsstatus

**Current Sprint**: Setup Phase (Sprint 1)
**Status**: US-004 ✅ Abgeschlossen

- [x] US-001: Android-Projekt erstellen
- [x] US-002: Clean Architecture Module
- [x] US-003: Hilt Setup
- [x] US-004: Navigation Setup
- [ ] US-005: CI/CD Pipeline
- [ ] US-006: Code Quality Tools

## 🧪 Testing

```bash
# Unit Tests
./gradlew test

# UI Tests
./gradlew connectedAndroidTest

# Code Coverage
./gradlew jacocoTestReport
```

## 📝 Code Style

Das Projekt verwendet:
- **ktlint** für Kotlin Code-Formatierung
- **Detekt** für statische Code-Analyse
- **Android Lint** für Android-spezifische Checks

```bash
# Code formatieren
./gradlew ktlintFormat

# Code-Analyse
./gradlew detekt
```

## 🤝 Contributing

Siehe [Development Workflow](claude/README.md#workflow) für Details zum Entwicklungsprozess.

## 📄 Lizenz

*To be determined*

## 👥 Team

*To be filled*

---

**Version**: 1.0.0
**Erstellt**: 2025-11-23
**Status**: In Entwicklung
