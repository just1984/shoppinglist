package com.gifttrack.feature.shops.ui

import com.gifttrack.core.domain.model.Shop

/**
 * Sealed class representing the UI state for the Shops list screen.
 */
sealed class ShopsUiState {
    /**
     * Loading state - data is being fetched.
     */
    data object Loading : ShopsUiState()

    /**
     * Success state - shops list is available.
     *
     * @param shops List of shops to display.
     */
    data class Success(val shops: List<Shop>) : ShopsUiState()

    /**
     * Empty state - no shops available.
     */
    data object Empty : ShopsUiState()

    /**
     * Error state - an error occurred while fetching shops.
     *
     * @param message Error message to display.
     */
    data class Error(val message: String) : ShopsUiState()
}
