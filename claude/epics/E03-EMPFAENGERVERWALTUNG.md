# E03 - Empfängerverwaltung & Geschenkplanung

**Epic ID**: E03
**Status**: Not Started
**Priority**: High
**Sprint**: Sprint 4

## Beschreibung

Implementierung der Empfängerverwaltung und Zuordnung von Bestellungen/Produkten zu Empfängern für die Geschenkplanung.

## Ziele

- Nutzer können Empfänger anlegen und verwalten
- Produkte/Bestellungen können Empfängern zugeordnet werden
- Übersicht nach Empfängern filtern
- Geschenkstatus tracken (offen/erledigt)
- Geschenklisten pro Empfänger anzeigen

## Umfang

### In Scope
- Room Database für Empfänger
- CRUD Operationen für Empfänger
- Zuordnung Produkt → Empfänger
- UI für Empfängerverwaltung
- UI für Geschenkübersicht pro Empfänger
- Filter in Bestellübersicht nach Empfänger
- Geschenkstatus (offen/erledigt)
- Statistiken pro Empfänger (Anzahl Geschenke)

### Out of Scope
- Budget-Tracking (E07)
- Ausgabenmanagement (E07)
- Social Sharing (E07)

## User Stories

- US-030: Empfänger-Datenbankschema erstellen
- US-031: Empfänger hinzufügen/bearbeiten/löschen
- US-032: Empfängerliste anzeigen
- US-033: Produkt einem Empfänger zuordnen
- US-034: Geschenkübersicht nach Empfänger filtern
- US-035: Geschenkstatus setzen (offen/erledigt)
- US-036: Geschenkliste pro Empfänger anzeigen
- US-037: Empfänger-Detailansicht mit allen Geschenken
- US-038: Schnellzuordnung beim Bestellung erstellen
- US-039: Empfänger-Avatars/Bilder speichern

## Technische Details

### Database Schema

```kotlin
@Entity(tableName = "recipients")
data class Recipient(
    @PrimaryKey val id: String,
    val name: String,
    val avatarUrl: String?,
    val notes: String?,
    val birthday: LocalDate?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

@Entity(
    tableName = "gift_assignments",
    foreignKeys = [
        ForeignKey(entity = Product::class, parentColumns = ["id"], childColumns = ["productId"]),
        ForeignKey(entity = Recipient::class, parentColumns = ["id"], childColumns = ["recipientId"])
    ]
)
data class GiftAssignment(
    @PrimaryKey val id: String,
    val productId: String,
    val recipientId: String,
    val status: GiftStatus, // OPEN, COMPLETED
    val assignedAt: LocalDateTime
)

enum class GiftStatus {
    OPEN,
    COMPLETED
}
```

### UI Screens
- RecipientsListScreen
- RecipientDetailScreen (mit allen Geschenken)
- AddEditRecipientScreen
- GiftAssignmentSheet (Bottom Sheet für Zuordnung)

## Akzeptanzkriterien

- [ ] Empfänger können angelegt, bearbeitet und gelöscht werden
- [ ] Produkte können einem oder mehreren Empfängern zugeordnet werden
- [ ] Geschenkstatus kann gesetzt werden
- [ ] Bestellübersicht kann nach Empfänger gefiltert werden
- [ ] Geschenkliste pro Empfänger zeigt alle zugeordneten Produkte
- [ ] Statistik zeigt Anzahl offener/erledigter Geschenke
- [ ] UI ist intuitiv und benutzerfreundlich
- [ ] Beim Löschen eines Empfängers werden Zuordnungen entfernt
- [ ] Unit Tests für Repository und Use Cases
- [ ] UI Tests für kritische Flows

## Abhängigkeiten

- E01 (Projekt-Setup)
- E02 (Bestellverwaltung) - Produkte müssen existieren

## Risiken

- Komplexität der Zuordnungslogik (1:n Beziehung)
- UX muss intuitiv sein für schnelle Zuordnung

## Geschätzter Aufwand

**Story Points**: 13
**Dauer**: 1 Sprint

## Notes

Diese Funktion ist das Alleinstellungsmerkmal der App gegenüber reinen Tracking-Apps.
