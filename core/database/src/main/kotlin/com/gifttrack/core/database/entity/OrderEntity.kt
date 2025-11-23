package com.gifttrack.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Room database entity for Order.
 * This is the database representation of an Order.
 *
 * Foreign Keys:
 * - shopId references shops table
 * - recipientId references recipients table
 */
@Entity(
    tableName = "orders",
    foreignKeys = [
        ForeignKey(
            entity = ShopEntity::class,
            parentColumns = ["id"],
            childColumns = ["shop_id"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = RecipientEntity::class,
            parentColumns = ["id"],
            childColumns = ["recipient_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["shop_id"]),
        Index(value = ["recipient_id"]),
        Index(value = ["order_date"]),
        Index(value = ["status"])
    ]
)
data class OrderEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "order_number")
    val orderNumber: String,

    @ColumnInfo(name = "shop_id")
    val shopId: String?,

    @ColumnInfo(name = "shop_name")
    val shopName: String, // Denormalized for quick display

    @ColumnInfo(name = "product_name")
    val productName: String?,

    @ColumnInfo(name = "product_description")
    val productDescription: String?,

    @ColumnInfo(name = "product_image_url")
    val productImageUrl: String?,

    @ColumnInfo(name = "order_date")
    val orderDate: Long,

    @ColumnInfo(name = "total_amount")
    val totalAmount: Double?,

    @ColumnInfo(name = "currency")
    val currency: String = "EUR",

    @ColumnInfo(name = "status")
    val status: String, // ORDERED, PROCESSING, SHIPPED, IN_TRANSIT, OUT_FOR_DELIVERY, DELIVERED, CANCELLED

    @ColumnInfo(name = "tracking_number")
    val trackingNumber: String?,

    @ColumnInfo(name = "carrier_name")
    val carrierName: String?, // DHL, DPD, Hermes, UPS, GLS, etc.

    @ColumnInfo(name = "estimated_delivery_date")
    val estimatedDeliveryDate: Long?,

    @ColumnInfo(name = "actual_delivery_date")
    val actualDeliveryDate: Long?,

    @ColumnInfo(name = "recipient_id")
    val recipientId: String?,

    @ColumnInfo(name = "notes")
    val notes: String?,

    @ColumnInfo(name = "is_gift")
    val isGift: Boolean = false,

    @ColumnInfo(name = "is_hidden")
    val isHidden: Boolean = false,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
)
