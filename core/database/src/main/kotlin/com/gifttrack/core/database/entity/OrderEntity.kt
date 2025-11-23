package com.gifttrack.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room database entity for Order.
 * This is the database representation of an Order.
 */
@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey
    val id: String,
    val orderNumber: String,
    val shopName: String,
    val productName: String?,
    val productImageUrl: String?,
    val orderDate: Long,
    val totalAmount: Double?,
    val status: String, // Stored as String, mapped to enum in domain
    val trackingNumber: String?,
    val recipientId: String?
)
