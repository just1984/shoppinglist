# US-012: Bestellungsübersicht UI erstellen

**Epic**: E02 - Bestellverwaltung
**Priority**: High
**Story Points**: 5
**Status**: Not Started

## User Story

Als **Nutzer** möchte ich **eine übersichtliche Liste aller meiner Bestellungen** sehen, damit ich einen schnellen Überblick über meine Käufe habe.

## Beschreibung

Implementierung der Hauptansicht der App: Eine scrollbare Liste aller Bestellungen mit Filterund Sortieroptionen. Die UI soll Material 3 Design verwenden und flüssig performen.

## Akzeptanzkriterien

- [ ] Liste zeigt alle Bestellungen chronologisch (neueste zuerst)
- [ ] Jedes Listen-Item zeigt: Produktbild, Name, Shop, Datum, Status
- [ ] Pull-to-Refresh funktioniert
- [ ] Empty State wird angezeigt wenn keine Bestellungen vorhanden
- [ ] FAB (Floating Action Button) zum Hinzufügen neuer Bestellungen
- [ ] Klick auf Bestellung öffnet Detailansicht
- [ ] Filter-Chip-Gruppe für Status-Filter (Alle, Bestellt, Versandt, Geliefert)
- [ ] Sortier-Optionen (Datum, Shop, Status)
- [ ] Suchleiste für Bestellnummer/Produktname
- [ ] Loading State während Daten laden
- [ ] Performant bei 100+ Bestellungen
- [ ] Responsive Layout (verschiedene Bildschirmgrößen)

## Technische Details

### Screen Composable

```kotlin
// feature/orders/src/main/kotlin/com/gifttrack/feature/orders/OrdersScreen.kt
package com.gifttrack.feature.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun OrdersRoute(
    onOrderClick: (String) -> Unit,
    onAddOrderClick: () -> Unit,
    viewModel: OrdersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    OrdersScreen(
        uiState = uiState,
        onOrderClick = onOrderClick,
        onAddOrderClick = onAddOrderClick,
        onRefresh = viewModel::refresh,
        onFilterChange = viewModel::setFilter,
        onSortChange = viewModel::setSort,
        onSearchQueryChange = viewModel::setSearchQuery
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun OrdersScreen(
    uiState: OrdersUiState,
    onOrderClick: (String) -> Unit,
    onAddOrderClick: () -> Unit,
    onRefresh: () -> Unit,
    onFilterChange: (OrderFilter) -> Unit,
    onSortChange: (OrderSort) -> Unit,
    onSearchQueryChange: (String) -> Unit
) {
    Scaffold(
        topBar = {
            OrdersTopBar(
                searchQuery = uiState.searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                onSortChange = onSortChange
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddOrderClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Order")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Filter Chips
            OrderFilterChips(
                selectedFilter = uiState.filter,
                onFilterChange = onFilterChange,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Content
            when {
                uiState.isLoading -> LoadingState()
                uiState.orders.isEmpty() -> EmptyState(onAddOrderClick)
                else -> OrdersList(
                    orders = uiState.orders,
                    onOrderClick = onOrderClick,
                    onRefresh = onRefresh,
                    isRefreshing = uiState.isRefreshing
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OrdersTopBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSortChange: (OrderSort) -> Unit
) {
    var showSearchBar by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text("Orders") },
        actions = {
            IconButton(onClick = { showSearchBar = !showSearchBar }) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
            IconButton(onClick = { showSortMenu = true }) {
                Icon(Icons.Default.Sort, contentDescription = "Sort")
            }
            // Sort Menu Dropdown
            DropdownMenu(
                expanded = showSortMenu,
                onDismissRequest = { showSortMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Date (Newest)") },
                    onClick = {
                        onSortChange(OrderSort.DATE_DESC)
                        showSortMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Date (Oldest)") },
                    onClick = {
                        onSortChange(OrderSort.DATE_ASC)
                        showSortMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Shop") },
                    onClick = {
                        onSortChange(OrderSort.SHOP)
                        showSortMenu = false
                    }
                )
            }
        }
    )

    if (showSearchBar) {
        SearchBar(
            query = searchQuery,
            onQueryChange = onSearchQueryChange,
            onSearch = { },
            active = showSearchBar,
            onActiveChange = { showSearchBar = it },
            placeholder = { Text("Search orders...") }
        ) {
            // Search suggestions could go here
        }
    }
}

@Composable
private fun OrderFilterChips(
    selectedFilter: OrderFilter,
    onFilterChange: (OrderFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedFilter == OrderFilter.ALL,
            onClick = { onFilterChange(OrderFilter.ALL) },
            label = { Text("All") }
        )
        FilterChip(
            selected = selectedFilter == OrderFilter.ORDERED,
            onClick = { onFilterChange(OrderFilter.ORDERED) },
            label = { Text("Ordered") }
        )
        FilterChip(
            selected = selectedFilter == OrderFilter.SHIPPED,
            onClick = { onFilterChange(OrderFilter.SHIPPED) },
            label = { Text("Shipped") }
        )
        FilterChip(
            selected = selectedFilter == OrderFilter.DELIVERED,
            onClick = { onFilterChange(OrderFilter.DELIVERED) },
            label = { Text("Delivered") }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OrdersList(
    orders: List<OrderUiModel>,
    onOrderClick: (String) -> Unit,
    onRefresh: () -> Unit,
    isRefreshing: Boolean
) {
    val pullRefreshState = rememberPullToRefreshState()

    if (pullRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            onRefresh()
        }
    }

    LaunchedEffect(isRefreshing) {
        if (!isRefreshing) {
            pullRefreshState.endRefresh()
        }
    }

    Box(modifier = Modifier.nestedScroll(pullRefreshState.nestedScrollConnection)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = orders,
                key = { it.id }
            ) { order ->
                OrderListItem(
                    order = order,
                    onClick = { onOrderClick(order.id) }
                )
            }
        }

        PullToRefreshContainer(
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
private fun OrderListItem(
    order: OrderUiModel,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Product Image
            AsyncImage(
                model = order.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = order.productName,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = order.shopName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = order.orderDate,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            OrderStatusBadge(status = order.status)
        }
    }
}

@Composable
private fun OrderStatusBadge(status: String) {
    val color = when (status) {
        "DELIVERED" -> MaterialTheme.colorScheme.primary
        "SHIPPED" -> MaterialTheme.colorScheme.secondary
        "ORDERED" -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    Surface(
        color = color,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = status,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
private fun EmptyState(onAddOrderClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingBag,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No orders yet",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "Add your first order to get started",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onAddOrderClick) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Order")
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
```

