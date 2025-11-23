package com.gifttrack.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Room database entity for Tracking Event.
 * Represents a single tracking event in the delivery history.
 *
 * Foreign Keys:
 * - orderId references orders table
 */
@Entity(
    tableName = "tracking_events",
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["id"],
            childColumns = ["order_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["order_id"]),
        Index(value = ["timestamp"])
    ]
)
data class TrackingEventEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "order_id")
    val orderId: String,

    @ColumnInfo(name = "status")
    val status: String, // Event status (e.g., "IN_TRANSIT", "OUT_FOR_DELIVERY")

    @ColumnInfo(name = "description")
    val description: String, // Human-readable description

    @ColumnInfo(name = "location")
    val location: String?, // City, Country, etc.

    @ColumnInfo(name = "timestamp")
    val timestamp: Long,

    @ColumnInfo(name = "carrier_code")
    val carrierCode: String?, // Carrier-specific code

    @ColumnInfo(name = "created_at")
    val createdAt: Long
)
