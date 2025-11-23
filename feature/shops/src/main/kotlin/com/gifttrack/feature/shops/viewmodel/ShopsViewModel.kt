package com.gifttrack.feature.shops.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gifttrack.core.domain.repository.ShopRepository
import com.gifttrack.feature.shops.ui.ShopsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Shops screen.
 *
 * Manages the list of all shops and provides operations like refresh.
 */
@HiltViewModel
class ShopsViewModel @Inject constructor(
    private val shopRepository: ShopRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ShopsUiState>(ShopsUiState.Loading)
    val uiState: StateFlow<ShopsUiState> = _uiState.asStateFlow()

    init {
        loadShops()
    }

    /**
     * Loads shops from the repository.
     */
    private fun loadShops() {
        viewModelScope.launch {
            shopRepository.getShops()
                .catch { e ->
                    _uiState.value = ShopsUiState.Error(
                        message = e.message ?: "Fehler beim Laden der Shops"
                    )
                }
                .collect { shops ->
                    _uiState.value = if (shops.isEmpty()) {
                        ShopsUiState.Empty
                    } else {
                        ShopsUiState.Success(shops)
                    }
                }
        }
    }

    /**
     * Refreshes the shops list.
     */
    fun refresh() {
        _uiState.value = ShopsUiState.Loading
        loadShops()
    }
}
