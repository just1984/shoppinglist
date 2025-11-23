package com.gifttrack.feature.orders.ui

import com.gifttrack.core.domain.model.OrderStatus

/**
 * Data class representing order filter options.
 */
data class OrderFilter(
    val shopName: String? = null,
    val status: OrderStatus? = null,
    val isGift: Boolean? = null,
    val dateFrom: Long? = null,
    val dateTo: Long? = null
) {
    /**
     * Checks if any filter is active.
     */
    fun isActive(): Boolean {
        return shopName != null ||
                status != null ||
                isGift != null ||
                dateFrom != null ||
                dateTo != null
    }

    /**
     * Clears all filters.
     */
    fun clear(): OrderFilter {
        return OrderFilter()
    }
}

/**
 * Enum representing order sort options.
 */
enum class OrderSortOption {
    DATE_DESC,      // Newest first (default)
    DATE_ASC,       // Oldest first
    SHOP_NAME_ASC,  // Shop A-Z
    SHOP_NAME_DESC, // Shop Z-A
    AMOUNT_DESC,    // Highest amount first
    AMOUNT_ASC      // Lowest amount first
}

/**
 * Data class representing order sort state.
 */
data class OrderSort(
    val option: OrderSortOption = OrderSortOption.DATE_DESC
)
