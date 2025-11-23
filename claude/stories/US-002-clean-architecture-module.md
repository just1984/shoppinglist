# US-002: Clean Architecture Module einrichten

**Epic**: E01 - Projekt-Setup & Infrastruktur
**Priority**: Critical
**Story Points**: 5
**Status**: Not Started

## User Story

Als **Entwickler** möchte ich **eine modulare Projektstruktur nach Clean Architecture** einrichten, damit die App wartbar, testbar und skalierbar ist.

## Beschreibung

Einrichtung einer modularen Projektstruktur mit Trennung von Presentation, Domain und Data Layer gemäß Clean Architecture Prinzipien.

## Akzeptanzkriterien

- [ ] Modul-Struktur ist erstellt
- [ ] Gradle-Module sind konfiguriert
- [ ] Abhängigkeiten zwischen Modulen sind korrekt definiert
- [ ] Jedes Modul hat eigene build.gradle.kts
- [ ] Domain-Modul hat keine Android-Dependencies
- [ ] Data-Modul implementiert Domain-Interfaces
- [ ] Feature-Module verwenden Domain und Data
- [ ] Convention Plugins für gemeinsame Konfiguration
- [ ] Projekt kompiliert erfolgreich

## Technische Details

### Modul-Struktur

```
shoppinglist/
├── app/                          # Main Application Module
├── core/
│   ├── common/                   # Common utilities, extensions
│   ├── domain/                   # Business logic, use cases (Pure Kotlin)
│   ├── data/                     # Repository implementations, data sources
│   ├── database/                 # Room database
│   ├── network/                  # Retrofit/API clients
│   └── ui/                       # Shared UI components, theme
├── feature/
│   ├── orders/                   # Orders feature module
│   ├── recipients/               # Recipients feature module
│   ├── tracking/                 # Tracking feature module
│   └── settings/                 # Settings feature module
└── build-logic/                  # Convention plugins
    └── convention/
```

### settings.gradle.kts
```kotlin
pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "GiftTrack"

include(":app")

// Core modules
include(":core:common")
include(":core:domain")
include(":core:data")
include(":core:database")
include(":core:network")
include(":core:ui")

// Feature modules
include(":feature:orders")
include(":feature:recipients")
include(":feature:tracking")
include(":feature:settings")
```

### core:domain/build.gradle.kts
```kotlin
plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.javax.inject)
}
```

### core:data/build.gradle.kts
```kotlin
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.gifttrack.core.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:database"))
    implementation(project(":core:network"))
    implementation(project(":core:common"))

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.kotlinx.coroutines.android)
}
```

### feature:orders/build.gradle.kts
```kotlin
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.gifttrack.feature.orders"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:ui"))
    implementation(project(":core:common"))

    // Compose
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)
}
```

### Domain Layer Beispiel

```kotlin
// core:domain/src/main/kotlin/com/gifttrack/domain/model/Order.kt
package com.gifttrack.domain.model

data class Order(
    val id: String,
    val orderNumber: String,
    val shopName: String,
    val orderDate: LocalDateTime,
    val status: OrderStatus
)

enum class OrderStatus {
    ORDERED, SHIPPED, DELIVERED, CANCELLED
}
```

```kotlin
// core:domain/src/main/kotlin/com/gifttrack/domain/repository/OrderRepository.kt
package com.gifttrack.domain.repository

import com.gifttrack.domain.model.Order
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getOrders(): Flow<List<Order>>
    suspend fun getOrderById(id: String): Order?
    suspend fun insertOrder(order: Order)
    suspend fun deleteOrder(id: String)
}
```

```kotlin
// core:domain/src/main/kotlin/com/gifttrack/domain/usecase/GetOrdersUseCase.kt
package com.gifttrack.domain.usecase

import com.gifttrack.domain.model.Order
import com.gifttrack.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    operator fun invoke(): Flow<List<Order>> {
        return repository.getOrders()
    }
}
```

### Data Layer Beispiel

```kotlin
// core:data/src/main/kotlin/com/gifttrack/data/repository/OrderRepositoryImpl.kt
package com.gifttrack.data.repository

import com.gifttrack.domain.model.Order
import com.gifttrack.domain.repository.OrderRepository
import com.gifttrack.database.dao.OrderDao
import com.gifttrack.data.mapper.toEntity
import com.gifttrack.data.mapper.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val orderDao: OrderDao
) : OrderRepository {
    override fun getOrders(): Flow<List<Order>> {
        return orderDao.getOrders().map { entities ->
            entities.map { it.toModel() }
        }
    }

    override suspend fun getOrderById(id: String): Order? {
        return orderDao.getOrderById(id)?.toModel()
    }

    override suspend fun insertOrder(order: Order) {
        orderDao.insert(order.toEntity())
    }

    override suspend fun deleteOrder(id: String) {
        orderDao.deleteById(id)
    }
}
```

## Definition of Done

- [ ] Alle Module sind erstellt und konfiguriert
- [ ] Projekt kompiliert ohne Fehler
- [ ] Abhängigkeiten sind korrekt (Domain hat keine Android-Dependencies)
- [ ] Beispiel Use Case, Repository und Model sind implementiert
- [ ] Architektur-Diagramm in README dokumentiert
- [ ] Code Review durchgeführt

## Dependencies

- US-001 (Projekt-Setup)

## Notizen

- Domain-Modul sollte Pure Kotlin sein (kein Android)
- Dependency Rule: Domain ← Data ← Presentation
- Convention Plugins später für weniger Boilerplate
