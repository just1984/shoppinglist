package com.gifttrack.core.data.repository

import com.gifttrack.core.database.dao.OrderDao
import com.gifttrack.core.data.mapper.toEntity
import com.gifttrack.core.data.mapper.toModel
import com.gifttrack.core.domain.model.Order
import com.gifttrack.core.domain.model.OrderStatus
import com.gifttrack.core.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of OrderRepository using Room database.
 *
 * This class bridges the domain layer (repository interface)
 * and the data layer (Room database).
 */
class OrderRepositoryImpl @Inject constructor(
    private val orderDao: OrderDao
) : OrderRepository {

    override fun getOrders(): Flow<List<Order>> {
        return orderDao.getOrders().map { entities ->
            entities.map { it.toModel() }
        }
    }

    override fun getOrdersByRecipient(recipientId: String): Flow<List<Order>> {
        return orderDao.getOrdersByRecipient(recipientId).map { entities ->
            entities.map { it.toModel() }
        }
    }

    override fun getOrdersByShop(shopId: String): Flow<List<Order>> {
        return orderDao.getOrdersByShop(shopId).map { entities ->
            entities.map { it.toModel() }
        }
    }

    override fun getOrdersByStatus(status: OrderStatus): Flow<List<Order>> {
        return orderDao.getOrdersByStatus(status.name).map { entities ->
            entities.map { it.toModel() }
        }
    }

    override suspend fun getOrderById(id: String): Order? {
        return orderDao.getOrderById(id)?.toModel()
    }

    override suspend fun insertOrder(order: Order) {
        orderDao.insert(order.toEntity())
    }

    override suspend fun insertOrders(orders: List<Order>) {
        orderDao.insertAll(orders.map { it.toEntity() })
    }

    override suspend fun deleteOrder(id: String) {
        orderDao.deleteById(id)
    }

    override suspend fun updateOrderStatus(orderId: String, status: OrderStatus) {
        orderDao.updateStatus(orderId, status.name)
    }

    override suspend fun deleteAllOrders() {
        orderDao.deleteAll()
    }
}
