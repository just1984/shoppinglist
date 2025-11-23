# E02 - Bestellverwaltung

**Epic ID**: E02
**Status**: Not Started
**Priority**: High
**Sprint**: Sprint 2-3

## Beschreibung

Implementierung der zentralen Bestellverwaltung, die es Nutzern ermöglicht, Bestellungen aus verschiedenen Online-Shops manuell hinzuzufügen, anzuzeigen, zu bearbeiten und zu verwalten.

## Ziele

- Nutzer können Bestellungen manuell erfassen
- Übersichtliche Darstellung aller Bestellungen
- Filter- und Sortierfunktionen
- Offline-First mit lokaler Speicherung
- Produktbilder und Details verwalten

## Umfang

### In Scope
- Room Database Schema für Bestellungen, Produkte, Shops
- CRUD Operationen für Bestellungen
- UI für Bestellungsübersicht (Liste)
- UI für Bestellungsdetails
- UI für Bestellung hinzufügen/bearbeiten
- Produktbilder lokal speichern
- Filter nach Shop, Status, Datum
- Sortierung nach verschiedenen Kriterien
- Suchfunktion

### Out of Scope
- Automatischer Import aus E-Mails
- API-Integration mit Shops
- Empfänger-Zuordnung (E03)
- Tracking-Integration (E04)

## User Stories

- US-010: Room Database Schema definieren
- US-011: Repository Pattern für Bestellungen implementieren
- US-012: Bestellungsübersicht UI erstellen
- US-013: Bestellung manuell hinzufügen
- US-014: Bestellungsdetails anzeigen
- US-015: Bestellung bearbeiten
- US-016: Bestellung löschen
- US-017: Produktbilder hochladen und speichern
- US-018: Shop-Verwaltung implementieren
- US-019: Filter- und Sortieroptionen
- US-020: Suchfunktion implementieren
- US-021: Bestellstatus verwalten (bestellt, versandt, geliefert, etc.)

## Technische Details

### Database Schema

```kotlin
@Entity(tableName = "orders")
data class Order(
    @PrimaryKey val id: String,
    val orderNumber: String,
    val shopId: String,
    val orderDate: LocalDateTime,
    val status: OrderStatus,
    val totalAmount: Double?,
    val currency: String,
    val notes: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

@Entity(tableName = "products")
data class Product(
    @PrimaryKey val id: String,
    val orderId: String,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val price: Double?,
    val quantity: Int,
    val recipientId: String? // Foreign key to E03
)

@Entity(tableName = "shops")
data class Shop(
    @PrimaryKey val id: String,
    val name: String,
    val logoUrl: String?,
    val website: String?
)
```

### UI Screens
- OrdersListScreen (Hauptübersicht)
- OrderDetailScreen
- AddEditOrderScreen
- ShopSelectionScreen

## Akzeptanzkriterien

- [ ] Bestellungen können hinzugefügt, angezeigt, bearbeitet und gelöscht werden
- [ ] Alle Daten werden lokal in Room gespeichert
- [ ] Produktbilder werden lokal gespeichert (keine Cloud)
- [ ] Filter nach Shop, Status, Datum funktionieren
- [ ] Sortierung nach Datum, Shop, Status möglich
- [ ] Suchfunktion findet Bestellungen nach Name/Nummer
- [ ] UI ist intuitiv und Material 3 konform
- [ ] Offline-Funktionalität gewährleistet
- [ ] Unit Tests für Repository
- [ ] UI Tests für kritische Flows

## Abhängigkeiten

- E01 (Projekt-Setup) muss abgeschlossen sein

## Risiken

- Performance bei vielen Bestellungen (>1000)
- Bildgröße könnte lokalen Speicher füllen

## Geschätzter Aufwand

**Story Points**: 21
**Dauer**: 2 Sprints

## Notes

Dies ist das Herzstück der App. Sorgfältiges Design der Datenbank ist wichtig für spätere Features.
