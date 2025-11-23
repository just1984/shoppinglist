package com.gifttrack.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Room database entity for Shop.
 * Represents an online shop where orders are placed.
 */
@Entity(
    tableName = "shops",
    indices = [
        Index(value = ["name"], unique = true)
    ]
)
data class ShopEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "url")
    val url: String?,

    @ColumnInfo(name = "logo_url")
    val logoUrl: String?,

    @ColumnInfo(name = "color")
    val color: String?, // Hex color for UI theming

    @ColumnInfo(name = "order_count")
    val orderCount: Int = 0, // Denormalized count for quick display

    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
)
