# US-010: Room Database Schema definieren

**Epic**: E02 - Bestellverwaltung
**Priority**: High
**Story Points**: 5
**Status**: Not Started

## User Story

Als **Entwickler** möchte ich **ein Room Database Schema für Bestellungen, Produkte und Shops** definieren, damit Daten lokal gespeichert werden können.

## Beschreibung

Definition des kompletten Database Schemas mit Room für die Offline-First Datenhaltung. Entities, DAOs und Database-Klasse werden erstellt.

## Akzeptanzkriterien

- [ ] Room Dependencies sind konfiguriert
- [ ] Entities für Order, Product, Shop sind definiert
- [ ] Relationships (1:n) sind korrekt modelliert
- [ ] DAOs mit CRUD-Operationen sind implementiert
- [ ] Database-Klasse ist erstellt
- [ ] Type Converters für LocalDateTime sind implementiert
- [ ] Database-Version ist definiert
- [ ] Migration Strategy ist dokumentiert
- [ ] Unit Tests für DAOs sind vorhanden
- [ ] Database lässt sich erstellen und nutzen

## Technische Details

### Dependencies (libs.versions.toml)
```toml
[versions]
room = "2.6.1"

[libraries]
androidx-room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
androidx-room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "room" }
androidx-room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }
```

### Entities

```kotlin
// core/database/src/main/kotlin/com/gifttrack/database/entity/OrderEntity.kt
package com.gifttrack.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index
import java.time.LocalDateTime

@Entity(
    tableName = "orders",
    foreignKeys = [
        ForeignKey(
            entity = ShopEntity::class,
            parentColumns = ["id"],
            childColumns = ["shop_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("shop_id"), Index("order_date")]
)
data class OrderEntity(
    @PrimaryKey
    val id: String,
    val orderNumber: String,
    val shop_id: String?,
    val orderDate: LocalDateTime,
    val status: String,
    val totalAmount: Double?,
    val currency: String = "EUR",
    val notes: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["id"],
            childColumns = ["order_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("order_id")]
)
data class ProductEntity(
    @PrimaryKey
    val id: String,
    val order_id: String,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val localImagePath: String?,
    val price: Double?,
    val quantity: Int = 1,
    val recipientId: String?
)

@Entity(tableName = "shops")
data class ShopEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val logoUrl: String?,
    val website: String?,
    val createdAt: LocalDateTime
)

// Relation für Order mit Products
data class OrderWithProducts(
    @Embedded val order: OrderEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "order_id"
    )
    val products: List<ProductEntity>,
    @Relation(
        parentColumn = "shop_id",
        entityColumn = "id"
    )
    val shop: ShopEntity?
)
```

### Type Converters

```kotlin
// core/database/src/main/kotlin/com/gifttrack/database/converter/Converters.kt
package com.gifttrack.database.converter

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneOffset.UTC)
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? {
        return date?.atZone(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()
    }
}
```

### DAOs

