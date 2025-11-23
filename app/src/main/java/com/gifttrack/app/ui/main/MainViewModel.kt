package com.gifttrack.app.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gifttrack.core.domain.model.Order
import com.gifttrack.core.domain.usecase.GetOrdersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Main ViewModel demonstrating Hilt dependency injection.
 *
 * This ViewModel automatically receives the GetOrdersUseCase
 * through constructor injection via Hilt.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        loadOrders()
    }

    private fun loadOrders() {
        viewModelScope.launch {
            _uiState.value = MainUiState.Loading
            try {
                getOrdersUseCase().collect { orders ->
                    _uiState.value = if (orders.isEmpty()) {
                        MainUiState.Empty
                    } else {
                        MainUiState.Success(orders)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = MainUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

/**
 * UI state for the main screen.
 */
sealed class MainUiState {
    object Loading : MainUiState()
    object Empty : MainUiState()
    data class Success(val orders: List<Order>) : MainUiState()
    data class Error(val message: String) : MainUiState()
}
