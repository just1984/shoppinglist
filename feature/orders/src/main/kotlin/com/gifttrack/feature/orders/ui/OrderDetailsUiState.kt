package com.gifttrack.feature.orders.ui

import com.gifttrack.core.domain.model.Order

/**
 * UI state for the Order Details screen.
 *
 * This sealed class represents all possible states when viewing order details.
 */
sealed class OrderDetailsUiState {
    /**
     * Loading state - order data is being fetched.
     */
    data object Loading : OrderDetailsUiState()

    /**
     * Success state - order data is available.
     *
     * @param order The order to display.
     */
    data class Success(val order: Order) : OrderDetailsUiState()

    /**
     * Deleting state - order is being deleted.
     */
    data object Deleting : OrderDetailsUiState()

    /**
     * Deleted state - order was deleted successfully.
     */
    data object Deleted : OrderDetailsUiState()

    /**
     * Error state - order not found or error occurred.
     *
     * @param message Error message to display.
     */
    data class Error(val message: String) : OrderDetailsUiState()
}
