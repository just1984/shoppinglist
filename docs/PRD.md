# Product Requirements Document (PRD)

## Produktname

GiftTrack – Eine App zur Geschenkplanung und Einkaufsübersicht

## Vision

Eine App, die Endkunden ermöglicht, alle ihre Einkäufe aus verschiedenen Onlineshops an einem Ort zu strukturieren, mit Fokus auf Geschenkplanung durch Empfängerzuordnung und strenge Datenschutzrichtlinien.

## Zielgruppe

- Vielbesteller, die in mehreren Online-Shops einkaufen
- Nutzer, die mehrere Geschenke für verschiedene Empfänger bestellen (besonders Weihnachtszeit)
- Datenschutzbewusste Nutzer, die keine unkontrollierte Weitergabe ihrer Einkaufsdaten wünschen


## Kernfunktionen (MVP)

### 1. Multi-Shop Einkaufsübersicht

Der Nutzer kann alle Einkäufe aus verschiedenen Shops (Amazon, eBay, Otto, Zalando etc.) zentral sehen.

- Manuelle und automatische Importfunktionen (z.B. Import via Trackingnummern, optional Mail-Parsing)
- Übersicht mit Produktbild, Bestelldatum, Shop und Status


### 2. Geschenkplanung mit Empfänger-Logik

Der Nutzer kann jedem Kauf einen Empfänger zuweisen und so einfach die Geschenkplanung verfolgen.

- Empfängerverwaltung mit Namenslisten
- Filter und Sortierung nach Empfängern und Status
- Markierung „Geschenk erledigt“ oder „offen“


### 3. Sendestatus-Tracking und Benachrichtigungen

Automatisches Tracking des Sendestatus mit Push-Benachrichtigungen bei Statusänderungen.

- Unterstützung für verschiedene Versanddienstleister
- Anzeige von Lieferzeiten und Verzögerungen


### 4. Datenschutz \& Sicherheit

- Lokale Speicherung der Einkaufsdaten auf dem Gerät oder verschlüsselte Cloud-Synchronisierung
- Keine Weitergabe von Zugangsdaten an Dritte
- DSGVO-konformität und klare Nutzerkontrolle über Datenfreigaben


## Erweiterte Funktionen (Future Releases)

- Automatisches Auslesen von Bestellungen aus E-Mail-Postfächern (nur nach Zustimmung)
- Budget- und Ausgabenmanagement pro Empfänger
- Exportmöglichkeit der Geschenk- und Bestellübersichten (PDF, CSV)
- Mehrsprachigkeit (mindestens Deutsch, Englisch)
- Social-Sharing der Geschenklisten (optional)


## Technische Rahmenbedingungen

- Mobile App: Native Entwicklung mit Swift (iOS) – optionale spätere Android-Version
- Backend: Supabase für Authentifizierung, Datenmanagement und Synchronisation
- Datenschutz- und Sicherheitsstandards nach europäischem Recht
- Offline-Verfügbarkeit der wichtigsten Funktionen


## KPIs für Erfolgsmessung

- Anzahl verknüpfter Shops und Bestellungen pro Nutzer
- Nutzeraktivität und Retention speziell im Weihnachtszeitraum
- Anzahl eingetragener Empfänger und Geschenklisten
- Nutzerfeedback zu Datenschutz und Benutzerfreundlichkeit


## Monetarisierung

- Kostenfreie Basisversion mit allen Kernfunktionen
- Optionales Pro-Upgrade für Budget-Tools und Exportfunktionen
- Keine Werbung, klare Trennung von kostenfreien und Premiumfunktionen


## Risiken und Herausforderungen

- Komplexität bei der Integration vieler Shops und Versanddienstleister
- Datenschutzkonforme Umsetzung bei automatischem Import und Tracking
- Nutzerakzeptanz bzgl. der notwendigen Zugriffsrechte (z.B. auf E-Mails)
- Konkurrenz durch etablierte Pakettracking-Apps ohne Geschenkplanung
