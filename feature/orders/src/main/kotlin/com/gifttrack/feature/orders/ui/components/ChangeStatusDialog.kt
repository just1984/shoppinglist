package com.gifttrack.feature.orders.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
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

/**
 * Dialog for changing order status.
 */
@Composable
fun ChangeStatusDialog(
    currentStatus: OrderStatus,
    onStatusChange: (OrderStatus) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedStatus by remember { mutableStateOf(currentStatus) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Status ändern") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OrderStatus.values().forEach { status ->
                    StatusOption(
                        label = getStatusLabel(status),
                        isSelected = selectedStatus == status,
                        onClick = { selectedStatus = status }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onStatusChange(selectedStatus)
                    onDismiss()
                }
            ) {
                Text("Ändern")
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
private fun StatusOption(
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
        RadioButton(
            selected = isSelected,
            onClick = onClick
        )
        Text(
            text = label,
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
