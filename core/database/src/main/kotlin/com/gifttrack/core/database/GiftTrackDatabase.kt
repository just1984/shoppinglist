package com.gifttrack.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gifttrack.core.database.dao.OrderDao
import com.gifttrack.core.database.dao.RecipientDao
import com.gifttrack.core.database.dao.ShopDao
import com.gifttrack.core.database.dao.TrackingEventDao
import com.gifttrack.core.database.entity.OrderEntity
import com.gifttrack.core.database.entity.RecipientEntity
import com.gifttrack.core.database.entity.ShopEntity
import com.gifttrack.core.database.entity.TrackingEventEntity

/**
 * GiftTrack Room Database.
 *
 * Contains all entities and provides DAOs.
 *
 * **Entities:**
 * - OrderEntity: Orders placed in online shops
 * - RecipientEntity: People who receive gifts
 * - ShopEntity: Online shops where orders are placed
 * - TrackingEventEntity: Tracking history for orders
 *
 * **Version History:**
 * - v1: Initial schema with OrderEntity and RecipientEntity (basic fields)
 * - v2: Extended schema with ShopEntity, TrackingEventEntity, and enhanced OrderEntity/RecipientEntity fields
 */
@Database(
    entities = [
        OrderEntity::class,
        RecipientEntity::class,
        ShopEntity::class,
        TrackingEventEntity::class
    ],
    version = 2,
    exportSchema = true
)
abstract class GiftTrackDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    abstract fun recipientDao(): RecipientDao
    abstract fun shopDao(): ShopDao
    abstract fun trackingEventDao(): TrackingEventDao
}
