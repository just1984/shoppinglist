# Code Quality Tools - GiftTrack

## 📋 Übersicht

GiftTrack verwendet **ktlint** und **detekt** für automatisierte Code-Qualitätsprüfung und Einhaltung von Kotlin-Coding-Standards.

## 🛠️ Tools

### 1. ktlint

**ktlint** ist ein Code-Formatter und Linter für Kotlin, der die offiziellen Kotlin-Coding-Konventionen durchsetzt.

**Features:**
- ✅ Automatische Code-Formatierung
- ✅ Konsistenter Code-Stil im gesamten Projekt
- ✅ Reduziert Code-Review-Diskussionen über Stil-Präferenzen
- ✅ EditorConfig-Integration

**Version:** 12.1.0 (Plugin), 1.1.1 (ktlint-CLI)

### 2. detekt

**detekt** ist ein Static Code Analysis Tool für Kotlin, das Code-Smells und potenzielle Bugs erkennt.

**Features:**
- ✅ Erkennt über 200 Code-Smells
- ✅ Komplexitäts-Metriken
- ✅ Performance-Probleme identifizieren
- ✅ Konfigurierbares Regelset
- ✅ HTML/XML Reports

**Version:** 1.23.4

## 📁 Konfigurationsdateien

### .editorconfig

Definiert grundlegende Formatierungs-Regeln für verschiedene Dateitypen:

```
/home/user/shoppinglist/.editorconfig
```

**Wichtige Einstellungen:**
- **Kotlin-Dateien (*.kt, *.kts):**
  - Indent: 4 Spaces
  - Max Line Length: 120
  - Trailing Comma: Erlaubt
- **YAML/JSON:**
  - Indent: 2 Spaces
- **End of Line:** LF (Unix-Style)
- **Charset:** UTF-8

### detekt.yml

Detekt-Konfiguration mit aktivierten Regelsets:

```
/home/user/shoppinglist/config/detekt/detekt.yml
```

**Aktivierte Regelsets:**
- ✅ **complexity**: Komplexitäts-Metriken (Cyclomatic Complexity, Long Methods, etc.)
- ✅ **coroutines**: Coroutine-spezifische Regeln
- ✅ **empty-blocks**: Leere Code-Blöcke erkennen
- ✅ **exceptions**: Exception-Handling Best Practices
- ✅ **naming**: Naming Conventions
- ✅ **performance**: Performance-Optimierungen
- ✅ **potential-bugs**: Potenzielle Bugs erkennen
- ✅ **style**: Code-Stil Konventionen

**Wichtige Schwellenwerte:**
- Cyclomatic Complexity: 15
- Long Method: 60 Zeilen
- Long Parameter List: 6 Parameter (Funktionen), 7 (Konstruktoren)
- Large Class: 600 Zeilen
- Max Line Length: 120 Zeichen
- Return Count: 3

## 🚀 Verwendung

### Gradle Tasks

#### ktlint

```bash
# Alle Module prüfen
./gradlew ktlintCheckAll

# Alle Module formatieren
./gradlew ktlintFormatAll

# Nur ein Modul prüfen
./gradlew :app:ktlintCheck

# Nur ein Modul formatieren
./gradlew :app:ktlintFormat
```

#### detekt

```bash
# Alle Module analysieren
./gradlew detektAll

# Nur ein Modul analysieren
./gradlew :app:detekt
```

#### Kombiniert

```bash
# Alle Code Quality Checks ausführen (ktlint + detekt)
./gradlew codeQualityCheck
```

### Lokale Entwicklung

#### Vor jedem Commit

```bash
# Code formatieren und prüfen
./gradlew ktlintFormatAll codeQualityCheck
```

#### Nur Formatierung

```bash
# Schnelle Formatierung ohne Analyse
./gradlew ktlintFormatAll
```

#### Nur Analyse

```bash
# Nur detekt ohne Formatierung
./gradlew detektAll
```

## 📊 Reports

### ktlint Reports

**Ausgabe:** Console Output
**Format:** Text mit Farbcodes (Rot für Fehler)

**Beispiel:**
```
/app/src/main/java/com/gifttrack/app/MainActivity.kt:42:1:
Unexpected blank line(s) before "}"
```

**Fehler beheben:**
```bash
./gradlew ktlintFormat
```

### detekt Reports

**Speicherort:** `<module>/build/reports/detekt/`

**Verfügbare Formate:**
- **HTML:** `detekt.html` (empfohlen für manuelles Review)
- **XML:** `detekt.xml` (für CI/CD Integration)

**HTML Report öffnen:**
```bash
# Beispiel für app-Modul
open app/build/reports/detekt/detekt.html
```

