package com.gifttrack.core.domain.model

/**
 * Domain model representing an order from an online shop.
 *
 * This is a pure domain entity without Android dependencies.
 */
data class Order(
    val id: String,
    val orderNumber: String,
    val shopId: String?,
    val shopName: String,
    val productName: String?,
    val productDescription: String?,
    val productImageUrl: String?,
    val orderDate: Long, // Unix timestamp in milliseconds
    val totalAmount: Double?,
    val currency: String = "EUR",
    val status: OrderStatus,
    val trackingNumber: String?,
    val carrierName: String?,
    val estimatedDeliveryDate: Long?,
    val actualDeliveryDate: Long?,
    val recipientId: String?,
    val notes: String?,
    val isGift: Boolean = false,
    val isHidden: Boolean = false,
    val createdAt: Long,
    val updatedAt: Long
)

/**
 * Status of an order in the order lifecycle.
 */
enum class OrderStatus {
    ORDERED,
    PROCESSING,
    SHIPPED,
    IN_TRANSIT,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELLED,
    RETURNED
}
