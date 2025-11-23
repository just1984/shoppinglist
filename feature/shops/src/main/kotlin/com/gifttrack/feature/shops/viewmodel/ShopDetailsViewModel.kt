package com.gifttrack.feature.shops.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gifttrack.core.domain.repository.ShopRepository
import com.gifttrack.feature.shops.ui.ShopDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Shop Details screen.
 *
 * Manages loading and displaying a single shop's details.
 */
@HiltViewModel
class ShopDetailsViewModel @Inject constructor(
    private val shopRepository: ShopRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val shopId: String = checkNotNull(savedStateHandle["shopId"]) {
        "shopId is required for ShopDetailsViewModel"
    }

    private val _uiState = MutableStateFlow<ShopDetailsUiState>(ShopDetailsUiState.Loading)
    val uiState: StateFlow<ShopDetailsUiState> = _uiState.asStateFlow()

    init {
        loadShopDetails()
    }

    /**
     * Loads shop details from the repository.
     */
    private fun loadShopDetails() {
        viewModelScope.launch {
            try {
                val shop = shopRepository.getShopById(shopId)
                if (shop != null) {
                    _uiState.value = ShopDetailsUiState.Success(shop)
                } else {
                    _uiState.value = ShopDetailsUiState.Error(
                        message = "Shop nicht gefunden"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = ShopDetailsUiState.Error(
                    message = e.message ?: "Fehler beim Laden des Shops"
                )
            }
        }
    }

    /**
     * Refreshes the shop details.
     */
    fun refresh() {
        _uiState.value = ShopDetailsUiState.Loading
        loadShopDetails()
    }

    /**
     * Deletes the current shop.
     */
    fun deleteShop() {
        _uiState.value = ShopDetailsUiState.Deleting

        viewModelScope.launch {
            try {
                shopRepository.deleteShop(shopId)
                _uiState.value = ShopDetailsUiState.Deleted
            } catch (e: Exception) {
                _uiState.value = ShopDetailsUiState.Error(
                    message = e.message ?: "Fehler beim Löschen des Shops"
                )
            }
        }
    }

    /**
     * Resets the UI state to the current loaded shop after an error.
     */
    fun resetUiState() {
        loadShopDetails()
    }
}
