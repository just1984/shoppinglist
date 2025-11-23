# E01 - Projekt-Setup & Infrastruktur

**Epic ID**: E01
**Status**: Not Started
**Priority**: Critical
**Sprint**: Sprint 1

## Beschreibung

Einrichtung der grundlegenden Projektstruktur, Build-Konfiguration und Entwicklungsumgebung für die GiftTrack Android App.

## Ziele

- Funktionsfähiges Android-Projekt mit Kotlin und Jetpack Compose
- Clean Architecture mit modularer Struktur
- CI/CD Pipeline eingerichtet
- Entwicklungsumgebung für alle Teammitglieder nutzbar

## Umfang

### In Scope
- Android Studio Projekt-Setup
- Gradle Konfiguration mit Version Catalog
- Modularisierung (app, feature, core Module)
- Dependency Injection mit Hilt
- Basic Navigation Setup
- CI/CD mit GitHub Actions
- Code Quality Tools (Detekt, ktlint)
- Testing Framework Setup

### Out of Scope
- Business Logic Implementierung
- UI Design Implementation
- Backend Integration

## User Stories

- US-001: Android Projekt mit Kotlin & Compose erstellen
- US-002: Clean Architecture Module einrichten
- US-003: Dependency Injection mit Hilt konfigurieren
- US-004: Navigation Setup implementieren
- US-005: CI/CD Pipeline einrichten
- US-006: Code Quality Tools integrieren

## Technische Details

### Module Struktur
```
app/
├── feature/
│   ├── orders/
│   ├── recipients/
│   ├── tracking/
│   └── settings/
├── core/
│   ├── ui/
│   ├── data/
│   ├── domain/
│   ├── common/
│   └── database/
```

### Technologien
- Kotlin 1.9+
- Gradle Kotlin DSL
- Jetpack Compose
- Hilt
- Compose Navigation
- GitHub Actions

## Akzeptanzkriterien

- [ ] Projekt kompiliert ohne Fehler
- [ ] Alle Module sind korrekt konfiguriert
- [ ] Hilt Dependency Injection funktioniert
- [ ] Basic Navigation zwischen Screens möglich
- [ ] CI Pipeline führt Tests und Builds durch
- [ ] Code Quality Checks laufen automatisch
- [ ] README mit Setup-Anleitung vorhanden

## Abhängigkeiten

Keine - Startpunkt des Projekts

## Risiken

- Komplexität der Modularisierung könnte Verzögerungen verursachen
- Team muss sich mit Compose und Hilt vertraut machen

## Geschätzter Aufwand

**Story Points**: 13
**Dauer**: 1-2 Sprints

## Notes

Dieses Epic bildet die Grundlage für alle weiteren Entwicklungen. Sorgfältige Planung hier spart später Zeit.
