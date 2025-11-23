package com.gifttrack.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Room database entity for Recipient.
 * Represents a person who can receive gifts.
 */
@Entity(
    tableName = "recipients",
    indices = [
        Index(value = ["name"])
    ]
)
data class RecipientEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String?,

    @ColumnInfo(name = "relationship")
    val relationship: String?, // Familie, Freund, Kollege, etc.

    @ColumnInfo(name = "birthday")
    val birthday: Long?, // Optional birthday for gift planning

    @ColumnInfo(name = "notes")
    val notes: String?,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
)
