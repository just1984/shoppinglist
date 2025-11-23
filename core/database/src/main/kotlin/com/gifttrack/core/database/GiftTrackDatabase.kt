package com.gifttrack.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gifttrack.core.database.dao.OrderDao
import com.gifttrack.core.database.dao.RecipientDao
import com.gifttrack.core.database.entity.OrderEntity
import com.gifttrack.core.database.entity.RecipientEntity

/**
 * GiftTrack Room Database.
 *
 * Contains all entities and provides DAOs.
 */
@Database(
    entities = [
        OrderEntity::class,
        RecipientEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class GiftTrackDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    abstract fun recipientDao(): RecipientDao
}
