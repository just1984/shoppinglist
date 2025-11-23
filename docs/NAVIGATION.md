# Navigation - GiftTrack

## 📋 Übersicht

GiftTrack verwendet **Jetpack Compose Navigation** für die App-Navigation. Die Navigation ist typ-sicher, deklarativ und vollständig in Compose integriert.

## 🏗️ Architektur

### Navigation Komponenten

```
MainActivity
    ↓
GiftTrackApp (Scaffold + NavController)
    ↓
├── GiftTrackBottomBar (Bottom Navigation)
└── GiftTrackNavHost (Navigation Graph)
    ↓
    ├── OrdersScreen
    ├── RecipientsScreen
    ├── TrackingScreen
    └── SettingsScreen
```

## 📦 Hauptkomponenten

### 1. Screen (Routes Definition)

**Datei**: `app/navigation/Screen.kt`

```kotlin
sealed class Screen(val route: String) {
    object Orders : Screen("orders")
    object Recipients : Screen("recipients")
    object Tracking : Screen("tracking")
    object Settings : Screen("settings")
}
```

**Zweck:**
- Typ-sichere Route-Definitionen
- Zentrale Verwaltung aller Destinations
- Vermeidung von String-Fehlern

### 2. GiftTrackNavHost

**Datei**: `app/navigation/GiftTrackNavHost.kt`

```kotlin
@Composable
fun GiftTrackNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = Screen.Orders.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = Screen.Orders.route) {
            OrdersScreen()
        }
        // ... andere Screens
    }
}
```

**Zweck:**
- Definiert den Navigation Graph
- Verknüpft Routes mit Composables
- Konfiguriert Start-Destination

### 3. GiftTrackBottomBar

**Datei**: `app/navigation/GiftTrackBottomBar.kt`

```kotlin
@Composable
fun GiftTrackBottomBar(
    navController: NavHostController
) {
    NavigationBar {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                icon = { Icon(...) },
                label = { Text(item.label) },
                selected = isSelected,
                onClick = { navController.navigate(item.screen.route) }
            )
        }
    }
}
```

**Features:**
- Material 3 Navigation Bar
- Automatische Selection-States
- State-Preservation beim Wechsel
- Single-Top Launch Mode

## 🎯 Features

### State Preservation

```kotlin
navController.navigate(route) {
    popUpTo(navController.graph.findStartDestination().id) {
        saveState = true
    }
    launchSingleTop = true
    restoreState = true
}
```

**Was das macht:**
- `saveState` - Speichert Screen-State beim Verlassen
- `launchSingleTop` - Verhindert Duplikate im Back Stack
- `restoreState` - Stellt State beim Zurückkehren wieder her

### Back Stack Management

Die Bottom Navigation poppt immer zurück zur Start-Destination, um den Back Stack sauber zu halten.

## 📱 Feature Screens

Jedes Feature-Modul hat seinen eigenen Screen:

### OrdersScreen
**Modul**: `feature:orders`
**Route**: `orders`
**Beschreibung**: Zeigt Liste aller Bestellungen

### RecipientsScreen
**Modul**: `feature:recipients`
**Route**: `recipients`
**Beschreibung**: Zeigt Liste aller Empfänger

### TrackingScreen
**Modul**: `feature:tracking`
**Route**: `tracking`
**Beschreibung**: Zeigt Tracking-Informationen

### SettingsScreen
**Modul**: `feature:settings`
**Route**: `settings`
**Beschreibung**: App-Einstellungen

## 🔄 Navigation Flow

### 1. User klickt auf Bottom Nav Item

```
User klickt "Empfänger"
    ↓
GiftTrackBottomBar onClick
    ↓
navController.navigate("recipients")
    ↓
NavHost findet composable mit route="recipients"
    ↓
RecipientsScreen wird angezeigt
```

### 2. Back Button Verhalten

- Innerhalb eines Screens: Zurück zur vorherigen Destination
- Auf Start-Screen (Orders): App verlassen

## 🚀 Navigation mit Argumenten (Future)

### Route mit Parametern

