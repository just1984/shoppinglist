package com.gifttrack.core.data.mapper

import com.gifttrack.core.database.entity.TrackingEventEntity
import com.gifttrack.core.domain.model.TrackingEvent

/**
 * Maps TrackingEventEntity (database) to TrackingEvent (domain).
 */
fun TrackingEventEntity.toModel(): TrackingEvent {
    return TrackingEvent(
        id = id,
        orderId = orderId,
        status = status,
        description = description,
        location = location,
        timestamp = timestamp,
        carrierCode = carrierCode,
        createdAt = createdAt
    )
}

/**
 * Maps TrackingEvent (domain) to TrackingEventEntity (database).
 */
fun TrackingEvent.toEntity(): TrackingEventEntity {
    return TrackingEventEntity(
        id = id,
        orderId = orderId,
        status = status,
        description = description,
        location = location,
        timestamp = timestamp,
        carrierCode = carrierCode,
        createdAt = createdAt
    )
}
