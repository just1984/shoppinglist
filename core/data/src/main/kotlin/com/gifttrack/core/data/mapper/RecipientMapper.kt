package com.gifttrack.core.data.mapper

import com.gifttrack.core.database.entity.RecipientEntity
import com.gifttrack.core.domain.model.Recipient

/**
 * Maps RecipientEntity (database) to Recipient (domain).
 */
fun RecipientEntity.toModel(): Recipient {
    return Recipient(
        id = id,
        name = name,
        avatarUrl = avatarUrl,
        relationship = relationship,
        notes = notes,
        createdAt = createdAt
    )
}

/**
 * Maps Recipient (domain) to RecipientEntity (database).
 */
fun Recipient.toEntity(): RecipientEntity {
    return RecipientEntity(
        id = id,
        name = name,
        avatarUrl = avatarUrl,
        relationship = relationship,
        notes = notes,
        createdAt = createdAt
    )
}