**Beispiel Report-Inhalt:**
- Complexity-Metriken pro Datei
- Erkannte Issues gruppiert nach Severity
- Code-Smells mit Dateipfad und Zeilennummer
- Empfohlene Fixes

## 🔧 Konfiguration

### ktlint anpassen

**EditorConfig bearbeiten:**
```bash
# .editorconfig im Root-Verzeichnis
nano .editorconfig
```

**Wichtige ktlint-Eigenschaften:**
```ini
[*.kt]
max_line_length = 120
ij_kotlin_allow_trailing_comma = true
indent_size = 4
```

### detekt anpassen

**Detekt-Config bearbeiten:**
```bash
nano config/detekt/detekt.yml
```

**Regel deaktivieren:**
```yaml
style:
  MagicNumber:
    active: false  # MagicNumber-Regel deaktivieren
```

**Schwellenwert ändern:**
```yaml
complexity:
  LongMethod:
    threshold: 80  # Von 60 auf 80 erhöhen
```

**Dateien/Ordner ausschließen:**
```yaml
build:
  excludes:
    - '**/test/**'
    - '**/generated/**'
```

## 🎯 CI/CD Integration

### GitHub Actions Workflow

Code Quality wird automatisch bei jedem Push und Pull Request geprüft.

**Workflow:** `.github/workflows/ci.yml`

**Code Quality Job:**
```yaml
code-quality:
  runs-on: ubuntu-latest
  steps:
    - name: Run ktlint
      run: ./gradlew ktlintCheck

    - name: Run detekt
      run: ./gradlew detekt

    - name: Upload detekt reports
      uses: actions/upload-artifact@v3
      with:
        name: detekt-reports
        path: '**/build/reports/detekt/'
```

**Prüfungen:**
1. ✅ ktlint Check (Code-Formatierung)
2. ✅ detekt (Static Analysis)
3. ✅ Reports als Artifacts hochgeladen

**Bei Fehler:**
- Build schlägt fehl
- PR kann nicht gemerged werden
- Detekt-Reports verfügbar als Artifact

## 📝 Best Practices

### ✅ DO

1. **Vor jedem Commit:**
   ```bash
   ./gradlew ktlintFormat codeQualityCheck
   ```

2. **Code automatisch formatieren:**
   ```bash
   ./gradlew ktlintFormat
   ```

3. **Regelmäßig detekt Reports prüfen:**
   - HTML-Reports anschauen
   - Code-Smells beheben
   - Komplexität reduzieren

4. **EditorConfig in IDE einbinden:**
   - Android Studio: Automatisch unterstützt
   - IntelliJ IDEA: Plugin installieren

5. **Warnings ernst nehmen:**
   - Nicht alle Regeln müssen perfekt sein
   - Aber versuchen, Warnungen zu minimieren

### ❌ DON'T

1. **Keine ktlint-Fehler committen:**
   - Immer vor Commit formatieren
   - CI wird sonst fehlschlagen

2. **Detekt-Regeln nicht ohne Grund deaktivieren:**
   - Nur wenn wirklich notwendig
   - Begründung in Kommentar

3. **Reports nicht ignorieren:**
   - Regelmäßig detekt HTML-Reports prüfen
   - Code-Smells frühzeitig beheben

4. **Keine großen Code-Blöcke ohne Formatierung:**
   - Formatierung sollte kontinuierlich sein
   - Nicht erst am Ende

5. **Code Quality nicht überspringen:**
   - Kein `./gradlew build -x ktlintCheck`
   - Alle Checks laufen lassen

## 🔍 Häufige Fehler & Lösungen

### ktlint Fehler

#### "Unexpected indentation"
```kotlin
// ❌ Falsch
fun example() {
  return 42
}

// ✅ Richtig
fun example() {
    return 42
}
```
**Fix:** `./gradlew ktlintFormat`

#### "Missing trailing comma"
```kotlin
// ❌ Falsch
data class User(
    val name: String,
    val age: Int
)

// ✅ Richtig
data class User(
    val name: String,
    val age: Int,
)
```
**Fix:** `./gradlew ktlintFormat`

#### "Line too long"
```kotlin
// ❌ Falsch (>120 Zeichen)
val message = "This is a very long message that exceeds the maximum line length and should be split into multiple lines"

// ✅ Richtig
val message = "This is a very long message that exceeds " +
    "the maximum line length and should be split into multiple lines"
```

### detekt Warnings

