package com.gifttrack.feature.shops.ui

/**
 * Sealed class representing the UI state for the Add Shop screen.
 */
sealed class AddShopUiState {
    /**
     * Idle state - user is filling the form.
     */
    data object Idle : AddShopUiState()

    /**
     * Saving state - shop is being saved.
     */
    data object Saving : AddShopUiState()

    /**
     * Success state - shop was saved successfully.
     */
    data object Success : AddShopUiState()

    /**
     * Error state - an error occurred while saving the shop.
     *
     * @param message Error message to display.
     */
    data class Error(val message: String) : AddShopUiState()
}

/**
 * Data class representing the form state for adding a shop.
 */
data class ShopFormState(
    val name: String = "",
    val url: String = "",
    val logoUrl: String = "",
    val color: String = "",
    val nameError: String? = null,
    val urlError: String? = null
)
