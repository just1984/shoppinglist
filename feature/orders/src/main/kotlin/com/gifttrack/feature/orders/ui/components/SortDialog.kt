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
import com.gifttrack.feature.orders.ui.OrderSort
import com.gifttrack.feature.orders.ui.OrderSortOption

/**
 * Dialog for sorting orders.
 */
@Composable
fun SortDialog(
    currentSort: OrderSort,
    onApply: (OrderSort) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedOption by remember { mutableStateOf(currentSort.option) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Sortieren") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SortOption(
                    label = "Neueste zuerst",
                    isSelected = selectedOption == OrderSortOption.DATE_DESC,
                    onClick = { selectedOption = OrderSortOption.DATE_DESC }
                )
                SortOption(
                    label = "Älteste zuerst",
                    isSelected = selectedOption == OrderSortOption.DATE_ASC,
                    onClick = { selectedOption = OrderSortOption.DATE_ASC }
                )
                SortOption(
                    label = "Shop A-Z",
                    isSelected = selectedOption == OrderSortOption.SHOP_NAME_ASC,
                    onClick = { selectedOption = OrderSortOption.SHOP_NAME_ASC }
                )
                SortOption(
                    label = "Shop Z-A",
                    isSelected = selectedOption == OrderSortOption.SHOP_NAME_DESC,
                    onClick = { selectedOption = OrderSortOption.SHOP_NAME_DESC }
                )
                SortOption(
                    label = "Betrag aufsteigend",
                    isSelected = selectedOption == OrderSortOption.AMOUNT_ASC,
                    onClick = { selectedOption = OrderSortOption.AMOUNT_ASC }
                )
                SortOption(
                    label = "Betrag absteigend",
                    isSelected = selectedOption == OrderSortOption.AMOUNT_DESC,
                    onClick = { selectedOption = OrderSortOption.AMOUNT_DESC }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onApply(OrderSort(option = selectedOption))
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
private fun SortOption(
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
