package com.gifttrack.core.data.repository

import com.gifttrack.core.database.dao.ShopDao
import com.gifttrack.core.data.mapper.toEntity
import com.gifttrack.core.data.mapper.toModel
import com.gifttrack.core.domain.model.Shop
import com.gifttrack.core.domain.repository.ShopRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of ShopRepository using Room database.
 *
 * This class bridges the domain layer (repository interface)
 * and the data layer (Room database).
 */
class ShopRepositoryImpl @Inject constructor(
    private val shopDao: ShopDao
) : ShopRepository {

    override fun getShops(): Flow<List<Shop>> {
        return shopDao.getShops().map { entities ->
            entities.map { it.toModel() }
        }
    }

    override suspend fun getShopById(id: String): Shop? {
        return shopDao.getShopById(id)?.toModel()
    }

    override suspend fun getShopByName(name: String): Shop? {
        return shopDao.getShopByName(name)?.toModel()
    }

    override suspend fun insertShop(shop: Shop) {
        shopDao.insert(shop.toEntity())
    }

    override suspend fun insertShops(shops: List<Shop>) {
        shopDao.insertAll(shops.map { it.toEntity() })
    }

    override suspend fun updateShop(shop: Shop) {
        shopDao.update(shop.toEntity())
    }

    override suspend fun deleteShop(id: String) {
        shopDao.deleteById(id)
    }

    override suspend fun updateShopOrderCount(shopId: String, count: Int) {
        shopDao.updateOrderCount(shopId, count, System.currentTimeMillis())
    }

    override suspend fun deleteAllShops() {
        shopDao.deleteAll()
    }
}
