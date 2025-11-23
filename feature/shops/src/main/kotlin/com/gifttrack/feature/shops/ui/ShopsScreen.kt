package com.gifttrack.feature.shops.ui

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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gifttrack.feature.shops.ui.components.ShopCard
import com.gifttrack.feature.shops.viewmodel.ShopsViewModel

/**
 * Screen for displaying the list of all shops.
 *
 * @param onNavigateToShopDetails Callback when user navigates to shop details.
 * @param onNavigateToAddShop Callback when user wants to add a new shop.
 * @param modifier Modifier for the screen.
 * @param viewModel ViewModel for managing shops state.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopsScreen(
    onNavigateToShopDetails: (String) -> Unit,
    onNavigateToAddShop: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ShopsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shops") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddShop,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Shop hinzufügen"
                )
            }
        }
    ) { padding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val state = uiState) {
                is ShopsUiState.Loading -> {
                    LoadingState()
                }

                is ShopsUiState.Success -> {
                    ShopsListContent(
                        shops = state.shops,
                        onShopClick = onNavigateToShopDetails
                    )
                }

                is ShopsUiState.Empty -> {
                    EmptyState(onAddShopClick = onNavigateToAddShop)
                }

                is ShopsUiState.Error -> {
                    ErrorState(
                        message = state.message,
                        onRetry = viewModel::refresh
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
 * Empty state - shows message when no shops are available.
 */
@Composable
private fun EmptyState(onAddShopClick: () -> Unit) {
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
                text = "🏪",
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = "Keine Shops vorhanden",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Fügen Sie Ihren ersten Shop hinzu, um Bestellungen zu organisieren.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Button(
                onClick = onAddShopClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Shop hinzufügen")
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

/**
 * Success state - shows list of shops.
 */
@Composable
private fun ShopsListContent(
    shops: List<com.gifttrack.core.domain.model.Shop>,
    onShopClick: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = shops,
            key = { it.id }
        ) { shop ->
            ShopCard(
                shop = shop,
                onClick = { onShopClick(shop.id) }
            )
        }
    }
}
