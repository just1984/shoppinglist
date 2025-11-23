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
        shopName = shopName,
        productName = productName,
        productImageUrl = productImageUrl,
        orderDate = orderDate,
        totalAmount = totalAmount,
        status = OrderStatus.valueOf(status),
        trackingNumber = trackingNumber,
        recipientId = recipientId
    )
}

/**
 * Maps Order (domain) to OrderEntity (database).
 */
fun Order.toEntity(): OrderEntity {
    return OrderEntity(
        id = id,
        orderNumber = orderNumber,
        shopName = shopName,
        productName = productName,
        productImageUrl = productImageUrl,
        orderDate = orderDate,
        totalAmount = totalAmount,
        status = status.name,
        trackingNumber = trackingNumber,
        recipientId = recipientId
    )
}
