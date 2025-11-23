package com.gifttrack.feature.orders.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gifttrack.core.domain.model.Order
import com.gifttrack.core.domain.repository.OrderRepository
import com.gifttrack.feature.orders.ui.OrderFilter
import com.gifttrack.feature.orders.ui.OrderSort
import com.gifttrack.feature.orders.ui.OrderSortOption
import com.gifttrack.feature.orders.ui.OrdersUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
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

    private val _filter = MutableStateFlow(OrderFilter())
    val filter: StateFlow<OrderFilter> = _filter.asStateFlow()

    private val _sort = MutableStateFlow(OrderSort())
    val sort: StateFlow<OrderSort> = _sort.asStateFlow()

    init {
        loadOrders()
    }

    /**
     * Loads orders from the repository with filter and sort applied.
     */
    private fun loadOrders() {
        viewModelScope.launch {
            combine(
                orderRepository.getOrders(),
                _filter,
                _sort
            ) { orders, filter, sort ->
                Triple(orders, filter, sort)
            }
                .catch { exception ->
                    _uiState.value = OrdersUiState.Error(
                        message = exception.message ?: "Ein unbekannter Fehler ist aufgetreten"
                    )
                }
                .collect { (orders, filter, sort) ->
                    val filteredOrders = applyFilter(orders, filter)
                    val sortedOrders = applySort(filteredOrders, sort)

                    _uiState.value = if (sortedOrders.isEmpty()) {
                        if (filter.isActive()) {
                            OrdersUiState.Success(emptyList())
                        } else {
                            OrdersUiState.Empty
                        }
                    } else {
                        OrdersUiState.Success(sortedOrders)
                    }
                }
        }
    }

    /**
     * Applies filter to orders list.
     */
    private fun applyFilter(orders: List<Order>, filter: OrderFilter): List<Order> {
        var filtered = orders

        // Filter by shop name
        filter.shopName?.let { shopName ->
            filtered = filtered.filter { order ->
                order.shopName.contains(shopName, ignoreCase = true)
            }
        }

        // Filter by status
        filter.status?.let { status ->
            filtered = filtered.filter { order ->
                order.status == status
            }
        }

        // Filter by gift flag
        filter.isGift?.let { isGift ->
            filtered = filtered.filter { order ->
                order.isGift == isGift
            }
        }

        // Filter by date range
        filter.dateFrom?.let { dateFrom ->
            filtered = filtered.filter { order ->
                order.orderDate >= dateFrom
            }
        }

        filter.dateTo?.let { dateTo ->
            filtered = filtered.filter { order ->
                order.orderDate <= dateTo
            }
        }

        return filtered
    }

    /**
     * Applies sort to orders list.
     */
    private fun applySort(orders: List<Order>, sort: OrderSort): List<Order> {
        return when (sort.option) {
            OrderSortOption.DATE_DESC -> orders.sortedByDescending { it.orderDate }
            OrderSortOption.DATE_ASC -> orders.sortedBy { it.orderDate }
            OrderSortOption.SHOP_NAME_ASC -> orders.sortedBy { it.shopName.lowercase() }
            OrderSortOption.SHOP_NAME_DESC -> orders.sortedByDescending { it.shopName.lowercase() }
            OrderSortOption.AMOUNT_ASC -> orders.sortedBy { it.totalAmount ?: 0.0 }
            OrderSortOption.AMOUNT_DESC -> orders.sortedByDescending { it.totalAmount ?: 0.0 }
        }
    }

    /**
     * Updates the filter.
     */
    fun updateFilter(filter: OrderFilter) {
        _filter.value = filter
    }

    /**
     * Clears the filter.
     */
    fun clearFilter() {
        _filter.value = OrderFilter()
    }

    /**
     * Updates the sort option.
     */
    fun updateSort(sort: OrderSort) {
        _sort.value = sort
    }

    /**
     * Refreshes the orders list.
     */
    fun refresh() {
        _uiState.value = OrdersUiState.Loading
        loadOrders()
    }
}
