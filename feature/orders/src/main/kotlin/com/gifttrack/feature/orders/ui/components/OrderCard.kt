package com.gifttrack.feature.orders.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gifttrack.core.domain.model.Order
import com.gifttrack.core.domain.model.OrderStatus
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Card component for displaying an order.
 *
 * @param order The order to display.
 * @param onClick Callback when the card is clicked.
 * @param modifier Modifier for the card.
 */
@Composable
fun OrderCard(
    order: Order,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
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
            // Header: Shop name and status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = order.shopName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                OrderStatusBadge(status = order.status)
            }

            // Product name
            order.productName?.let { productName ->
                Text(
                    text = productName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Order number and date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Bestellung: ${order.orderNumber}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = formatDate(order.orderDate),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Price
            order.totalAmount?.let { amount ->
                Text(
                    text = "%.2f %s".format(amount, order.currency),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

/**
 * Badge component for displaying order status.
 *
 * @param status The order status to display.
 */
@Composable
private fun OrderStatusBadge(status: OrderStatus) {
    val (text, color) = when (status) {
        OrderStatus.ORDERED -> "Bestellt" to MaterialTheme.colorScheme.secondary
        OrderStatus.PROCESSING -> "In Bearbeitung" to MaterialTheme.colorScheme.tertiary
        OrderStatus.SHIPPED -> "Versendet" to MaterialTheme.colorScheme.primary
        OrderStatus.IN_TRANSIT -> "Unterwegs" to MaterialTheme.colorScheme.primary
        OrderStatus.OUT_FOR_DELIVERY -> "Wird zugestellt" to MaterialTheme.colorScheme.primary
        OrderStatus.DELIVERED -> "Zugestellt" to MaterialTheme.colorScheme.tertiary
        OrderStatus.CANCELLED -> "Storniert" to MaterialTheme.colorScheme.error
        OrderStatus.RETURNED -> "Zurückgeschickt" to MaterialTheme.colorScheme.error
    }

    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = color,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

/**
 * Formats a timestamp to a readable date string.
 */
private fun formatDate(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)
    return formatter.format(date)
}
