package com.gifttrack.feature.orders.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.gifttrack.core.domain.model.Order
import com.gifttrack.core.domain.model.OrderStatus
import com.gifttrack.feature.orders.viewmodel.OrderDetailsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Screen for displaying detailed information about an order.
 *
 * @param onNavigateBack Callback when user navigates back.
 * @param onNavigateToEdit Callback when user wants to edit the order.
 * @param modifier Modifier for the screen.
 * @param viewModel ViewModel for managing order details state.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToEdit: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OrderDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bestellungsdetails") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Zurück"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (uiState is OrderDetailsUiState.Success) {
                FloatingActionButton(
                    onClick = onNavigateToEdit,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Bestellung bearbeiten"
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val state = uiState) {
                is OrderDetailsUiState.Loading -> {
                    LoadingState()
                }

                is OrderDetailsUiState.Success -> {
                    OrderDetailsContent(
                        order = state.order,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is OrderDetailsUiState.Error -> {
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
 * Content displaying order details.
 */
@Composable
private fun OrderDetailsContent(
    order: Order,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Status Badge
        OrderStatusCard(order = order)

        // Basic Information
        InfoSection(title = "Allgemeine Informationen") {
            InfoRow(label = "Bestellnummer", value = order.orderNumber)
            InfoRow(label = "Shop", value = order.shopName)
            InfoRow(
                label = "Bestelldatum",
                value = formatDate(order.orderDate)
            )
        }

        // Product Information
        if (order.productName != null || order.productDescription != null) {
            InfoSection(title = "Produktinformationen") {
                order.productName?.let { InfoRow(label = "Produktname", value = it) }
                order.productDescription?.let {
                    InfoRow(label = "Beschreibung", value = it)
                }
            }
        }

        // Price Information
        if (order.totalAmount != null) {
            InfoSection(title = "Preis") {
                InfoRow(
                    label = "Betrag",
                    value = "%.2f %s".format(order.totalAmount, order.currency)
                )
            }
        }

        // Shipping Information
        if (order.carrierName != null || order.trackingNumber != null ||
            order.estimatedDelivery != null || order.actualDelivery != null
        ) {
            InfoSection(title = "Versandinformationen") {
                order.carrierName?.let { InfoRow(label = "Versanddienstleister", value = it) }
                order.trackingNumber?.let { InfoRow(label = "Sendungsnummer", value = it) }
                order.estimatedDelivery?.let {
                    InfoRow(label = "Voraussichtliche Lieferung", value = formatDate(it))
                }
                order.actualDelivery?.let {
                    InfoRow(label = "Zugestellt am", value = formatDate(it))
                }
            }
        }

        // Recipient Information
        if (order.recipientName != null) {
            InfoSection(title = "Empfänger") {
                InfoRow(label = "Name", value = order.recipientName)
                if (order.isGift) {
                    InfoRow(label = "Geschenk", value = "Ja")
                }
            }
        } else if (order.isGift) {
            InfoSection(title = "Geschenk") {
                InfoRow(label = "Dies ist ein Geschenk", value = "Ja")
            }
        }

        // Notes
        if (!order.notes.isNullOrBlank()) {
            InfoSection(title = "Notizen") {
                Text(
                    text = order.notes,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Metadata
        InfoSection(title = "Weitere Informationen") {
            InfoRow(
                label = "Erstellt am",
                value = formatDateTime(order.createdAt)
            )
            InfoRow(
                label = "Zuletzt aktualisiert",
                value = formatDateTime(order.updatedAt)
            )
        }
    }
}

/**
 * Card displaying order status.
 */
@Composable
private fun OrderStatusCard(order: Order) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = getStatusColor(order.status)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = getStatusText(order.status),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

/**
 * Section with title and content.
 */
@Composable
private fun InfoSection(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            content()
        }
    }
}

/**
 * Row displaying label and value.
 */
@Composable
private fun InfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1.5f),
            textAlign = TextAlign.End
        )
    }
}

/**
 * Formats a timestamp to a readable date string.
 */
private fun formatDate(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)
    return formatter.format(date)
}

/**
 * Formats a timestamp to a readable date and time string.
 */
private fun formatDateTime(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMANY)
    return formatter.format(date)
}

/**
 * Gets the display text for an order status.
 */
private fun getStatusText(status: OrderStatus): String {
    return when (status) {
        OrderStatus.ORDERED -> "Bestellt"
        OrderStatus.PROCESSING -> "In Bearbeitung"
        OrderStatus.SHIPPED -> "Versendet"
        OrderStatus.IN_TRANSIT -> "Unterwegs"
        OrderStatus.OUT_FOR_DELIVERY -> "Wird zugestellt"
        OrderStatus.DELIVERED -> "Zugestellt"
        OrderStatus.CANCELLED -> "Storniert"
        OrderStatus.RETURNED -> "Zurückgeschickt"
    }
}

/**
 * Gets the color for an order status.
 */
@Composable
private fun getStatusColor(status: OrderStatus) = when (status) {
    OrderStatus.ORDERED -> MaterialTheme.colorScheme.secondary
    OrderStatus.PROCESSING -> MaterialTheme.colorScheme.tertiary
    OrderStatus.SHIPPED, OrderStatus.IN_TRANSIT, OrderStatus.OUT_FOR_DELIVERY ->
        MaterialTheme.colorScheme.primary
    OrderStatus.DELIVERED -> MaterialTheme.colorScheme.tertiary
    OrderStatus.CANCELLED, OrderStatus.RETURNED -> MaterialTheme.colorScheme.error
}
