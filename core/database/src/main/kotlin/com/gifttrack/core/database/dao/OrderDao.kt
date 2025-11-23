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
    @Query("SELECT * FROM orders ORDER BY orderDate DESC")
    fun getOrders(): Flow<List<OrderEntity>>

    /**
     * Observes orders filtered by recipient.
     */
    @Query("SELECT * FROM orders WHERE recipientId = :recipientId ORDER BY orderDate DESC")
    fun getOrdersByRecipient(recipientId: String): Flow<List<OrderEntity>>

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