```kotlin
// core/database/src/main/kotlin/com/gifttrack/database/dao/OrderDao.kt
package com.gifttrack.database.dao

import androidx.room.*
import com.gifttrack.database.entity.OrderEntity
import com.gifttrack.database.entity.OrderWithProducts
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Transaction
    @Query("SELECT * FROM orders ORDER BY orderDate DESC")
    fun getOrdersWithProducts(): Flow<List<OrderWithProducts>>

    @Query("SELECT * FROM orders ORDER BY orderDate DESC")
    fun getOrders(): Flow<List<OrderEntity>>

    @Transaction
    @Query("SELECT * FROM orders WHERE id = :orderId")
    suspend fun getOrderWithProductsById(orderId: String): OrderWithProducts?

    @Query("SELECT * FROM orders WHERE id = :orderId")
    suspend fun getOrderById(orderId: String): OrderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: OrderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(orders: List<OrderEntity>)

    @Update
    suspend fun update(order: OrderEntity)

    @Delete
    suspend fun delete(order: OrderEntity)

    @Query("DELETE FROM orders WHERE id = :orderId")
    suspend fun deleteById(orderId: String)

    @Query("SELECT * FROM orders WHERE shop_id = :shopId ORDER BY orderDate DESC")
    fun getOrdersByShop(shopId: String): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE status = :status ORDER BY orderDate DESC")
    fun getOrdersByStatus(status: String): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE orderNumber LIKE '%' || :query || '%' OR notes LIKE '%' || :query || '%'")
    fun searchOrders(query: String): Flow<List<OrderEntity>>
}

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE order_id = :orderId")
    suspend fun getProductsByOrderId(orderId: String): List<ProductEntity>

    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductById(productId: String): ProductEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ProductEntity>)

    @Update
    suspend fun update(product: ProductEntity)

    @Delete
    suspend fun delete(product: ProductEntity)

    @Query("DELETE FROM products WHERE id = :productId")
    suspend fun deleteById(productId: String)

    @Query("SELECT * FROM products WHERE recipientId = :recipientId")
    fun getProductsByRecipient(recipientId: String): Flow<List<ProductEntity>>
}

@Dao
interface ShopDao {
    @Query("SELECT * FROM shops ORDER BY name ASC")
    fun getShops(): Flow<List<ShopEntity>>

    @Query("SELECT * FROM shops WHERE id = :shopId")
    suspend fun getShopById(shopId: String): ShopEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shop: ShopEntity)

    @Update
    suspend fun update(shop: ShopEntity)

    @Delete
    suspend fun delete(shop: ShopEntity)

    @Query("SELECT * FROM shops WHERE name LIKE '%' || :query || '%'")
    fun searchShops(query: String): Flow<List<ShopEntity>>
}
```

### Database Class

```kotlin
// core/database/src/main/kotlin/com/gifttrack/database/GiftTrackDatabase.kt
package com.gifttrack.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gifttrack.database.converter.Converters
import com.gifttrack.database.dao.OrderDao
import com.gifttrack.database.dao.ProductDao
import com.gifttrack.database.dao.ShopDao
import com.gifttrack.database.entity.OrderEntity
import com.gifttrack.database.entity.ProductEntity
import com.gifttrack.database.entity.ShopEntity

@Database(
    entities = [
        OrderEntity::class,
        ProductEntity::class,
        ShopEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class GiftTrackDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    abstract fun productDao(): ProductDao
    abstract fun shopDao(): ShopDao
}
```

### Database Module (Hilt)

```kotlin
// core/database/src/main/kotlin/com/gifttrack/database/di/DatabaseModule.kt
package com.gifttrack.database.di

import android.content.Context
import androidx.room.Room
import com.gifttrack.database.GiftTrackDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

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
            .fallbackToDestructiveMigration() // TODO: Remove in production
            .build()
    }

    @Provides
    fun provideOrderDao(database: GiftTrackDatabase) = database.orderDao()

    @Provides
    fun provideProductDao(database: GiftTrackDatabase) = database.productDao()

    @Provides
    fun provideShopDao(database: GiftTrackDatabase) = database.shopDao()
}
```

## Definition of Done

- [ ] Alle Entities sind definiert
- [ ] Alle DAOs sind implementiert
- [ ] Type Converters funktionieren
- [ ] Database-Klasse ist erstellt
- [ ] Hilt DI ist konfiguriert
- [ ] Unit Tests für DAOs bestehen
- [ ] Database lässt sich erstellen
- [ ] Relationships (Foreign Keys) funktionieren
- [ ] Code Review durchgeführt

## Test Cases

```kotlin
@RunWith(AndroidJUnit4::class)
class OrderDaoTest {

    private lateinit var database: GiftTrackDatabase
    private lateinit var orderDao: OrderDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, GiftTrackDatabase::class.java).build()
        orderDao = database.orderDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndRetrieveOrder() = runBlocking {
        val order = OrderEntity(
            id = "1",
            orderNumber = "ORD-001",
            shop_id = null,
            orderDate = LocalDateTime.now(),
            status = "ORDERED",
            totalAmount = 99.99,
            currency = "EUR",
            notes = "Test order",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        orderDao.insert(order)
        val retrieved = orderDao.getOrderById("1")

        assertEquals(order, retrieved)
    }
}
```

## Dependencies

- US-002 (Module Setup)

## Notizen

- Verwende exportSchema = true für Migrations
- LocalDateTime benötigt Type Converter
- Foreign Keys mit CASCADE für Products
- Indices für Performance bei Queries
