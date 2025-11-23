package com.gifttrack.feature.orders.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gifttrack.core.domain.repository.OrderRepository
import com.gifttrack.feature.orders.ui.OrdersUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Orders screen.
 *
 * Manages the UI state and business logic for displaying orders.
 */
@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<OrdersUiState>(OrdersUiState.Loading)
    val uiState: StateFlow<OrdersUiState> = _uiState.asStateFlow()

    init {
        loadOrders()
    }

    /**
     * Loads orders from the repository.
     */
    private fun loadOrders() {
        viewModelScope.launch {
            orderRepository.getOrders()
                .catch { exception ->
                    _uiState.value = OrdersUiState.Error(
                        message = exception.message ?: "Ein unbekannter Fehler ist aufgetreten"
                    )
                }
                .collect { orders ->
                    _uiState.value = if (orders.isEmpty()) {
                        OrdersUiState.Empty
                    } else {
                        OrdersUiState.Success(orders)
                    }
                }
        }
    }

    /**
     * Refreshes the orders list.
     */
    fun refresh() {
        _uiState.value = OrdersUiState.Loading
        loadOrders()
    }
}
