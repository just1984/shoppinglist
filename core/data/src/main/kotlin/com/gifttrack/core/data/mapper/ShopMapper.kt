package com.gifttrack.core.data.mapper

import com.gifttrack.core.database.entity.ShopEntity
import com.gifttrack.core.domain.model.Shop

/**
 * Maps ShopEntity (database) to Shop (domain).
 */
fun ShopEntity.toModel(): Shop {
    return Shop(
        id = id,
        name = name,
        url = url,
        logoUrl = logoUrl,
        color = color,
        orderCount = orderCount,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

/**
 * Maps Shop (domain) to ShopEntity (database).
 */
fun Shop.toEntity(): ShopEntity {
    return ShopEntity(
        id = id,
        name = name,
        url = url,
        logoUrl = logoUrl,
        color = color,
        orderCount = orderCount,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
