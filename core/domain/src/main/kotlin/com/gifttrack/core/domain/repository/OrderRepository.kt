package com.gifttrack.core.domain.repository

import com.gifttrack.core.domain.model.Order
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing orders.
 *
 * This follows the repository pattern where the domain layer
 * defines the interface and the data layer provides the implementation.
 */
interface OrderRepository {
    /**
     * Observes all orders as a Flow.
     * The Flow will emit new values whenever the data changes.
     */
    fun getOrders(): Flow<List<Order>>

    /**
     * Gets orders filtered by recipient ID.
     */
    fun getOrdersByRecipient(recipientId: String): Flow<List<Order>>

    /**
     * Gets orders filtered by shop ID.
     */
    fun getOrdersByShop(shopId: String): Flow<List<Order>>

    /**
     * Gets orders filtered by status.
     */
    fun getOrdersByStatus(status: com.gifttrack.core.domain.model.OrderStatus): Flow<List<Order>>

    /**
     * Retrieves a single order by its ID.
     *
     * @return The order if found, null otherwise.
     */
    suspend fun getOrderById(id: String): Order?

    /**
     * Inserts or updates an order.
     */
    suspend fun insertOrder(order: Order)

    /**
     * Inserts or updates multiple orders.
     */
    suspend fun insertOrders(orders: List<Order>)

    /**
     * Deletes an order by its ID.
     */
    suspend fun deleteOrder(id: String)

    /**
     * Updates the status of an order.
     */
    suspend fun updateOrderStatus(orderId: String, status: com.gifttrack.core.domain.model.OrderStatus)

    /**
     * Deletes all orders (for testing/development).
     */
    suspend fun deleteAllOrders()
}
