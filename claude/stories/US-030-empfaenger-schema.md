# US-030: Empfänger-Datenbankschema erstellen

**Epic**: E03 - Empfängerverwaltung & Geschenkplanung
**Priority**: High
**Story Points**: 3
**Status**: Not Started

## User Story

Als **Entwickler** möchte ich **ein Datenbankschema für Empfänger und Geschenkzuordnungen** erstellen, damit Produkte Empfängern zugeordnet werden können.

## Beschreibung

Erweiterung der Room Database um Empfänger-Entities und Zuordnungstabellen für die Geschenkplanung.

## Akzeptanzkriterien

- [ ] Recipient Entity ist definiert
- [ ] GiftAssignment Entity für m:n Relation ist definiert
- [ ] RecipientDao mit CRUD-Operationen ist implementiert
- [ ] GiftAssignmentDao ist implementiert
- [ ] Database-Version ist erhöht
- [ ] Migration ist implementiert
- [ ] Foreign Keys sind korrekt definiert
- [ ] Queries für Empfänger mit Geschenken funktionieren
- [ ] Unit Tests sind vorhanden

## Technische Details

### Entities

```kotlin
// core/database/src/main/kotlin/com/gifttrack/database/entity/RecipientEntity.kt
package com.gifttrack.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "recipients")
data class RecipientEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val avatarUrl: String?,
    val localAvatarPath: String?,
    val notes: String?,
    val birthday: LocalDate?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

@Entity(
    tableName = "gift_assignments",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["product_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = RecipientEntity::class,
            parentColumns = ["id"],
            childColumns = ["recipient_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("product_id"), Index("recipient_id")]
)
data class GiftAssignmentEntity(
    @PrimaryKey
    val id: String,
    val product_id: String,
    val recipient_id: String,
    val status: String, // "OPEN", "COMPLETED"
    val assignedAt: LocalDateTime,
    val completedAt: LocalDateTime?
)

// Relations
data class RecipientWithGifts(
    @Embedded val recipient: RecipientEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipient_id",
        entity = GiftAssignmentEntity::class
    )
    val giftAssignments: List<GiftAssignmentWithProduct>
)

data class GiftAssignmentWithProduct(
    @Embedded val assignment: GiftAssignmentEntity,
    @Relation(
        parentColumn = "product_id",
        entityColumn = "id"
    )
    val product: ProductEntity
)
```

### Type Converter für LocalDate

```kotlin
// core/database/src/main/kotlin/com/gifttrack/database/converter/Converters.kt
class Converters {
    // ... existing converters

    @TypeConverter
    fun fromLocalDate(value: Long?): LocalDate? {
        return value?.let { LocalDate.ofEpochDay(it) }
    }

    @TypeConverter
    fun localDateToLong(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }
}
```

### DAOs

```kotlin
// core/database/src/main/kotlin/com/gifttrack/database/dao/RecipientDao.kt
package com.gifttrack.database.dao

import androidx.room.*
import com.gifttrack.database.entity.RecipientEntity
import com.gifttrack.database.entity.RecipientWithGifts
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipientDao {
    @Query("SELECT * FROM recipients ORDER BY name ASC")
    fun getRecipients(): Flow<List<RecipientEntity>>

    @Transaction
    @Query("SELECT * FROM recipients ORDER BY name ASC")
    fun getRecipientsWithGifts(): Flow<List<RecipientWithGifts>>

    @Transaction
    @Query("SELECT * FROM recipients WHERE id = :recipientId")
    suspend fun getRecipientWithGifts(recipientId: String): RecipientWithGifts?

    @Query("SELECT * FROM recipients WHERE id = :recipientId")
    suspend fun getRecipientById(recipientId: String): RecipientEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipient: RecipientEntity)

    @Update
    suspend fun update(recipient: RecipientEntity)

    @Delete
    suspend fun delete(recipient: RecipientEntity)

    @Query("DELETE FROM recipients WHERE id = :recipientId")
    suspend fun deleteById(recipientId: String)

    @Query("SELECT * FROM recipients WHERE name LIKE '%' || :query || '%'")
    fun searchRecipients(query: String): Flow<List<RecipientEntity>>

    @Query("SELECT COUNT(*) FROM recipients")
    suspend fun getRecipientCount(): Int
}

@Dao
interface GiftAssignmentDao {
    @Query("SELECT * FROM gift_assignments WHERE recipient_id = :recipientId")
    fun getAssignmentsByRecipient(recipientId: String): Flow<List<GiftAssignmentEntity>>

    @Query("SELECT * FROM gift_assignments WHERE product_id = :productId")
    suspend fun getAssignmentsByProduct(productId: String): List<GiftAssignmentEntity>

    @Transaction
    @Query("SELECT * FROM gift_assignments WHERE recipient_id = :recipientId")
    fun getAssignmentsWithProductByRecipient(recipientId: String): Flow<List<GiftAssignmentWithProduct>>

    @Query("SELECT * FROM gift_assignments WHERE id = :assignmentId")
    suspend fun getAssignmentById(assignmentId: String): GiftAssignmentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(assignment: GiftAssignmentEntity)

    @Update
    suspend fun update(assignment: GiftAssignmentEntity)

    @Delete
    suspend fun delete(assignment: GiftAssignmentEntity)

    @Query("DELETE FROM gift_assignments WHERE product_id = :productId AND recipient_id = :recipientId")
    suspend fun deleteAssignment(productId: String, recipientId: String)

    @Query("SELECT * FROM gift_assignments WHERE status = :status")
    fun getAssignmentsByStatus(status: String): Flow<List<GiftAssignmentEntity>>

    @Query("""
        SELECT COUNT(*) FROM gift_assignments
        WHERE recipient_id = :recipientId AND status = 'OPEN'
    """)
    suspend fun getOpenGiftsCount(recipientId: String): Int

    @Query("""
        SELECT COUNT(*) FROM gift_assignments
        WHERE recipient_id = :recipientId AND status = 'COMPLETED'
    """)
    suspend fun getCompletedGiftsCount(recipientId: String): Int
}
```

