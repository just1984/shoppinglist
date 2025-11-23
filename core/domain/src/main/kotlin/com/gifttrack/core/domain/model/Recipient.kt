package com.gifttrack.core.domain.model

/**
 * Domain model representing a gift recipient.
 */
data class Recipient(
    val id: String,
    val name: String,
    val avatarUrl: String?,
    val relationship: String?, // e.g., "Familie", "Freund", "Kollege"
    val birthday: Long?, // Unix timestamp in milliseconds
    val notes: String?,
    val createdAt: Long, // Unix timestamp in milliseconds
    val updatedAt: Long
)
