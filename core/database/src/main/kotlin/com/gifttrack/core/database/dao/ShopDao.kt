package com.gifttrack.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.gifttrack.core.database.entity.ShopEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Shop entities.
 */
@Dao
interface ShopDao {
    /**
     * Observes all shops.
     * @return Flow of all shops that emits on every database change.
     */
    @Query("SELECT * FROM shops ORDER BY name ASC")
    fun getShops(): Flow<List<ShopEntity>>

    /**
     * Gets a single shop by ID.
     */
    @Query("SELECT * FROM shops WHERE id = :id")
    suspend fun getShopById(id: String): ShopEntity?

    /**
     * Gets a shop by name.
     */
    @Query("SELECT * FROM shops WHERE name = :name")
    suspend fun getShopByName(name: String): ShopEntity?

    /**
     * Inserts or replaces a shop.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shop: ShopEntity)

    /**
     * Inserts multiple shops.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(shops: List<ShopEntity>)

    /**
     * Updates a shop.
     */
    @Update
    suspend fun update(shop: ShopEntity)

    /**
     * Deletes a shop by ID.
     */
    @Query("DELETE FROM shops WHERE id = :id")
    suspend fun deleteById(id: String)

    /**
     * Updates the order count for a shop.
     */
    @Query("UPDATE shops SET order_count = :count, updated_at = :updatedAt WHERE id = :shopId")
    suspend fun updateOrderCount(shopId: String, count: Int, updatedAt: Long)

    /**
     * Deletes all shops.
     */
    @Query("DELETE FROM shops")
    suspend fun deleteAll()
}
