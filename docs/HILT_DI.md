# Hilt Dependency Injection - GiftTrack

## 📋 Übersicht

GiftTrack verwendet **Hilt** als Dependency Injection Framework. Hilt ist ein kompilierzeit-basiertes DI-Framework, das auf Dagger aufbaut und speziell für Android optimiert ist.

## 🎯 Warum Hilt?

- ✅ **Kompilierzeit-Sicherheit** - Fehler werden beim Kompilieren gefunden
- ✅ **Weniger Boilerplate** - Automatische ViewModel-Integration
- ✅ **Android-optimiert** - Lifecycle-aware Components
- ✅ **Standardisiert** - Empfohlen von Google
- ✅ **Testbarkeit** - Einfaches Mocken für Tests

## 🏗️ Hilt Setup

### 1. Application-Klasse

```kotlin
@HiltAndroidApp
class GiftTrackApplication : Application() {
    // Hilt generiert automatisch die DI-Container
}
```

### 2. Activity/Fragment

```kotlin
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    // Aktiviert DI für diese Activity
}
```

### 3. ViewModel

```kotlin
@HiltViewModel
class MainViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase
) : ViewModel() {
    // UseCase wird automatisch injiziert
}
```

## 📦 Hilt Module

### DatabaseModule (`core:database`)

Stellt Database und DAOs bereit:

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideGiftTrackDatabase(
        @ApplicationContext context: Context
    ): GiftTrackDatabase

    @Provides
    @Singleton
    fun provideOrderDao(database: GiftTrackDatabase): OrderDao

    @Provides
    @Singleton
    fun provideRecipientDao(database: GiftTrackDatabase): RecipientDao
}
```

**Bereitgestellt:**
- `GiftTrackDatabase` - Room Database Instanz
- `OrderDao` - DAO für Order Entities
- `RecipientDao` - DAO für Recipient Entities

### RepositoryModule (`core:data`)

Bindet Repository-Interfaces an Implementierungen:

```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindOrderRepository(
        orderRepositoryImpl: OrderRepositoryImpl
    ): OrderRepository

    @Binds
    @Singleton
    abstract fun bindRecipientRepository(
        recipientRepositoryImpl: RecipientRepositoryImpl
    ): RecipientRepository
}
```

**Bereitgestellt:**
- `OrderRepository` → `OrderRepositoryImpl`
- `RecipientRepository` → `RecipientRepositoryImpl`

### NetworkModule (`core:network`)

Stellt Netzwerk-Dependencies bereit:

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit
}
```

**Bereitgestellt:**
- `Json` - Kotlinx Serialization JSON
- `OkHttpClient` - HTTP Client
- `Retrofit` - REST API Client

## 🔄 Dependency Flow

```
MainActivity (@AndroidEntryPoint)
    ↓
MainViewModel (@HiltViewModel)
    ↓
GetOrdersUseCase (@Inject)
    ↓
OrderRepository (Interface)
    ↓
OrderRepositoryImpl (@Inject) ← RepositoryModule
    ↓
OrderDao (@Provides) ← DatabaseModule
    ↓
GiftTrackDatabase (@Provides) ← DatabaseModule
```

## 💉 Injection Types

### Constructor Injection (Empfohlen)

```kotlin
class OrderRepositoryImpl @Inject constructor(
    private val orderDao: OrderDao
) : OrderRepository {
    // orderDao wird automatisch injiziert
}
```

### Field Injection (Nur für Android Components)

```kotlin
@AndroidEntryPoint
class MyFragment : Fragment() {
    @Inject
    lateinit var repository: OrderRepository
}
```

### ViewModels

```kotlin
@Composable
fun MyScreen(
    viewModel: MyViewModel = hiltViewModel()
) {
    // ViewModel wird automatisch mit Dependencies erstellt
}
```

## 🧪 Testing mit Hilt

### Test Module

```kotlin
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
abstract class TestRepositoryModule {
    @Binds
    abstract fun bindOrderRepository(
        fake: FakeOrderRepository
    ): OrderRepository
}
```

### Test Setup

```kotlin
@HiltAndroidTest
class MainViewModelTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: OrderRepository

    @Test
    fun testOrders() {
        // Test mit echtem DI-Graph
    }
}
```

## 🎨 Scopes

### SingletonComponent
- Lebt während der gesamten App-Laufzeit
- Verwendet für Repositories, Database, Network

```kotlin
@InstallIn(SingletonComponent::class)
```

### ActivityRetainedComponent
- Überlebt Configuration Changes (z.B. Screen Rotation)
- Verwendet für ViewModels

```kotlin
@InstallIn(ActivityRetainedComponent::class)
```

### ActivityComponent
- Lebt während Activity Lifecycle
- Verwendet für Activity-spezifische Dependencies

```kotlin
@InstallIn(ActivityComponent::class)
```

## 📝 Best Practices

### ✅ DO

- **Constructor Injection verwenden** wo möglich
- **@Singleton für teure Objekte** (Database, Network)
- **Interfaces für Repositories** (einfacheres Testen)
- **Module in passenden Packages** (core:data/di)
- **@Provides für externe Klassen** (Room, Retrofit)
- **@Binds für eigene Interfaces** (effizienter)

### ❌ DON'T

- **Field Injection in normalen Klassen** (nur für Android Components)
- **Lateinit ohne Initialisierung**
- **Zirkuläre Dependencies**
- **Zu viele Singletons** (Memory Leaks)
- **Context in Singletons** (außer ApplicationContext)

## 🔍 Debugging

### Hilt Code Generation prüfen

Generierte Klassen befinden sich in:
```
build/generated/hilt/
```

### Häufige Fehler

**Missing Binding:**
```
error: [Dagger/MissingBinding] OrderDao cannot be provided
```
→ Module fehlt oder nicht installiert

**Duplicate Bindings:**
```
error: [Dagger/DuplicateBindings] OrderRepository is bound multiple times
```
→ Mehrere @Provides oder @Binds für selben Typ

**Wrong Scope:**
```
error: dependency scopes incompatible
```
→ Singleton kann nicht in ActivityScoped injiziert werden

## 📚 Ressourcen

- [Hilt Documentation](https://dagger.dev/hilt/)
- [Android Hilt Guide](https://developer.android.com/training/dependency-injection/hilt-android)
- [Hilt Codelab](https://developer.android.com/codelabs/android-hilt)

---

**Erstellt**: 2025-11-23
**Status**: ✅ Vollständig konfiguriert
**Version**: 1.0.0
