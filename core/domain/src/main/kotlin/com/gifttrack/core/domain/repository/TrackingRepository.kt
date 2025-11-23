package com.gifttrack.core.domain.repository

import com.gifttrack.core.domain.model.TrackingEvent
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing tracking events.
 *
 * This follows the repository pattern where the domain layer
 * defines the interface and the data layer provides the implementation.
 */
interface TrackingRepository {
    /**
     * Observes all tracking events for an order as a Flow.
     * The Flow will emit new values whenever the data changes.
     *
     * Events are ordered by timestamp descending (newest first).
     */
    fun getTrackingEventsByOrder(orderId: String): Flow<List<TrackingEvent>>

    /**
     * Gets the latest tracking event for an order.
     *
     * @return The latest tracking event if found, null otherwise.
     */
    suspend fun getLatestTrackingEvent(orderId: String): TrackingEvent?

    /**
     * Gets all tracking events for an order (non-reactive).
     *
     * @return List of tracking events ordered by timestamp descending.
     */
    suspend fun getTrackingEventsForOrder(orderId: String): List<TrackingEvent>

    /**
     * Inserts or updates a tracking event.
     */
    suspend fun insertTrackingEvent(event: TrackingEvent)

    /**
     * Inserts or updates multiple tracking events.
     */
    suspend fun insertTrackingEvents(events: List<TrackingEvent>)

    /**
     * Deletes all tracking events for an order.
     */
    suspend fun deleteTrackingEventsByOrder(orderId: String)

    /**
     * Deletes all tracking events (for testing/development).
     */
    suspend fun deleteAllTrackingEvents()
}
