package com.gifttrack.core.data.mapper

import com.gifttrack.core.database.entity.OrderEntity
import com.gifttrack.core.domain.model.Order
import com.gifttrack.core.domain.model.OrderStatus

/**
 * Maps OrderEntity (database) to Order (domain).
 */
fun OrderEntity.toModel(): Order {
    return Order(
        id = id,
        orderNumber = orderNumber,
        shopId = shopId,
        shopName = shopName,
        productName = productName,
        productDescription = productDescription,
        productImageUrl = productImageUrl,
        orderDate = orderDate,
        totalAmount = totalAmount,
        currency = currency,
        status = OrderStatus.valueOf(status),
        trackingNumber = trackingNumber,
        carrierName = carrierName,
        estimatedDeliveryDate = estimatedDeliveryDate,
        actualDeliveryDate = actualDeliveryDate,
        recipientId = recipientId,
        notes = notes,
        isGift = isGift,
        isHidden = isHidden,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

/**
 * Maps Order (domain) to OrderEntity (database).
 */
fun Order.toEntity(): OrderEntity {
    return OrderEntity(
        id = id,
        orderNumber = orderNumber,
        shopId = shopId,
        shopName = shopName,
        productName = productName,
        productDescription = productDescription,
        productImageUrl = productImageUrl,
        orderDate = orderDate,
        totalAmount = totalAmount,
        currency = currency,
        status = status.name,
        trackingNumber = trackingNumber,
        carrierName = carrierName,
        estimatedDeliveryDate = estimatedDeliveryDate,
        actualDeliveryDate = actualDeliveryDate,
        recipientId = recipientId,
        notes = notes,
        isGift = isGift,
        isHidden = isHidden,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
