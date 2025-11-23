package com.gifttrack.core.domain.model

/**
 * Domain model representing a gift recipient.
 */
data class Recipient(
    val id: String,
    val name: String,
    val avatarUrl: String? = null,
    val relationship: String? = null, // e.g., "Family", "Friend", "Colleague"
    val notes: String? = null,
    val createdAt: Long // Unix timestamp in milliseconds
)
