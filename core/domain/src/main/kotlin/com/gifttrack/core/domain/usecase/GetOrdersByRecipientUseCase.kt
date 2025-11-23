package com.gifttrack.core.domain.usecase

import com.gifttrack.core.domain.model.Order
import com.gifttrack.core.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving orders filtered by recipient.
 */
class GetOrdersByRecipientUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    operator fun invoke(recipientId: String): Flow<List<Order>> {
        return orderRepository.getOrdersByRecipient(recipientId)
    }
}
