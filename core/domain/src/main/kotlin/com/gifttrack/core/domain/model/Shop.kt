package com.gifttrack.core.domain.model

/**
 * Domain model representing an online shop.
 *
 * This is a pure domain entity without Android dependencies.
 */
data class Shop(
    val id: String,
    val name: String,
    val url: String?,
    val logoUrl: String?,
    val color: String?, // Hex color for UI theming (e.g., "#FF5722")
    val orderCount: Int = 0,
    val createdAt: Long,
    val updatedAt: Long
)
