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

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        loadOrders()
    }

    /**
     * Loads orders from the repository with search, filter, and sort applied.
     */
    private fun loadOrders() {
        viewModelScope.launch {
            combine(
                orderRepository.getOrders(),
                _searchQuery,
                _filter,
                _sort
            ) { orders, searchQuery, filter, sort ->
                listOf(orders, searchQuery, filter, sort)
            }
                .catch { exception ->
                    _uiState.value = OrdersUiState.Error(
                        message = exception.message ?: "Ein unbekannter Fehler ist aufgetreten"
                    )
                }
                .collect { data ->
                    @Suppress("UNCHECKED_CAST")
                    val orders = data[0] as List<Order>
                    val searchQuery = data[1] as String
                    val filter = data[2] as OrderFilter
                    val sort = data[3] as OrderSort

                    val searchedOrders = applySearch(orders, searchQuery)
                    val filteredOrders = applyFilter(searchedOrders, filter)
                    val sortedOrders = applySort(filteredOrders, sort)

                    _uiState.value = if (sortedOrders.isEmpty()) {
                        if (filter.isActive() || searchQuery.isNotBlank()) {
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
     * Applies search query to orders list.
     * Searches across order number, shop name, product name, and description.
     */
    private fun applySearch(orders: List<Order>, query: String): List<Order> {
        if (query.isBlank()) {
            return orders
        }

        val searchQuery = query.trim().lowercase()
        return orders.filter { order ->
            order.orderNumber.lowercase().contains(searchQuery) ||
                    order.shopName.lowercase().contains(searchQuery) ||
                    order.productName?.lowercase()?.contains(searchQuery) == true ||
                    order.productDescription?.lowercase()?.contains(searchQuery) == true ||
                    order.trackingNumber?.lowercase()?.contains(searchQuery) == true ||
                    order.carrierName?.lowercase()?.contains(searchQuery) == true
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
     * Updates the search query.
     */
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    /**
     * Clears the search query.
     */
    fun clearSearch() {
        _searchQuery.value = ""
    }

    /**
     * Refreshes the orders list.
     */
    fun refresh() {
        _uiState.value = OrdersUiState.Loading
        loadOrders()
    }
}
