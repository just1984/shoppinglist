package com.gifttrack.feature.orders.ui

import com.gifttrack.core.domain.model.Order

/**
 * UI state for the Orders screen.
 *
 * This sealed class represents all possible states of the Orders UI.
 */
sealed class OrdersUiState {
    /**
     * Loading state - data is being fetched.
     */
    data object Loading : OrdersUiState()

    /**
     * Success state - data is available.
     *
     * @param orders List of orders to display.
     */
    data class Success(val orders: List<Order>) : OrdersUiState()

    /**
     * Empty state - no orders available.
     */
    data object Empty : OrdersUiState()

    /**
     * Error state - something went wrong.
     *
     * @param message Error message to display.
     */
    data class Error(val message: String) : OrdersUiState()
}
