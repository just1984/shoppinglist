# GiftTrack - Architecture Documentation

## 📐 Clean Architecture

GiftTrack follows **Clean Architecture** principles with a modular structure. The architecture ensures separation of concerns, testability, and maintainability.

## 🏗️ Module Structure

```
GiftTrack/
├── app/                          # Main Application Module
├── core/                         # Core functionality shared across features
│   ├── common/                   # Common utilities & extensions (Android)
│   ├── domain/                   # Business logic layer (Pure Kotlin)
│   ├── data/                     # Repository implementations
│   ├── database/                 # Room database & DAOs
│   ├── network/                  # Retrofit API clients
│   └── ui/                       # Shared UI components & theme
└── feature/                      # Feature modules
    ├── orders/                   # Order management feature
    ├── recipients/               # Recipient management feature
    ├── tracking/                 # Package tracking feature
    └── settings/                 # App settings feature
```

## 🎯 Layer Responsibilities

### 1. **Domain Layer** (`core:domain`)
- **Pure Kotlin module** (no Android dependencies)
- Contains business logic, use cases, and domain models
- Defines repository interfaces
- Independent of frameworks

**Key Components:**
- `Order` - Domain model for orders
- `Recipient` - Domain model for recipients
- `OrderRepository` - Repository interface
- `GetOrdersUseCase` - Business logic for fetching orders

### 2. **Data Layer** (`core:data`, `core:database`, `core:network`)
- Implements repository interfaces from domain layer
- Handles data sources (local & remote)
- Maps between data models and domain models
- Manages caching strategies

**Modules:**
- `core:database` - Room database, DAOs, entities
- `core:network` - Retrofit services, DTOs
- `core:data` - Repository implementations, mappers

### 3. **Presentation Layer** (feature modules)
- Jetpack Compose UI
- ViewModels (MVVM pattern)
- Navigation logic
- Feature-specific screens

**Modules:**
- `feature:orders` - Order list, details, add/edit screens
- `feature:recipients` - Recipient management screens
- `feature:tracking` - Package tracking screens
- `feature:settings` - Settings & preferences screens

### 4. **Common Layer** (`core:common`, `core:ui`)
- Shared utilities and extensions
- Common UI components
- Theme & design system
- Result/error handling

## 🔄 Dependency Flow

The dependency rule ensures that dependencies point inward:

```
Presentation Layer (Features)
         ↓ depends on
    Domain Layer
         ↑ implemented by
     Data Layer
```

**Rules:**
- ✅ Domain layer has **NO** Android dependencies
- ✅ Data layer depends on Domain layer
- ✅ Presentation layer depends on Domain layer
- ✅ Feature modules are independent of each other
- ❌ Domain layer never depends on Data or Presentation

## 📦 Module Dependencies

### App Module
```kotlin
implementation(project(":feature:orders"))
implementation(project(":feature:recipients"))
implementation(project(":feature:tracking"))
implementation(project(":feature:settings"))
implementation(project(":core:ui"))
implementation(project(":core:data"))
```

### Feature Modules (e.g., `feature:orders`)
```kotlin
implementation(project(":core:domain"))
implementation(project(":core:ui"))
implementation(project(":core:common"))
```

### Data Module
```kotlin
implementation(project(":core:domain"))
implementation(project(":core:database"))
implementation(project(":core:network"))
implementation(project(":core:common"))
```

### Domain Module
```kotlin
// Pure Kotlin - minimal dependencies
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
implementation("javax.inject:javax.inject")
```

## 🛠️ Technology Stack per Layer

### Domain
- Pure Kotlin
- Coroutines & Flow
- javax.inject (Dependency Injection)

### Data
- Room (Database)
- Retrofit (Networking)
- Kotlinx Serialization
- Hilt (DI)

### Presentation
- Jetpack Compose
- Material 3
- ViewModel & StateFlow
- Hilt
- Navigation Compose

### Common
- Kotlin Extensions
- Result wrapper
- Date utilities

## 🔍 Example Data Flow

**Use Case: Fetching Orders**

1. **User Action** → `OrdersScreen` (Presentation)
2. **ViewModel** → Calls `GetOrdersUseCase` (Domain)
3. **Use Case** → Calls `OrderRepository.getOrders()` (Domain Interface)
4. **Repository Implementation** → Fetches from `OrderDao` (Data)
5. **DAO** → Queries Room Database (Data)
6. **Mapper** → Converts `OrderEntity` → `Order` (Data → Domain)
7. **Flow** → Emits `List<Order>` through layers
8. **ViewModel** → Updates UI state
9. **Compose** → Renders updated state

## ✅ Benefits of This Architecture

### Testability
- **Domain layer**: Pure Kotlin, easy to unit test
- **Data layer**: Mock repositories & data sources
- **Presentation**: Test ViewModels independently

### Maintainability
- Clear separation of concerns
- Changes in one layer don't affect others
- Easy to locate bugs

### Scalability
- Add new features as independent modules
- Parallel development by teams
- Reusable core modules

### Flexibility
- Swap implementations (e.g., Room → another DB)
- Replace UI framework without touching business logic
- Easy to add new data sources

## 📝 Naming Conventions

### Packages
- `com.gifttrack.core.domain.model` - Domain models
- `com.gifttrack.core.domain.repository` - Repository interfaces
- `com.gifttrack.core.domain.usecase` - Use cases
- `com.gifttrack.core.data.repository` - Repository implementations
- `com.gifttrack.feature.orders.ui` - Feature UI

### Classes
- `Order` - Domain model
- `OrderEntity` - Database entity
- `OrderDto` - Network DTO
- `OrderRepository` - Interface
- `OrderRepositoryImpl` - Implementation
- `GetOrdersUseCase` - Use case
- `OrdersViewModel` - ViewModel
- `OrdersScreen` - Composable screen

## 🚀 Next Steps

1. **US-003**: Implement Hilt dependency injection
2. **US-004**: Setup Navigation Component
3. **US-010**: Implement Room database schema
4. **US-012**: Create Order list UI

## 📚 References

- [Clean Architecture - Uncle Bob](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Android Architecture Guide](https://developer.android.com/topic/architecture)
- [Now in Android - Modularization](https://github.com/android/nowinandroid/blob/main/docs/ModularizationLearningJourney.md)

---

**Last Updated**: 2025-11-23
**Version**: 1.0.0
