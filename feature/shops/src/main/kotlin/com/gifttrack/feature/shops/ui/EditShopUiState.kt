package com.gifttrack.feature.shops.ui

/**
 * Sealed class representing the UI state for the Edit Shop screen.
 */
sealed class EditShopUiState {
    /**
     * Loading state - shop data is being loaded.
     */
    data object Loading : EditShopUiState()

    /**
     * Editing state - user is editing the shop.
     */
    data object Editing : EditShopUiState()

    /**
     * Saving state - shop changes are being saved.
     */
    data object Saving : EditShopUiState()

    /**
     * Success state - shop was updated successfully.
     */
    data object Success : EditShopUiState()

    /**
     * Error state - an error occurred.
     *
     * @param message Error message to display.
     */
    data class Error(val message: String) : EditShopUiState()
}
