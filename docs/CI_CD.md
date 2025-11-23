# CI/CD Pipeline - GiftTrack

## 📋 Übersicht

GiftTrack verwendet **GitHub Actions** für Continuous Integration und Continuous Deployment. Die Pipeline automatisiert Build, Tests, Linting und Releases.

## 🏗️ Workflows

### 1. CI Workflow (`ci.yml`)

**Trigger:**
- Push zu `main`, `develop`, `feature/**`, `claude/**`
- Pull Requests zu `main`, `develop`

**Jobs:**

#### Build & Test Job
```yaml
- Checkout Code
- Setup JDK 17
- Cache Gradle
- Build mit Gradle
- Unit Tests ausführen
- Upload Build Reports (bei Fehler)
- Upload Debug APK
```

**Laufzeit:** ~5-10 Minuten

#### Lint Job
```yaml
- Checkout Code
- Setup JDK 17
- Android Lint ausführen
- Upload Lint Reports
```

**Laufzeit:** ~3-5 Minuten

#### Code Quality Job
```yaml
- Checkout Code
- Setup JDK 17
- ktlint Check (Code Style)
- detekt (Static Analysis)
- Upload Reports
```

**Laufzeit:** ~3-5 Minuten

**Parallelisierung:** Alle 3 Jobs laufen parallel

### 2. PR Check Workflow (`pr-check.yml`)

**Trigger:**
- Pull Request opened/synchronize/reopened

**Jobs:**

#### PR Validation
```yaml
- Checkout Code mit Full History
- Gradle Wrapper Validation
- Build all modules (assembleDebug)
- Run all tests
- Run lint
- Check PR title format (Conventional Commits)
- Comment PR mit Build-Status
```

**Features:**
- ✅ Validiert Gradle Wrapper Security
- ✅ Überprüft PR-Titel Format (feat/fix/docs/etc.)
- ✅ Kommentiert PR automatisch mit Ergebnissen
- ✅ Blockiert Merge bei Fehlern

**Laufzeit:** ~10-15 Minuten

### 3. Release Workflow (`release.yml`)

**Trigger:**
- Tag Push: `v*.*.*` (z.B. v1.0.0)
- Manueller Workflow Dispatch

**Jobs:**

#### Build Release APK
```yaml
- Checkout Code
- Setup JDK 17
- Run Tests
- Build Release APK
- Sign APK (TODO: Keystore konfigurieren)
- Create GitHub Release
- Upload APK als Artifact
```

**Features:**
- ✅ Erstellt GitHub Release
- ✅ Hängt APK an Release an
- ✅ Generiert Checksums
- ✅ 90 Tage Artifact Retention
- ⏳ APK Signing (wenn Keystore konfiguriert)

**Laufzeit:** ~15-20 Minuten

## 🎯 Workflow-Strategie

### Branching Strategy

```
main (protected)
  ↑
develop
  ↑
feature/*, claude/*
```

**Regeln:**
- `main`: Nur via PR, alle Checks müssen grün sein
- `develop`: Aktive Entwicklung
- `feature/*`: Feature Branches
- `claude/*`: Claude-generierte Branches

### CI Checks

**Bei jedem Push:**
1. ✅ Build kompiliert
2. ✅ Unit Tests bestehen
3. ✅ Lint Checks bestehen
4. ✅ Code Quality (ktlint/detekt)

**Bei Pull Requests zusätzlich:**
5. ✅ Gradle Wrapper Validierung
6. ✅ PR Titel Format
7. ✅ Automatischer Kommentar

## 📦 Artifacts

### Debug APK
**Workflow:** CI
**Speicherdauer:** 90 Tage
**Pfad:** `app/build/outputs/apk/debug/*.apk`

### Release APK
**Workflow:** Release
**Speicherdauer:** 90 Tage
**Pfad:** `app/build/outputs/apk/release/*.apk`

### Build Reports
**Bei Fehler hochgeladen:**
- Test Results: `**/build/test-results/`
- Build Reports: `**/build/reports/`
- Lint Reports: `**/build/reports/lint-results*.html`

## 🚀 Release Process

### Automatisches Release

1. **Tag erstellen:**
```bash
git tag v1.0.0
git push origin v1.0.0
```

2. **Workflow startet automatisch**
   - Führt alle Tests aus
   - Baut Release APK
   - Erstellt GitHub Release
   - Hängt APK an

3. **Release ist live!**

### Manuelles Release

1. **GitHub Actions → Release Build → Run workflow**
2. **Version eingeben** (z.B. 1.0.0)
3. **Workflow läuft**
4. **APK als Artifact verfügbar**

## 🔐 Secrets & Security

### Benötigte Secrets

