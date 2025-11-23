package com.gifttrack.core.domain.repository

import com.gifttrack.core.domain.model.Shop
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing shops.
 *
 * This follows the repository pattern where the domain layer
 * defines the interface and the data layer provides the implementation.
 */
interface ShopRepository {
    /**
     * Observes all shops as a Flow.
     * The Flow will emit new values whenever the data changes.
     */
    fun getShops(): Flow<List<Shop>>

    /**
     * Retrieves a single shop by its ID.
     *
     * @return The shop if found, null otherwise.
     */
    suspend fun getShopById(id: String): Shop?

    /**
     * Retrieves a shop by its name.
     *
     * @return The shop if found, null otherwise.
     */
    suspend fun getShopByName(name: String): Shop?

    /**
     * Inserts or updates a shop.
     */
    suspend fun insertShop(shop: Shop)

    /**
     * Inserts or updates multiple shops.
     */
    suspend fun insertShops(shops: List<Shop>)

    /**
     * Updates a shop.
     */
    suspend fun updateShop(shop: Shop)

    /**
     * Deletes a shop by its ID.
     */
    suspend fun deleteShop(id: String)

    /**
     * Updates the order count for a shop.
     */
    suspend fun updateShopOrderCount(shopId: String, count: Int)

    /**
     * Deletes all shops (for testing/development).
     */
    suspend fun deleteAllShops()
}
