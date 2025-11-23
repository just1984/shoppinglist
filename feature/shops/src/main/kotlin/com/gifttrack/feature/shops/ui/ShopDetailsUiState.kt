package com.gifttrack.feature.shops.ui

import com.gifttrack.core.domain.model.Shop

/**
 * Sealed class representing the UI state for the Shop Details screen.
 */
sealed class ShopDetailsUiState {
    /**
     * Loading state - shop details are being loaded.
     */
    data object Loading : ShopDetailsUiState()

    /**
     * Success state - shop data is available.
     *
     * @param shop The shop to display.
     */
    data class Success(val shop: Shop) : ShopDetailsUiState()

    /**
     * Deleting state - shop is being deleted.
     */
    data object Deleting : ShopDetailsUiState()

    /**
     * Deleted state - shop was deleted successfully.
     */
    data object Deleted : ShopDetailsUiState()

    /**
     * Error state - shop not found or error occurred.
     *
     * @param message Error message to display.
     */
    data class Error(val message: String) : ShopDetailsUiState()
}
