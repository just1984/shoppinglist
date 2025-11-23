# E05 - Authentifizierung & Sicherheit

**Epic ID**: E05
**Status**: Not Started
**Priority**: High
**Sprint**: Sprint 3

## Beschreibung

Implementierung eines sicheren Authentifizierungssystems mit Supabase und lokaler Datenverschlüsselung unter Einhaltung der DSGVO-Richtlinien.

## Ziele

- Sichere Benutzerauthentifizierung
- Biometrische Authentifizierung (Fingerprint, Face)
- Verschlüsselte lokale Datenspeicherung
- DSGVO-konforme Datenhaltung
- Nutzerkontrolle über Daten

## Umfang

### In Scope
- Supabase Auth Integration
- Email/Password Authentication
- OAuth (Google, Apple)
- Biometrische Authentifizierung
- Verschlüsselte Room Database (SQLCipher)
- EncryptedSharedPreferences für sensitive Daten
- Session Management
- Auto-Lock nach Inaktivität
- DSGVO-konforme Datenverarbeitung
- Datenschutzerklärung
- Einwilligungsmanagement

### Out of Scope
- Multi-Faktor-Authentifizierung (MFA) - Future
- Single Sign-On (SSO) - Future

## User Stories

- US-060: Supabase Projekt einrichten
- US-061: Supabase Auth SDK integrieren
- US-062: Registrierung mit Email/Password
- US-063: Login mit Email/Password
- US-064: Passwort zurücksetzen
- US-065: OAuth mit Google implementieren
- US-066: OAuth mit Apple implementieren (falls iOS später)
- US-067: Biometrische Authentifizierung einrichten
- US-068: SQLCipher für verschlüsselte Datenbank
- US-069: EncryptedSharedPreferences implementieren
- US-070: Session Management (Auto-Logout)
- US-071: Auto-Lock nach Inaktivität
- US-072: Datenschutzerklärung UI
- US-073: Einwilligungsmanagement (Consent)
- US-074: Account löschen (DSGVO Right to be Forgotten)
- US-075: Datenexport (DSGVO Right to Data Portability)
- US-076: Security Settings Screen

## Technische Details

### Supabase Auth
```kotlin
// Supabase Client Setup
val supabase = createSupabaseClient(
    supabaseUrl = BuildConfig.SUPABASE_URL,
    supabaseKey = BuildConfig.SUPABASE_ANON_KEY
) {
    install(Auth)
    install(Postgrest)
}

// Login
supabase.auth.signInWith(Email) {
    email = "user@example.com"
    password = "password"
}
```

### Biometric Authentication
```kotlin
val biometricPrompt = BiometricPrompt(
    activity,
    executor,
    object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            // Grant access
        }
    }
)
```

### Encrypted Database
```kotlin
// SQLCipher Integration
val passphrase = // Derived from biometric or user password
Room.databaseBuilder(context, AppDatabase::class.java, "gifttrack.db")
    .openHelperFactory(SupportFactory(passphrase))
    .build()
```

### Security Features
- HTTPS only
- Certificate Pinning (optional)
- ProGuard/R8 Obfuscation
- Root Detection (optional)

## Akzeptanzkriterien

- [ ] Nutzer können sich mit Email/Password registrieren
- [ ] Nutzer können sich mit Email/Password anmelden
- [ ] Passwort-Reset funktioniert per Email
- [ ] OAuth mit Google funktioniert
- [ ] Biometrische Authentifizierung funktioniert (wenn Hardware vorhanden)
- [ ] Lokale Datenbank ist verschlüsselt
- [ ] Sensitive Daten in EncryptedSharedPreferences
- [ ] Auto-Lock nach 5 Minuten Inaktivität
- [ ] Datenschutzerklärung ist sichtbar und akzeptierbar
- [ ] Nutzer kann Einwilligungen verwalten
- [ ] Nutzer kann Account löschen (alle Daten werden gelöscht)
- [ ] Nutzer kann Daten exportieren (JSON/CSV)
- [ ] HTTPS für alle Netzwerkverbindungen
- [ ] Security Unit Tests
- [ ] Penetration Testing Bericht

## Abhängigkeiten

- E01 (Projekt-Setup)

## Risiken

- Biometrische Auth nicht auf allen Geräten verfügbar
- SQLCipher Performance-Overhead
- DSGVO-Compliance erfordert juristisches Review
- Supabase-Verfügbarkeit

## Geschätzter Aufwand

**Story Points**: 13
**Dauer**: 1 Sprint

## Notes

- Datenschutzerklärung muss von Juristen geprüft werden
- Supabase Row Level Security (RLS) für zusätzliche Sicherheit
- Biometrische Daten niemals auf Server hochladen