#### "ComplexMethod"
```kotlin
// ❌ Zu komplex
fun processOrder(order: Order): Result {
    if (order.isValid()) {
        if (order.hasItems()) {
            if (order.isPaid()) {
                // ... viele weitere if-Statements
            }
        }
    }
}

// ✅ Besser
fun processOrder(order: Order): Result {
    if (!order.isValid()) return Result.Invalid
    if (!order.hasItems()) return Result.Empty
    if (!order.isPaid()) return Result.Unpaid
    // ... Hauptlogik
}
```

#### "LongParameterList"
```kotlin
// ❌ Zu viele Parameter
fun createUser(
    name: String,
    email: String,
    age: Int,
    address: String,
    phone: String,
    occupation: String,
    hobby: String
)

// ✅ Besser
data class UserData(
    val name: String,
    val email: String,
    val age: Int,
    val address: String,
    val phone: String,
    val occupation: String,
    val hobby: String,
)

fun createUser(userData: UserData)
```

#### "MagicNumber"
```kotlin
// ❌ Magic Numbers
fun calculateDiscount(price: Double): Double {
    return price * 0.15
}

// ✅ Konstanten verwenden
private const val DISCOUNT_RATE = 0.15

fun calculateDiscount(price: Double): Double {
    return price * DISCOUNT_RATE
}
```

## 🔗 IDE Integration

### Android Studio / IntelliJ IDEA

#### EditorConfig Plugin
**Installation:** Eingebaut (keine Installation nötig)

**Verwendung:**
- File → Settings → Editor → Code Style
- "Enable EditorConfig support" aktivieren
- Automatische Formatierung beim Speichern

#### ktlint Plugin
**Installation:**
1. File → Settings → Plugins
2. Marketplace durchsuchen
3. "ktlint" installieren

**Verwendung:**
- Automatische Formatierung beim Speichern
- Inline-Warnings im Editor

#### detekt Plugin
**Installation:**
1. File → Settings → Plugins
2. Marketplace durchsuchen
3. "detekt" installieren

**Verwendung:**
- Inline-Warnings im Editor
- Quick-Fixes für häufige Issues

### VS Code

#### EditorConfig Extension
```bash
code --install-extension EditorConfig.EditorConfig
```

#### Kotlin Extension
```bash
code --install-extension mathiasfrohlich.Kotlin
```

## 📚 Ressourcen

### Offizielle Dokumentation
- [ktlint Documentation](https://pinterest.github.io/ktlint/)
- [detekt Documentation](https://detekt.dev/)
- [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- [EditorConfig](https://editorconfig.org/)

### Gradle Plugins
- [ktlint-gradle Plugin](https://github.com/JLLeitschuh/ktlint-gradle)
- [detekt-gradle Plugin](https://detekt.dev/docs/gettingstarted/gradle/)

### Code Style Guides
- [Official Kotlin Style Guide](https://kotlinlang.org/docs/coding-conventions.html)
- [Android Kotlin Style Guide](https://developer.android.com/kotlin/style-guide)

## 🎨 Code Quality Metriken

### ktlint Success Kriterien
- ✅ **0 Formatierungs-Fehler** in allen Modulen
- ✅ **Konsistente Einrückung** (4 Spaces)
- ✅ **Max Line Length** eingehalten (120 Zeichen)
- ✅ **Trailing Commas** wo sinnvoll

### detekt Success Kriterien
- ✅ **Keine kritischen Issues** (Severity: Error)
- ✅ **Complexity:** Cyclomatic Complexity < 15
- ✅ **LongMethod:** Methoden < 60 Zeilen
- ✅ **LongParameterList:** < 6 Parameter
- ✅ **Keine Potential Bugs** erkannt

### Empfohlene Targets
- 🎯 **ktlint:** 0 Fehler (strikt)
- 🎯 **detekt:** < 10 Warnings pro Modul
- 🎯 **Code Coverage:** > 80% (zukünftig)
- 🎯 **Complexity Score:** A-B Rating

## 🚧 Zukünftige Erweiterungen

1. **Pre-Commit Hooks**
   - Git Hooks für automatisches ktlint Format
   - detekt Check vor jedem Commit
   - Husky oder pre-commit Framework

2. **SonarQube Integration**
   - Erweiterte Code Quality Metriken
   - Code Duplication Detection
   - Security Vulnerabilities

3. **Codecov Integration**
   - Code Coverage Tracking
   - Coverage Reports in PRs
   - Trend-Analyse

4. **Custom detekt Rules**
   - Projekt-spezifische Regeln
   - Business-Logic Validierung
   - Architektur-Compliance

---

**Erstellt**: 2025-11-23
**Status**: ✅ Produktionsbereit
**Version**: 1.0.0
