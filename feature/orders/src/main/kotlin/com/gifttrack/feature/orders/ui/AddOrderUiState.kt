package com.gifttrack.feature.orders.ui

/**
 * UI state for the Add Order screen.
 *
 * This sealed class represents all possible states when adding a new order.
 */
sealed class AddOrderUiState {
    /**
     * Idle state - form is ready for input.
     */
    data object Idle : AddOrderUiState()

    /**
     * Saving state - order is being saved to database.
     */
    data object Saving : AddOrderUiState()

    /**
     * Success state - order was saved successfully.
     */
    data object Success : AddOrderUiState()

    /**
     * Error state - something went wrong while saving.
     *
     * @param message Error message to display.
     */
    data class Error(val message: String) : AddOrderUiState()
}

/**
 * Data class representing the form state for adding an order.
 */
data class AddOrderFormState(
    val orderNumber: String = "",
    val shopName: String = "",
    val productName: String = "",
    val productDescription: String = "",
    val totalAmount: String = "",
    val currency: String = "EUR",
    val carrierName: String = "",
    val trackingNumber: String = "",
    val isGift: Boolean = false,
    val orderNumberError: String? = null,
    val shopNameError: String? = null,
    val totalAmountError: String? = null
)
