# US-001: Android Projekt mit Kotlin & Compose erstellen

**Epic**: E01 - Projekt-Setup & Infrastruktur
**Priority**: Critical
**Story Points**: 3
**Status**: Not Started

## User Story

Als **Entwickler** möchte ich **ein neues Android-Projekt mit Kotlin und Jetpack Compose** erstellen, damit ich mit der Entwicklung der GiftTrack-App beginnen kann.

## Beschreibung

Erstellen eines neuen Android Studio Projekts mit den neuesten Versionen von Kotlin und Jetpack Compose. Das Projekt soll die Basis für alle weiteren Entwicklungen bilden.

## Akzeptanzkriterien

- [ ] Android Studio Projekt ist erstellt
- [ ] Target SDK: Android 14 (API Level 34)
- [ ] Min SDK: Android 8.0 (API Level 26)
- [ ] Kotlin 1.9+ ist konfiguriert
- [ ] Jetpack Compose BOM ist eingebunden
- [ ] Material 3 Design ist konfiguriert
- [ ] Gradle Kotlin DSL wird verwendet
- [ ] Version Catalog (libs.versions.toml) ist eingerichtet
- [ ] Projekt kompiliert ohne Fehler
- [ ] Eine "Hello GiftTrack" Compose UI wird angezeigt

## Technische Details

### build.gradle.kts (Project)
```kotlin
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
}
```

### build.gradle.kts (App)
```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.gifttrack.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.gifttrack.app"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.activity.compose)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
```

### libs.versions.toml
```toml
[versions]
kotlin = "1.9.22"
compose-bom = "2024.02.00"
activity-compose = "1.8.2"

[libraries]
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "compose-bom" }
androidx-compose-ui = { group = "androidx.compose.ui", name = "ui" }
androidx-compose-material3 = { group = "androidx.compose.material3", name = "material3" }
androidx-compose-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
androidx-compose-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "activity-compose" }

[plugins]
android-application = { id = "com.android.application", version = "8.2.2" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
compose-compiler = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
```

### MainActivity.kt
```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GiftTrackTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Text("Hello GiftTrack!")
                }
            }
        }
    }
}
```

## Definition of Done

- [ ] Code ist geschrieben
- [ ] App läuft auf Emulator und physischem Gerät
- [ ] Keine Compiler-Warnungen
- [ ] README.md mit Setup-Anleitung erstellt
- [ ] Git Repository initialisiert
- [ ] .gitignore für Android konfiguriert

## Dependencies

Keine

## Notizen

- Verwende Android Studio Jellyfish oder neuer
- Compose Compiler Plugin statt separater composeOptions