### ViewModel

```kotlin
// feature/orders/src/main/kotlin/com/gifttrack/feature/orders/OrdersViewModel.kt
package com.gifttrack.feature.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gifttrack.domain.usecase.GetOrdersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _filter = MutableStateFlow(OrderFilter.ALL)
    private val _sort = MutableStateFlow(OrderSort.DATE_DESC)
    private val _isRefreshing = MutableStateFlow(false)

    val uiState: StateFlow<OrdersUiState> = combine(
        getOrdersUseCase(),
        _searchQuery,
        _filter,
        _sort,
        _isRefreshing
    ) { orders, query, filter, sort, isRefreshing ->
        val filteredOrders = orders
            .filter { order ->
                when (filter) {
                    OrderFilter.ALL -> true
                    OrderFilter.ORDERED -> order.status == "ORDERED"
                    OrderFilter.SHIPPED -> order.status == "SHIPPED"
                    OrderFilter.DELIVERED -> order.status == "DELIVERED"
                }
            }
            .filter { order ->
                query.isEmpty() ||
                order.productName.contains(query, ignoreCase = true) ||
                order.orderNumber.contains(query, ignoreCase = true)
            }
            .let { list ->
                when (sort) {
                    OrderSort.DATE_DESC -> list.sortedByDescending { it.orderDate }
                    OrderSort.DATE_ASC -> list.sortedBy { it.orderDate }
                    OrderSort.SHOP -> list.sortedBy { it.shopName }
                }
            }

        OrdersUiState(
            orders = filteredOrders.map { it.toUiModel() },
            searchQuery = query,
            filter = filter,
            sort = sort,
            isLoading = false,
            isRefreshing = isRefreshing
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = OrdersUiState()
    )

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setFilter(filter: OrderFilter) {
        _filter.value = filter
    }

    fun setSort(sort: OrderSort) {
        _sort.value = sort
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            // Trigger refresh logic
            _isRefreshing.value = false
        }
    }
}

data class OrdersUiState(
    val orders: List<OrderUiModel> = emptyList(),
    val searchQuery: String = "",
    val filter: OrderFilter = OrderFilter.ALL,
    val sort: OrderSort = OrderSort.DATE_DESC,
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false
)

data class OrderUiModel(
    val id: String,
    val orderNumber: String,
    val productName: String,
    val shopName: String,
    val imageUrl: String?,
    val orderDate: String,
    val status: String
)

enum class OrderFilter { ALL, ORDERED, SHIPPED, DELIVERED }
enum class OrderSort { DATE_DESC, DATE_ASC, SHOP }
```

## Definition of Done

- [ ] UI ist implementiert und zeigt Bestellungen an
- [ ] Filter funktionieren korrekt
- [ ] Sortierung funktioniert
- [ ] Suchfunktion funktioniert
- [ ] Pull-to-Refresh funktioniert
- [ ] Empty State wird angezeigt
- [ ] Loading State wird angezeigt
- [ ] Performance bei 100+ Items ist gut
- [ ] UI Tests sind vorhanden
- [ ] Screenshot Tests (optional)
- [ ] Accessibility Labels sind gesetzt

## Dependencies

- US-010 (Database Schema)
- US-011 (Repository Implementation)

## Notizen

- Verwende Coil für Image Loading
- AsyncImage für Produktbilder
- LazyColumn für Performance
- Material 3 Components durchgehend nutzen
