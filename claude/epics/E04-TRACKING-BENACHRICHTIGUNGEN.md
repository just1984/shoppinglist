# E04 - Tracking & Benachrichtigungen

**Epic ID**: E04
**Status**: Not Started
**Priority**: Medium
**Sprint**: Sprint 5-6

## Beschreibung

Implementierung des automatischen Sendungstrackings mit Integration verschiedener Versanddienstleister und Push-Benachrichtigungen bei Statusänderungen.

## Ziele

- Automatisches Tracking von Sendungen
- Integration mehrerer Versanddienstleister
- Push-Benachrichtigungen bei Statusänderungen
- Historische Tracking-Daten speichern
- Lieferzeiten und Verzögerungen anzeigen

## Umfang

### In Scope
- Room Database für Tracking-Informationen
- API-Integration für Versanddienstleister (DHL, DPD, Hermes, UPS, etc.)
- Barcode/QR-Code Scanner für Trackingnummern
- WorkManager für periodische Updates
- Firebase Cloud Messaging für Push-Notifications
- Lokale Benachrichtigungen
- Tracking-Historie anzeigen
- Lieferstatus-Übersicht

### Out of Scope
- Automatische Erkennung des Versanddienstleisters (kann manuell ausgewählt werden)
- Live-Karte mit Lieferposition

## User Stories

- US-040: Tracking-Datenbank-Schema erstellen
- US-041: Trackingnummer zu Bestellung hinzufügen
- US-042: Barcode-Scanner für Trackingnummern implementieren
- US-043: API-Integration für DHL
- US-044: API-Integration für DPD
- US-045: API-Integration für Hermes
- US-046: API-Integration für UPS/DHL Express
- US-047: API-Integration für GLS
- US-048: WorkManager für periodische Updates einrichten
- US-049: Push-Benachrichtigungen implementieren (FCM)
- US-050: Lokale Benachrichtigungen bei Statusänderung
- US-051: Tracking-Historie in UI anzeigen
- US-052: Lieferstatus-Übersicht implementieren
- US-053: Manuelle Versanddienstleister-Auswahl
- US-054: Fehlerbehandlung für fehlgeschlagene API-Calls
- US-055: Retry-Logik für Tracking-Updates

## Technische Details

### Database Schema

```kotlin
@Entity(tableName = "tracking_info")
data class TrackingInfo(
    @PrimaryKey val id: String,
    val orderId: String,
    val trackingNumber: String,
    val carrier: Carrier,
    val currentStatus: TrackingStatus,
    val estimatedDelivery: LocalDateTime?,
    val lastUpdated: LocalDateTime,
    val createdAt: LocalDateTime
)

@Entity(tableName = "tracking_events")
data class TrackingEvent(
    @PrimaryKey val id: String,
    val trackingInfoId: String,
    val status: TrackingStatus,
    val location: String?,
    val timestamp: LocalDateTime,
    val description: String
)

enum class Carrier {
    DHL, DPD, HERMES, UPS, GLS, DHL_EXPRESS, FEDEX, AMAZON_LOGISTICS
}

enum class TrackingStatus {
    LABEL_CREATED,
    PICKED_UP,
    IN_TRANSIT,
    OUT_FOR_DELIVERY,
    DELIVERED,
    EXCEPTION,
    FAILED_DELIVERY
}
```

### API Integration
- **DHL**: DHL Parcel API
- **DPD**: DPD API
- **Hermes**: Hermes API
- Alternativ: Universal Tracking APIs wie AfterShip oder Shippo

### WorkManager
```kotlin
// Periodisches Update alle 4 Stunden
PeriodicWorkRequestBuilder<TrackingUpdateWorker>(4, TimeUnit.HOURS)
    .setConstraints(
        Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    )
```

### UI Components
- TrackingDetailScreen
- TrackingTimelineComponent
- BarcodeScanner (ML Kit)
- NotificationSettings

## Akzeptanzkriterien

- [ ] Trackingnummern können manuell eingegeben werden
- [ ] Barcode-Scanner erfasst Trackingnummern korrekt
- [ ] Mindestens 3 Versanddienstleister sind integriert
- [ ] Tracking-Updates erfolgen automatisch alle 4 Stunden
- [ ] Push-Benachrichtigungen werden bei Statusänderung gesendet
- [ ] Tracking-Historie zeigt alle Events chronologisch
- [ ] Fehlerhafte Trackingnummern werden erkannt und gemeldet
- [ ] Offline-Modus zeigt letzte bekannte Daten
- [ ] Nutzer kann Benachrichtigungen in Settings konfigurieren
- [ ] Unit Tests für API-Integration
- [ ] Integration Tests für WorkManager

## Abhängigkeiten

- E01 (Projekt-Setup)
- E02 (Bestellverwaltung) - Bestellungen müssen existieren

## Risiken

- API-Verfügbarkeit und -Stabilität der Versanddienstleister
- Rate Limits der APIs
- Kosten für API-Nutzung
- Komplexität der unterschiedlichen API-Formate
- Barcode-Erkennung funktioniert nicht auf allen Geräten

## Geschätzter Aufwand

**Story Points**: 21
**Dauer**: 2 Sprints

## Notes

- Eventuell Universal-Tracking-API verwenden statt einzelne Integrationen
- Barcode-Scanner benötigt Camera-Permissions
- FCM Setup erfordert Firebase-Projekt
