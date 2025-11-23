package com.gifttrack.feature.orders.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gifttrack.core.domain.repository.OrderRepository
import com.gifttrack.feature.orders.ui.OrderDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Order Details screen.
 *
 * Manages loading and displaying a single order's details.
 */
@HiltViewModel
class OrderDetailsViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val orderId: String = checkNotNull(savedStateHandle["orderId"]) {
        "orderId is required for OrderDetailsViewModel"
    }

    private val _uiState = MutableStateFlow<OrderDetailsUiState>(OrderDetailsUiState.Loading)
    val uiState: StateFlow<OrderDetailsUiState> = _uiState.asStateFlow()

    init {
        loadOrderDetails()
    }

    /**
     * Loads order details from the repository.
     */
    private fun loadOrderDetails() {
        viewModelScope.launch {
            try {
                val order = orderRepository.getOrderById(orderId)
                if (order != null) {
                    _uiState.value = OrderDetailsUiState.Success(order)
                } else {
                    _uiState.value = OrderDetailsUiState.Error(
                        message = "Bestellung nicht gefunden"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = OrderDetailsUiState.Error(
                    message = e.message ?: "Fehler beim Laden der Bestellung"
                )
            }
        }
    }

    /**
     * Refreshes the order details.
     */
    fun refresh() {
        _uiState.value = OrderDetailsUiState.Loading
        loadOrderDetails()
    }
}
