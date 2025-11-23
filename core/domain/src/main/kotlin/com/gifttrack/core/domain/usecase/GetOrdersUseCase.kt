package com.gifttrack.core.domain.usecase

import com.gifttrack.core.domain.model.Order
import com.gifttrack.core.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving all orders.
 *
 * Use cases encapsulate business logic and coordinate between
 * different repositories. They are the entry point for business
 * operations from the presentation layer.
 */
class GetOrdersUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    /**
     * Invoke operator allows calling the use case like a function.
     *
     * @return Flow of all orders that updates when data changes.
     */
    operator fun invoke(): Flow<List<Order>> {
        return orderRepository.getOrders()
    }
}
