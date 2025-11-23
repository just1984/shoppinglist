# GiftTrack - Project Planning Documentation

Diese Ordnerstruktur enthält alle Epics, User Stories und den Projektfortschritt für die GiftTrack Android App.

## 📁 Struktur

```
claude/
├── README.md                          # Diese Datei
├── PROGRESS.md                        # Zentrale Progress-Tracking
├── epics/                             # Epic-Dokumente
│   ├── E01-PROJEKT-SETUP.md
│   ├── E02-BESTELLVERWALTUNG.md
│   ├── E03-EMPFAENGERVERWALTUNG.md
│   ├── E04-TRACKING-BENACHRICHTIGUNGEN.md
│   ├── E05-AUTHENTIFIZIERUNG-SICHERHEIT.md
│   ├── E06-CLOUD-SYNCHRONISATION.md
│   └── E07-ERWEITERTE-FEATURES.md
└── stories/                           # User Story-Dokumente
    ├── US-001-projekt-setup.md
    ├── US-002-clean-architecture-module.md
    ├── US-010-room-database-schema.md
    ├── US-012-bestellungsuebersicht-ui.md
    ├── US-030-empfaenger-schema.md
    └── ... (weitere Stories werden hinzugefügt)
```

## 📋 Epics Übersicht

| Epic ID | Name | Priority | Story Points | Status |
|---------|------|----------|--------------|--------|
| **E01** | Projekt-Setup & Infrastruktur | Critical | 21 | Not Started |
| **E02** | Bestellverwaltung | High | 39 | Not Started |
| **E03** | Empfängerverwaltung & Geschenkplanung | High | 30 | Not Started |
| **E04** | Tracking & Benachrichtigungen | Medium | 52 | Not Started |
| **E05** | Authentifizierung & Sicherheit | High | 50 | Not Started |
| **E06** | Cloud-Synchronisation | Medium | 62 | Not Started |
| **E07** | Erweiterte Features (Future) | Low | ~70 | Not Started |

**Gesamt**: ~324 Story Points

## 🎯 MVP Scope

Das **Minimum Viable Product (MVP)** umfasst folgende Epics:

- ✅ **E01**: Projekt-Setup & Infrastruktur
- ✅ **E02**: Bestellverwaltung
- ✅ **E03**: Empfängerverwaltung & Geschenkplanung
- ⚠️ **E04**: Tracking & Benachrichtigungen (reduziert auf 2-3 Carrier)
- ⚠️ **E05**: Authentifizierung & Sicherheit (optional für MVP)

**E06** (Cloud-Sync) und **E07** (Erweiterte Features) sind Post-MVP.

## 📖 Dokumentation

### Hauptdokumente (Root-Verzeichnis)
- **`docs/PRD.md`**: Product Requirements Document
- **`docs/TECHSTACK.md`**: Technischer Stack und Architektur

### Epics
Jedes Epic enthält:
- Beschreibung und Ziele
- Umfang (In Scope / Out of Scope)
- Liste aller zugehörigen User Stories
- Technische Details
- Akzeptanzkriterien
- Abhängigkeiten und Risiken
- Aufwandsschätzung

### User Stories
Jede User Story enthält:
- Epic-Zuordnung
- Priorität und Story Points
- User Story im Format "Als [Rolle] möchte ich [Funktion] damit [Nutzen]"
- Detaillierte Beschreibung
- Akzeptanzkriterien
- Technische Details mit Code-Beispielen
- Definition of Done
- Abhängigkeiten
- Test Cases

## 📊 Progress Tracking

**Zentrale Progress-Datei**: [`PROGRESS.md`](./PROGRESS.md)

Diese Datei enthält:
- Aktueller Sprint-Status
- Epic-Progress mit Story-Übersicht
- Story Points Metrics
- Velocity Tracking
- Milestones und Deadlines
- Known Issues & Blockers
- Sprint Planning

**Update-Frequenz**:
- Nach jeder Story-Completion
- Nach jedem Sprint
- Wöchentliche Reviews

## 🚀 User Story Naming Convention

```
US-XXX-kurzbeschreibung.md
```

- **US-001 bis US-009**: E01 - Projekt-Setup
- **US-010 bis US-029**: E02 - Bestellverwaltung
- **US-030 bis US-039**: E03 - Empfängerverwaltung
- **US-040 bis US-059**: E04 - Tracking & Benachrichtigungen
- **US-060 bis US-079**: E05 - Authentifizierung & Sicherheit
- **US-080 bis US-099**: E06 - Cloud-Synchronisation
- **US-100+**: E07 - Erweiterte Features

## 🔄 Workflow

### 1. Sprint Planning
1. Team wählt Stories aus Backlog (basierend auf Epic-Priorität)
2. Stories werden in Sprint übernommen
3. Story Points werden summiert
4. Sprint Goal wird definiert

### 2. Während des Sprints
1. Entwickler picken Story aus Sprint Backlog
2. Story wird in `PROGRESS.md` auf "In Progress" gesetzt
3. Entwicklung, Tests, Review
4. Story wird als "Completed" markiert

### 3. Sprint Review
1. Demo der completed Stories
2. Velocity berechnen
3. `PROGRESS.md` aktualisieren
4. Lessons Learned dokumentieren

### 4. Sprint Retrospective
1. Was lief gut?
2. Was kann verbessert werden?
3. Action Items für nächsten Sprint

## 📝 Wie neue Stories erstellt werden

1. **Epic identifizieren**: Welchem Epic gehört die Story an?
2. **Story ID vergeben**: Nächste freie Nummer im Epic-Range
3. **Story-Datei erstellen**: `stories/US-XXX-titel.md`
4. **Template verwenden**: Siehe bestehende Stories als Vorlage
5. **In Epic eintragen**: Story in entsprechendem Epic-Dokument listen
6. **In PROGRESS.md eintragen**: Story in Epic-Tabelle hinzufügen

## 🎓 Best Practices

### Story Writing
- **Atomic**: Jede Story sollte in 1-2 Tagen abschließbar sein
- **Testable**: Klare Akzeptanzkriterien definieren
- **Specific**: Technische Details mit Code-Beispielen
- **Independent**: Minimale Abhängigkeiten zu anderen Stories

### Epic Organization
- **Cohesive**: Alle Stories eines Epics gehören zusammen
- **Deliverable**: Epic sollte eigenständig nutzbare Funktionalität liefern
- **Sized**: Epic sollte in 1-3 Sprints abschließbar sein

### Progress Tracking
- **Daily**: Story-Status Updates
- **Weekly**: PROGRESS.md Review
- **Sprint**: Vollständige Metriken-Update

## 🔗 Verwandte Dokumente

- [Product Requirements Document (PRD)](../docs/PRD.md)
- [Tech Stack Documentation](../docs/TECHSTACK.md)
- [Project Progress Tracker](./PROGRESS.md)

## 📞 Kontakt

*To be filled when team is assembled*

---

**Erstellt**: 2025-11-23
**Letzte Aktualisierung**: 2025-11-23
**Version**: 1.0.0
