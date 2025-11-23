package com.gifttrack.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gifttrack.core.database.entity.TrackingEventEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Tracking Event entities.
 */
@Dao
interface TrackingEventDao {
    /**
     * Observes all tracking events for an order.
     * @return Flow of tracking events ordered by timestamp descending.
     */
    @Query("SELECT * FROM tracking_events WHERE order_id = :orderId ORDER BY timestamp DESC")
    fun getTrackingEventsByOrder(orderId: String): Flow<List<TrackingEventEntity>>

    /**
     * Gets the latest tracking event for an order.
     */
    @Query("SELECT * FROM tracking_events WHERE order_id = :orderId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestTrackingEvent(orderId: String): TrackingEventEntity?

    /**
     * Gets all tracking events for an order (non-reactive).
     */
    @Query("SELECT * FROM tracking_events WHERE order_id = :orderId ORDER BY timestamp DESC")
    suspend fun getTrackingEventsForOrder(orderId: String): List<TrackingEventEntity>

    /**
     * Inserts or replaces a tracking event.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: TrackingEventEntity)

    /**
     * Inserts multiple tracking events.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(events: List<TrackingEventEntity>)

    /**
     * Deletes all tracking events for an order.
     */
    @Query("DELETE FROM tracking_events WHERE order_id = :orderId")
    suspend fun deleteByOrderId(orderId: String)

    /**
     * Deletes all tracking events.
     */
    @Query("DELETE FROM tracking_events")
    suspend fun deleteAll()
}