```yaml
# GitHub Token (automatisch verfügbar)
GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}

# TODO: Für APK Signing
KEYSTORE_FILE: base64 encoded keystore
KEYSTORE_PASSWORD: ***
KEY_ALIAS: ***
KEY_PASSWORD: ***
```

### Hinzufügen von Secrets

```
GitHub Repo → Settings → Secrets and variables → Actions → New secret
```

## ⚡ Performance

### Caching Strategy

**Gradle Cache:**
```yaml
~/.gradle/caches
~/.gradle/wrapper
```

**Cache Key:**
```
${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle*', '**/gradle-wrapper.properties') }}
```

**Effekt:**
- Ohne Cache: ~10 Minuten Build
- Mit Cache: ~3-5 Minuten Build

### Build Optimierung

```kotlin
// gradle.properties
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.configuration-cache=true
```

## 🧪 Lokales Testing

### CI Workflow lokal testen

**Mit act (GitHub Actions local runner):**

```bash
# Install act
brew install act  # macOS
# oder
curl https://raw.githubusercontent.com/nektos/act/master/install.sh | sudo bash

# Run CI workflow
act push

# Run specific job
act -j build

# Run PR workflow
act pull_request
```

### Gradle Tasks manuell

```bash
# Alles was CI ausführt
./gradlew build test lint

# Nur Build
./gradlew assembleDebug

# Nur Tests
./gradlew test

# Nur Lint
./gradlew lint

# Release Build
./gradlew assembleRelease
```

## 📊 Status Badges

### In README.md

```markdown
[![CI](https://github.com/just1984/shoppinglist/actions/workflows/ci.yml/badge.svg)](https://github.com/just1984/shoppinglist/actions/workflows/ci.yml)
[![PR Check](https://github.com/just1984/shoppinglist/actions/workflows/pr-check.yml/badge.svg)](https://github.com/just1984/shoppinglist/actions/workflows/pr-check.yml)
```

**Badge Status:**
- ✅ Grün: Alle Checks bestanden
- ❌ Rot: Checks fehlgeschlagen
- 🟡 Gelb: Workflow läuft

## 🔍 Debugging

### Workflow Logs

```
GitHub → Actions → Workflow Run → Job → Step
```

### Häufige Fehler

**Build Failed:**
```
→ Lokaler Build: ./gradlew build
→ Logs prüfen
→ Dependencies aktualisieren
```

**Tests Failed:**
```
→ Lokale Tests: ./gradlew test
→ Test Reports: build/reports/tests/
```

**Lint Failed:**
```
→ Lokaler Lint: ./gradlew lint
→ Lint Report: build/reports/lint-results.html
```

**Gradle Wrapper Invalid:**
```
→ Neu generieren: gradle wrapper
→ Wrapper committen
```

## 📝 Best Practices

### ✅ DO

- **Kleine, fokussierte Commits**
- **Aussagekräftige Commit Messages**
- **PR Title nach Conventional Commits**
- **Lokale Tests vor Push**
- **Branch aktuell halten**
- **Artifacts nach Download löschen**

### ❌ DON'T

- **Direkt zu main pushen**
- **Checks überspringen**
- **Secrets in Code committen**
- **Große Binärdateien committen**
- **Breaking Changes ohne Tests**
- **Force Push zu shared branches**

## 🎨 Conventional Commits

**Format:**
```
<type>(<scope>): <subject>

<body>

<footer>
```

**Types:**
- `feat`: Neue Features
- `fix`: Bug Fixes
- `docs`: Dokumentation
- `style`: Code-Formatierung
- `refactor`: Code-Umstrukturierung
- `perf`: Performance-Verbesserungen
- `test`: Tests
- `build`: Build-System
- `ci`: CI/CD
- `chore`: Sonstiges

**Beispiele:**
```
feat(orders): add order list screen
fix(database): resolve crash on empty orders
docs(readme): update setup instructions
ci(workflow): add code coverage reporting
```

## 🚧 Zukünftige Erweiterungen

### Geplante Features

1. **Code Coverage**
   - JaCoCo Integration
   - Coverage Reports
   - Codecov.io Integration

2. **UI Tests**
   - Instrumentation Tests
   - Screenshot Tests
   - Firebase Test Lab

3. **Dependency Scanning**
   - Dependabot
   - OWASP Dependency Check
   - License Scanning

4. **App Distribution**
   - Firebase App Distribution
   - Google Play Internal Testing
   - Beta Track Deployment

5. **Performance Monitoring**
   - Build Time Tracking
   - APK Size Monitoring
   - Benchmark Tests

## 📚 Ressourcen

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Gradle Build Action](https://github.com/gradle/gradle-build-action)
- [Conventional Commits](https://www.conventionalcommits.org/)
- [act - Local GitHub Actions](https://github.com/nektos/act)

---

**Erstellt**: 2025-11-23
**Status**: ✅ Produktionsbereit
**Version**: 1.0.0
