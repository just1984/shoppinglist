package com.gifttrack.core.data.repository

import com.gifttrack.core.database.dao.TrackingEventDao
import com.gifttrack.core.data.mapper.toEntity
import com.gifttrack.core.data.mapper.toModel
import com.gifttrack.core.domain.model.TrackingEvent
import com.gifttrack.core.domain.repository.TrackingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of TrackingRepository using Room database.
 *
 * This class bridges the domain layer (repository interface)
 * and the data layer (Room database).
 */
class TrackingRepositoryImpl @Inject constructor(
    private val trackingEventDao: TrackingEventDao
) : TrackingRepository {

    override fun getTrackingEventsByOrder(orderId: String): Flow<List<TrackingEvent>> {
        return trackingEventDao.getTrackingEventsByOrder(orderId).map { entities ->
            entities.map { it.toModel() }
        }
    }

    override suspend fun getLatestTrackingEvent(orderId: String): TrackingEvent? {
        return trackingEventDao.getLatestTrackingEvent(orderId)?.toModel()
    }

    override suspend fun getTrackingEventsForOrder(orderId: String): List<TrackingEvent> {
        return trackingEventDao.getTrackingEventsForOrder(orderId).map { it.toModel() }
    }

    override suspend fun insertTrackingEvent(event: TrackingEvent) {
        trackingEventDao.insert(event.toEntity())
    }

    override suspend fun insertTrackingEvents(events: List<TrackingEvent>) {
        trackingEventDao.insertAll(events.map { it.toEntity() })
    }

    override suspend fun deleteTrackingEventsByOrder(orderId: String) {
        trackingEventDao.deleteByOrderId(orderId)
    }

    override suspend fun deleteAllTrackingEvents() {
        trackingEventDao.deleteAll()
    }
}
