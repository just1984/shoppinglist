package com.gifttrack.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room database entity for Recipient.
 */
@Entity(tableName = "recipients")
data class RecipientEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val avatarUrl: String?,
    val relationship: String?,
    val notes: String?,
    val createdAt: Long
)
