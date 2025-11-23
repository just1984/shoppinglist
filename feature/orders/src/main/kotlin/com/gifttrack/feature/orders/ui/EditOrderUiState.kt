package com.gifttrack.feature.orders.ui

/**
 * UI state for the Edit Order screen.
 *
 * This sealed class represents all possible states when editing an order.
 */
sealed class EditOrderUiState {
    /**
     * Loading state - order data is being loaded.
     */
    data object Loading : EditOrderUiState()

    /**
     * Editing state - form is ready for editing.
     */
    data object Editing : EditOrderUiState()

    /**
     * Saving state - changes are being saved to database.
     */
    data object Saving : EditOrderUiState()

    /**
     * Success state - order was updated successfully.
     */
    data object Success : EditOrderUiState()

    /**
     * Error state - something went wrong.
     *
     * @param message Error message to display.
     */
    data class Error(val message: String) : EditOrderUiState()
}
