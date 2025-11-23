# E06 - Cloud-Synchronisation

**Epic ID**: E06
**Status**: Not Started
**Priority**: Medium
**Sprint**: Sprint 7

## Beschreibung

Implementierung der optionalen Cloud-Synchronisation mit Supabase für geräteübergreifenden Zugriff auf Bestellungen und Geschenklisten.

## Ziele

- Optionale Cloud-Sync (Opt-in)
- Geräteübergreifende Datensynchronisation
- Konfliktauflösung bei gleichzeitigen Änderungen
- Offline-First mit automatischer Sync bei Verbindung
- Ende-zu-Ende-Verschlüsselung (optional)

## Umfang

### In Scope
- Supabase PostgreSQL Schema
- Supabase Realtime Subscriptions
- Sync-Engine (bidirektional)
- Konfliktauflösung (Last-Write-Wins)
- Sync-Status-Anzeige
- Manuelle Sync-Trigger
- Auto-Sync bei Netzwerkverbindung
- Opt-in/Opt-out für Cloud-Sync
- Sync-Settings

### Out of Scope
- Ende-zu-Ende-Verschlüsselung (E07 - Future)
- Selective Sync (alle oder keine Daten)
- Versionierung/Historie der Änderungen

## User Stories

- US-080: Supabase PostgreSQL Schema erstellen
- US-081: Row Level Security (RLS) Policies definieren
- US-082: Sync-Engine implementieren (Local → Cloud)
- US-083: Sync-Engine implementieren (Cloud → Local)
- US-084: Konfliktauflösung implementieren
- US-085: Realtime Subscriptions für Live-Updates
- US-086: Sync-Status UI (Indikator, Fortschritt)
- US-087: Manuelle Sync-Button in Settings
- US-088: Auto-Sync bei Netzwerkverbindung
- US-089: Opt-in Flow für Cloud-Sync
- US-090: Opt-out und Daten löschen
- US-091: Sync-Fehlerbehandlung und Retry
- US-092: Sync-Logs für Debugging
- US-093: Sync-Performance-Optimierung (Delta-Sync)
- US-094: Initial-Sync bei erstem Setup

## Technische Details

### Supabase Schema

```sql
-- Orders Table
CREATE TABLE public.orders (
    id UUID PRIMARY KEY,
    user_id UUID REFERENCES auth.users NOT NULL,
    order_number TEXT,
    shop_id UUID,
    order_date TIMESTAMPTZ,
    status TEXT,
    total_amount NUMERIC,
    currency TEXT,
    notes TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    deleted_at TIMESTAMPTZ
);

-- Row Level Security
ALTER TABLE public.orders ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Users can only see their own orders"
    ON public.orders FOR SELECT
    USING (auth.uid() = user_id);

CREATE POLICY "Users can insert their own orders"
    ON public.orders FOR INSERT
    WITH CHECK (auth.uid() = user_id);
```

### Sync Engine Strategy

```kotlin
class SyncEngine(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository
) {
    suspend fun sync() {
        // 1. Push local changes to cloud
        val localChanges = localRepository.getUnsyncedChanges()
        remoteRepository.pushChanges(localChanges)

        // 2. Pull remote changes
        val lastSyncTimestamp = localRepository.getLastSyncTimestamp()
        val remoteChanges = remoteRepository.getChangesSince(lastSyncTimestamp)

        // 3. Resolve conflicts
        val resolvedChanges = conflictResolver.resolve(localChanges, remoteChanges)

        // 4. Apply to local database
        localRepository.applyChanges(resolvedChanges)

        // 5. Update sync timestamp
        localRepository.updateLastSyncTimestamp(now())
    }
}
```

### Conflict Resolution

**Strategy**: Last-Write-Wins (basierend auf `updated_at` Timestamp)

```kotlin
fun resolveConflict(local: Entity, remote: Entity): Entity {
    return if (local.updatedAt > remote.updatedAt) local else remote
}
```

### Realtime Subscriptions

```kotlin
val channel = supabase.realtime.channel("orders")
channel.on<PostgresAction.Insert>(schema = "public", table = "orders") { payload ->
    // Handle new order from another device
    localRepository.insert(payload.data)
}
```

## Akzeptanzkriterien

- [ ] Nutzer kann Cloud-Sync aktivieren/deaktivieren
- [ ] Lokale Änderungen werden bei Sync hochgeladen
- [ ] Remote-Änderungen werden bei Sync heruntergeladen
- [ ] Konflikte werden automatisch aufgelöst (Last-Write-Wins)
- [ ] Sync funktioniert bidirektional
- [ ] Realtime-Updates werden angezeigt
- [ ] Sync-Status ist in UI sichtbar
- [ ] Manuelle Sync-Option vorhanden
- [ ] Auto-Sync bei Netzwerkverbindung
- [ ] Fehlerhafte Syncs werden wiederholt
- [ ] Performance bei vielen Daten (>1000 Bestellungen) akzeptabel
- [ ] Row Level Security verhindert unbefugten Zugriff
- [ ] Unit Tests für Sync-Engine
- [ ] Integration Tests mit Supabase

## Abhängigkeiten

- E01 (Projekt-Setup)
- E02 (Bestellverwaltung)
- E05 (Authentifizierung) - Nutzer muss angemeldet sein

## Risiken

- Komplexität der Konfliktauflösung
- Netzwerk-Performance bei vielen Daten
- Supabase-Kosten bei vielen Nutzern
- Race Conditions bei gleichzeitigen Änderungen
- Dateninkonsistenzen bei fehlerhafter Sync

## Geschätzter Aufwand

**Story Points**: 21
**Dauer**: 2 Sprints

## Notes

- Delta-Sync implementieren um Bandbreite zu sparen
- Nur geänderte Felder übertragen
- Soft-Delete implementieren (deleted_at) statt echtem Löschen
- Backup-Strategie für Cloud-Daten definieren
