package com.gifttrack.feature.orders.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gifttrack.feature.orders.ui.components.FilterDialog
import com.gifttrack.feature.orders.ui.components.OrderCard
import com.gifttrack.feature.orders.ui.components.SortDialog
import com.gifttrack.feature.orders.viewmodel.OrdersViewModel

/**
 * Orders screen - displays list of orders.
 *
 * This screen shows all orders with different states:
 * - Loading: Shows loading indicator
 * - Empty: Shows empty state message
 * - Success: Shows list of orders in LazyColumn
 * - Error: Shows error message with retry button
 *
 * @param onNavigateToAddOrder Callback when user wants to add a new order.
 * @param onNavigateToOrderDetails Callback when user clicks on an order card.
 * @param modifier Modifier for the screen.
 * @param viewModel ViewModel for managing orders state.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    onNavigateToAddOrder: () -> Unit,
    onNavigateToOrderDetails: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OrdersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val filter by viewModel.filter.collectAsStateWithLifecycle()
    val sort by viewModel.sort.collectAsStateWithLifecycle()

    var showFilterDialog by remember { mutableStateOf(false) }
    var showSortDialog by remember { mutableStateOf(false) }

    if (showFilterDialog) {
        FilterDialog(
            currentFilter = filter,
            onApply = viewModel::updateFilter,
            onDismiss = { showFilterDialog = false }
        )
    }

    if (showSortDialog) {
        SortDialog(
            currentSort = sort,
            onApply = viewModel::updateSort,
            onDismiss = { showSortDialog = false }
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Bestellungen") },
                actions = {
                    // Sort button
                    IconButton(onClick = { showSortDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Sort,
                            contentDescription = "Sortieren"
                        )
                    }

                    // Filter button with badge
                    IconButton(onClick = { showFilterDialog = true }) {
                        BadgedBox(
                            badge = {
                                if (filter.isActive()) {
                                    Badge()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Filtern"
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddOrder,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Bestellung hinzufügen"
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is OrdersUiState.Loading -> {
                    LoadingState()
                }

                is OrdersUiState.Empty -> {
                    EmptyState()
                }

                is OrdersUiState.Success -> {
                    SuccessState(
                        orders = state.orders,
                        onOrderClick = onNavigateToOrderDetails,
                        hasActiveFilter = filter.isActive()
                    )
                }

                is OrdersUiState.Error -> {
                    ErrorState(
                        message = state.message,
                        onRetry = { viewModel.refresh() }
                    )
                }
            }
        }
    }
}

/**
 * Loading state - shows circular progress indicator.
 */
@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary
        )
    }
}

/**
 * Empty state - shows message when no orders are available.
 */
@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "📦",
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = "Keine Bestellungen",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Du hast noch keine Bestellungen hinzugefügt.\nFüge deine erste Bestellung hinzu!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Success state - shows list of orders.
 */
@Composable
private fun SuccessState(
    orders: List<com.gifttrack.core.domain.model.Order>,
    onOrderClick: (String) -> Unit,
    hasActiveFilter: Boolean
) {
    if (orders.isEmpty() && hasActiveFilter) {
        // Show empty filter result
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(32.dp)
            ) {
                Text(
                    text = "🔍",
                    style = MaterialTheme.typography.displayLarge
                )
                Text(
                    text = "Keine Ergebnisse",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Mit den aktuellen Filtern wurden keine Bestellungen gefunden.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = orders,
                key = { order -> order.id }
            ) { order ->
                OrderCard(
                    order = order,
                    onClick = { onOrderClick(order.id) }
                )
            }
        }
    }
}

/**
 * Error state - shows error message with retry button.
 */
@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "⚠️",
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = "Fehler",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Button(
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Erneut versuchen")
            }
        }
    }
}
