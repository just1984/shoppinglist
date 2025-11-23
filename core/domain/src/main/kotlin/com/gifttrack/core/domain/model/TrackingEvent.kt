package com.gifttrack.core.domain.model

/**
 * Domain model representing a tracking event.
 *
 * A tracking event is a single update in the delivery history of an order.
 *
 * This is a pure domain entity without Android dependencies.
 */
data class TrackingEvent(
    val id: String,
    val orderId: String,
    val status: String, // Event status code
    val description: String, // Human-readable description
    val location: String?, // City, Country, Postal Code
    val timestamp: Long, // Unix timestamp in milliseconds
    val carrierCode: String?, // Carrier-specific event code
    val createdAt: Long
)
