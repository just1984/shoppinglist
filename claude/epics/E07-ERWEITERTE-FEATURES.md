# E07 - Erweiterte Features (Future Releases)

**Epic ID**: E07
**Status**: Not Started
**Priority**: Low
**Sprint**: Future

## Beschreibung

Erweiterte Funktionen für zukünftige Releases, die über den MVP hinausgehen und zusätzlichen Mehrwert für Nutzer bieten.

## Ziele

- App-Funktionalität erweitern
- Premium-Features für Monetarisierung
- Nutzerbindung erhöhen
- Differenzierung von Wettbewerbern

## Umfang

### In Scope
- Automatischer E-Mail-Import
- Budget- und Ausgabenmanagement
- PDF/CSV Export
- Mehrsprachigkeit (DE, EN)
- Social Sharing
- Widgets für Home Screen
- Statistiken und Analytics
- Ende-zu-Ende-Verschlüsselung

### Out of Scope
- Web-Version
- Desktop-App
- Integration mit Kalender-Apps

## User Stories

### E-Mail-Import
- US-100: E-Mail-Zugriff mit OAuth (Gmail, Outlook)
- US-101: Parser für Amazon-Bestellbestätigungen
- US-102: Parser für eBay-Bestellbestätigungen
- US-103: Parser für allgemeine Shop-E-Mails
- US-104: Automatische Erkennung von Bestellungen
- US-105: Einwilligungsmanagement für E-Mail-Zugriff

### Budget-Management
- US-110: Budget pro Empfänger festlegen
- US-111: Ausgaben tracken
- US-112: Budget-Überschreitungs-Warnung
- US-113: Ausgaben-Statistiken
- US-114: Monatliche/jährliche Ausgaben-Übersicht

### Export-Funktionen
- US-120: PDF-Export für Geschenklisten
- US-121: CSV-Export für Bestellungen
- US-122: Sharing via Email/Messaging-Apps
- US-123: Druckansicht für Geschenklisten

### Mehrsprachigkeit
- US-130: Internationalisierung (i18n) Setup
- US-131: Deutsche Übersetzung
- US-132: Englische Übersetzung
- US-133: Sprachauswahl in Settings
- US-134: Automatische Sprache basierend auf System

### Social Features
- US-140: Geschenklisten teilen (Read-only Link)
- US-141: Gemeinsame Geschenklisten (Kollaboration)
- US-142: Wunschlisten-Feature

### Widgets
- US-150: Home Screen Widget für nächste Lieferungen
- US-151: Widget für offene Geschenke
- US-152: Widget-Konfiguration

### Statistiken & Analytics
- US-160: Dashboard mit Statistiken
- US-161: Ausgaben nach Monat/Jahr
- US-162: Meistgenutzte Shops
- US-163: Geschenke nach Empfänger
- US-164: Lieferzeiten-Analyse

### Ende-zu-Ende-Verschlüsselung
- US-170: E2EE für Cloud-Sync
- US-171: Verschlüsselungs-Key Management
- US-172: Zero-Knowledge-Architektur

## Technische Details

### E-Mail-Import
- Gmail API / Outlook Graph API
- NLP für E-Mail-Parsing
- Background Service für periodischen Import

### Budget-Management
```kotlin
@Entity(tableName = "budgets")
data class Budget(
    @PrimaryKey val id: String,
    val recipientId: String,
    val amount: Double,
    val currency: String,
    val period: BudgetPeriod, // MONTHLY, YEARLY, ONE_TIME
    val year: Int,
    val month: Int?
)
```

### Export
- iText Library für PDF
- Apache Commons CSV
- Android Share Intent

### i18n
- Android Resources (strings.xml)
- Locale-basierte String-Auswahl

### E2EE
- libsodium / Tink Library
- Client-side Encryption
- Key Derivation von User-Password

## Akzeptanzkriterien

### E-Mail-Import
- [ ] Nutzer kann Gmail/Outlook verbinden
- [ ] Bestellungen werden automatisch erkannt
- [ ] Mindestens 3 Shop-Formate werden unterstützt
- [ ] Nutzer kann Import aktivieren/deaktivieren

### Budget
- [ ] Budget pro Empfänger kann gesetzt werden
- [ ] Warnung bei Überschreitung
- [ ] Statistiken zeigen Ausgaben korrekt

### Export
- [ ] PDF-Export generiert schöne Geschenklisten
- [ ] CSV enthält alle relevanten Daten
- [ ] Sharing funktioniert mit allen Apps

### Mehrsprachigkeit
- [ ] App ist vollständig auf Deutsch
- [ ] App ist vollständig auf Englisch
- [ ] Sprachwechsel funktioniert ohne Neustart

### Widgets
- [ ] Widget zeigt aktuelle Daten
- [ ] Widget aktualisiert sich automatisch
- [ ] Widget ist konfigurierbar

### E2EE
- [ ] Daten sind auf Server verschlüsselt
- [ ] Server kann Daten nicht lesen
- [ ] Key-Wiederherstellung funktioniert

## Abhängigkeiten

- E01, E02, E03, E04, E05, E06 müssen abgeschlossen sein
- Premium-Subscription System (für Budget/Export)

## Risiken

- Komplexität des E-Mail-Parsings
- E-Mail-API-Kosten
- E2EE erhöht Komplexität deutlich
- Key-Verlust = Datenverlust bei E2EE

## Geschätzter Aufwand

**Story Points**: 55+
**Dauer**: 5+ Sprints (gestaffelt)

## Priorisierung für Releases

### Release 2.0
- Budget-Management (Premium)
- PDF/CSV Export (Premium)
- Mehrsprachigkeit

### Release 3.0
- E-Mail-Import
- Widgets
- Statistiken

### Release 4.0
- Social Features
- E2EE
- Erweiterte Analytics

## Monetarisierung

**Premium Features**:
- Budget-Management
- Export-Funktionen
- E-Mail-Import
- Erweiterte Statistiken

**Free Features**:
- Mehrsprachigkeit
- Basic Widgets
- Basis-Statistiken

## Notes

Diese Features sollten nach MVP und basierend auf Nutzerfeedback priorisiert werden.
