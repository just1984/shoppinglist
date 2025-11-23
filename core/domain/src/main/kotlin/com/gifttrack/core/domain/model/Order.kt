package com.gifttrack.core.domain.model

/**
 * Domain model representing an order from an online shop.
 *
 * This is a pure domain entity without Android dependencies.
 */
data class Order(
    val id: String,
    val orderNumber: String,
    val shopName: String,
    val productName: String?,
    val productImageUrl: String?,
    val orderDate: Long, // Unix timestamp in milliseconds
    val totalAmount: Double?,
    val status: OrderStatus,
    val trackingNumber: String? = null,
    val recipientId: String? = null
)

/**
 * Status of an order in the order lifecycle.
 */
enum class OrderStatus {
    ORDERED,
    PROCESSING,
    SHIPPED,
    IN_TRANSIT,
    DELIVERED,
    CANCELLED,
    RETURNED
}
