package com.gifttrack.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gifttrack.core.database.entity.OrderEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Order entities.
 */
@Dao
interface OrderDao {
    /**
     * Observes all orders.
     * @return Flow of all orders that emits on every database change.
     */
    @Query("SELECT * FROM orders ORDER BY order_date DESC")
    fun getOrders(): Flow<List<OrderEntity>>

    /**
     * Observes orders filtered by recipient.
     */
    @Query("SELECT * FROM orders WHERE recipient_id = :recipientId ORDER BY order_date DESC")
    fun getOrdersByRecipient(recipientId: String): Flow<List<OrderEntity>>

    /**
     * Observes orders filtered by shop.
     */
    @Query("SELECT * FROM orders WHERE shop_id = :shopId ORDER BY order_date DESC")
    fun getOrdersByShop(shopId: String): Flow<List<OrderEntity>>

    /**
     * Observes orders filtered by status.
     */
    @Query("SELECT * FROM orders WHERE status = :status ORDER BY order_date DESC")
    fun getOrdersByStatus(status: String): Flow<List<OrderEntity>>

    /**
     * Gets a single order by ID.
     */
    @Query("SELECT * FROM orders WHERE id = :id")
    suspend fun getOrderById(id: String): OrderEntity?

    /**
     * Inserts or replaces an order.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: OrderEntity)

    /**
     * Inserts multiple orders.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(orders: List<OrderEntity>)

    /**
     * Deletes an order by ID.
     */
    @Query("DELETE FROM orders WHERE id = :id")
    suspend fun deleteById(id: String)

    /**
     * Updates the status of an order.
     */
    @Query("UPDATE orders SET status = :status WHERE id = :orderId")
    suspend fun updateStatus(orderId: String, status: String)

    /**
     * Deletes all orders.
     */
    @Query("DELETE FROM orders")
    suspend fun deleteAll()
}
