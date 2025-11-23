package com.gifttrack.feature.orders.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gifttrack.core.domain.model.OrderStatus
import com.gifttrack.feature.orders.ui.OrderFilter

/**
 * Dialog for filtering orders.
 */
@Composable
fun FilterDialog(
    currentFilter: OrderFilter,
    onApply: (OrderFilter) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var shopName by remember { mutableStateOf(currentFilter.shopName ?: "") }
    var selectedStatus by remember { mutableStateOf(currentFilter.status) }
    var isGift by remember { mutableStateOf(currentFilter.isGift) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filter") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Shop Name Filter
                Text(
                    text = "Shop",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                OutlinedTextField(
                    value = shopName,
                    onValueChange = { shopName = it },
                    label = { Text("Shop-Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                HorizontalDivider()

                // Status Filter
                Text(
                    text = "Status",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    StatusFilterItem(
                        label = "Alle",
                        isSelected = selectedStatus == null,
                        onClick = { selectedStatus = null }
                    )
                    OrderStatus.values().forEach { status ->
                        StatusFilterItem(
                            label = getStatusLabel(status),
                            isSelected = selectedStatus == status,
                            onClick = { selectedStatus = status }
                        )
                    }
                }

                HorizontalDivider()

                // Gift Filter
                Text(
                    text = "Geschenk",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    GiftFilterItem(
                        label = "Alle",
                        isSelected = isGift == null,
                        onClick = { isGift = null }
                    )
                    GiftFilterItem(
                        label = "Nur Geschenke",
                        isSelected = isGift == true,
                        onClick = { isGift = true }
                    )
                    GiftFilterItem(
                        label = "Keine Geschenke",
                        isSelected = isGift == false,
                        onClick = { isGift = false }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val newFilter = OrderFilter(
                        shopName = shopName.ifBlank { null },
                        status = selectedStatus,
                        isGift = isGift
                    )
                    onApply(newFilter)
                    onDismiss()
                }
            ) {
                Text("Anwenden")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Abbrechen")
            }
        },
        modifier = modifier
    )
}

@Composable
private fun StatusFilterItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = { onClick() }
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun GiftFilterItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = { onClick() }
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

/**
 * Returns a localized label for the given OrderStatus.
 */
private fun getStatusLabel(status: OrderStatus): String {
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