```kotlin
// Route Definition
object OrderDetails : Screen("orders/{orderId}") {
    fun createRoute(orderId: String) = "orders/$orderId"
}

// NavHost
composable(
    route = Screen.OrderDetails.route,
    arguments = listOf(navArgument("orderId") { type = NavType.StringType })
) { backStackEntry ->
    val orderId = backStackEntry.arguments?.getString("orderId")
    OrderDetailsScreen(orderId = orderId)
}

// Navigation
navController.navigate(Screen.OrderDetails.createRoute("order123"))
```

## 🔗 Deep Links (Vorbereitet)

```kotlin
composable(
    route = Screen.Orders.route,
    deepLinks = listOf(
        navDeepLink { uriPattern = "gifttrack://orders" }
    )
) {
    OrdersScreen()
}
```

**AndroidManifest.xml**:
```xml
<intent-filter>
    <action android:name="android.intent.action.VIEW" />
    <category android:name="android.intent.category.DEFAULT" />
    <category android:name="android.intent.category.BROWSABLE" />
    <data android:scheme="gifttrack" android:host="orders" />
</intent-filter>
```

## 🧪 Testing

### Navigation Testing

```kotlin
@Test
fun testNavigationToRecipients() {
    val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

    composeTestRule.setContent {
        navController.navigatorProvider.addNavigator(ComposeNavigator())
        GiftTrackNavHost(navController = navController)
    }

    // Navigate to recipients
    navController.navigate(Screen.Recipients.route)

    // Verify current route
    assertEquals(Screen.Recipients.route, navController.currentBackStackEntry?.destination?.route)
}
```

## 📝 Best Practices

### ✅ DO

- **Sealed Class für Routes** - Typ-Sicherheit
- **State Preservation konfigurieren** - Bessere UX
- **Single Top Launch** - Vermeidet Duplikate
- **NavController per Remember** - Wiederverwendung
- **Arguments mit NavType** - Typ-sichere Parameter

### ❌ DON'T

- **String-Routes direkt verwenden** - Fehleranfällig
- **NavController in ViewModel** - Verletzt Clean Architecture
- **Zu tiefer Back Stack** - Schlechte UX
- **Navigation ohne State Saving** - Verliert User-Daten
- **Compose Navigation mit Fragment** - Inkonsistent

## 🎨 UI Patterns

### Bottom Navigation
**Verwendet für**: Top-Level Destinations (3-5 Items)
**Aktuell**: Orders, Recipients, Tracking, Settings

### Navigation Drawer
**Verwendet für**: Mehr als 5 Top-Level Destinations
**Status**: Nicht implementiert (bei Bedarf)

### Top App Bar Navigation
**Verwendet für**: Hierarchische Navigation
**Status**: Für Detail-Screens geplant

## 🔍 Debugging

### Current Route anzeigen

```kotlin
val currentRoute = navController.currentBackStackEntry?.destination?.route
Log.d("Navigation", "Current route: $currentRoute")
```

### Back Stack inspizieren

```kotlin
navController.currentBackStack.value.forEach { entry ->
    Log.d("BackStack", "Route: ${entry.destination.route}")
}
```

## 🚧 Zukünftige Erweiterungen

### Geplante Features

1. **Nested Navigation Graphs**
   - Orders Graph (List → Details → Edit)
   - Recipients Graph (List → Details → Add)

2. **Shared Element Transitions**
   - Produktbilder zwischen Screens
   - Empfänger-Avatare

3. **Bottom Sheet Destinations**
   - Quick Actions
   - Filters

4. **Tab Navigation**
   - Innerhalb von Orders (Alle, Versendet, Geliefert)

## 📚 Ressourcen

- [Compose Navigation Guide](https://developer.android.com/jetpack/compose/navigation)
- [Navigation Best Practices](https://developer.android.com/guide/navigation/navigation-principles)
- [Navigation with Compose Codelab](https://developer.android.com/codelabs/jetpack-compose-navigation)

---

**Erstellt**: 2025-11-23
**Status**: ✅ Basis-Implementation abgeschlossen
**Version**: 1.0.0