### Database Migration

```kotlin
// core/database/src/main/kotlin/com/gifttrack/database/GiftTrackDatabase.kt
@Database(
    entities = [
        OrderEntity::class,
        ProductEntity::class,
        ShopEntity::class,
        RecipientEntity::class,
        GiftAssignmentEntity::class
    ],
    version = 2, // Increased version
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class GiftTrackDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    abstract fun productDao(): ProductDao
    abstract fun shopDao(): ShopDao
    abstract fun recipientDao(): RecipientDao
    abstract fun giftAssignmentDao(): GiftAssignmentDao
}

// Migration from version 1 to 2
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Create recipients table
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS recipients (
                id TEXT PRIMARY KEY NOT NULL,
                name TEXT NOT NULL,
                avatarUrl TEXT,
                localAvatarPath TEXT,
                notes TEXT,
                birthday INTEGER,
                createdAt INTEGER NOT NULL,
                updatedAt INTEGER NOT NULL
            )
        """)

        // Create gift_assignments table
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS gift_assignments (
                id TEXT PRIMARY KEY NOT NULL,
                product_id TEXT NOT NULL,
                recipient_id TEXT NOT NULL,
                status TEXT NOT NULL,
                assignedAt INTEGER NOT NULL,
                completedAt INTEGER,
                FOREIGN KEY(product_id) REFERENCES products(id) ON DELETE CASCADE,
                FOREIGN KEY(recipient_id) REFERENCES recipients(id) ON DELETE CASCADE
            )
        """)

        // Create indices
        database.execSQL("CREATE INDEX IF NOT EXISTS index_gift_assignments_product_id ON gift_assignments(product_id)")
        database.execSQL("CREATE INDEX IF NOT EXISTS index_gift_assignments_recipient_id ON gift_assignments(recipient_id)")
    }
}
```

### Update DatabaseModule

```kotlin
@Provides
@Singleton
fun provideDatabase(
    @ApplicationContext context: Context
): GiftTrackDatabase {
    return Room.databaseBuilder(
        context,
        GiftTrackDatabase::class.java,
        "gifttrack.db"
    )
        .addMigrations(MIGRATION_1_2)
        .build()
}

@Provides
fun provideRecipientDao(database: GiftTrackDatabase) = database.recipientDao()

@Provides
fun provideGiftAssignmentDao(database: GiftTrackDatabase) = database.giftAssignmentDao()
```

## Definition of Done

- [ ] Recipient Entity ist erstellt
- [ ] GiftAssignment Entity ist erstellt
- [ ] RecipientDao ist implementiert
- [ ] GiftAssignmentDao ist implementiert
- [ ] Migration 1→2 ist implementiert
- [ ] Database-Version ist auf 2 erhöht
- [ ] Relations funktionieren (RecipientWithGifts)
- [ ] Unit Tests für neue DAOs bestehen
- [ ] Migration Tests bestehen
- [ ] Code Review durchgeführt

## Test Cases

```kotlin
@Test
fun insertRecipientAndAssignGift() = runBlocking {
    // Insert recipient
    val recipient = RecipientEntity(
        id = "1",
        name = "John Doe",
        avatarUrl = null,
        localAvatarPath = null,
        notes = null,
        birthday = LocalDate.of(1990, 1, 1),
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )
    recipientDao.insert(recipient)

    // Insert product (from previous test)
    val product = createTestProduct()
    productDao.insert(product)

    // Create gift assignment
    val assignment = GiftAssignmentEntity(
        id = "1",
        product_id = product.id,
        recipient_id = recipient.id,
        status = "OPEN",
        assignedAt = LocalDateTime.now(),
        completedAt = null
    )
    giftAssignmentDao.insert(assignment)

    // Verify
    val recipientWithGifts = recipientDao.getRecipientWithGifts("1")
    assertEquals(1, recipientWithGifts?.giftAssignments?.size)
    assertEquals("OPEN", recipientWithGifts?.giftAssignments?.first()?.assignment?.status)
}
```

## Dependencies

- US-010 (Order Database Schema)

## Notizen

- CASCADE Delete: Wenn Empfänger gelöscht wird, werden Zuordnungen gelöscht
- Status: OPEN, COMPLETED
- LocalDate für Geburtstag (optional)
- Avatar kann lokal oder URL sein
