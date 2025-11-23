package com.gifttrack.feature.orders.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gifttrack.feature.orders.viewmodel.AddOrderViewModel

/**
 * Screen for adding a new order manually.
 *
 * @param onNavigateBack Callback when user navigates back.
 * @param viewModel ViewModel for managing add order state.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrderScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddOrderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle success state
    LaunchedEffect(uiState) {
        when (uiState) {
            is AddOrderUiState.Success -> {
                snackbarHostState.showSnackbar("Bestellung erfolgreich hinzugefügt")
                viewModel.clearForm()
                onNavigateBack()
            }
            is AddOrderUiState.Error -> {
                val errorMessage = (uiState as AddOrderUiState.Error).message
                snackbarHostState.showSnackbar(errorMessage)
                viewModel.resetUiState()
            }
            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Neue Bestellung") },
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
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (uiState) {
                is AddOrderUiState.Saving -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    AddOrderForm(
                        formState = formState,
                        onOrderNumberChange = viewModel::updateOrderNumber,
                        onShopNameChange = viewModel::updateShopName,
                        onProductNameChange = viewModel::updateProductName,
                        onProductDescriptionChange = viewModel::updateProductDescription,
                        onTotalAmountChange = viewModel::updateTotalAmount,
                        onCurrencyChange = viewModel::updateCurrency,
                        onCarrierNameChange = viewModel::updateCarrierName,
                        onTrackingNumberChange = viewModel::updateTrackingNumber,
                        onIsGiftChange = viewModel::updateIsGift,
                        onSaveClick = viewModel::saveOrder,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

/**
 * Form content for adding an order.
 */
@Composable
private fun AddOrderForm(
    formState: AddOrderFormState,
    onOrderNumberChange: (String) -> Unit,
    onShopNameChange: (String) -> Unit,
    onProductNameChange: (String) -> Unit,
    onProductDescriptionChange: (String) -> Unit,
    onTotalAmountChange: (String) -> Unit,
    onCurrencyChange: (String) -> Unit,
    onCarrierNameChange: (String) -> Unit,
    onTrackingNumberChange: (String) -> Unit,
    onIsGiftChange: (Boolean) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Required fields section
        Text(
            text = "Pflichtfelder",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        OutlinedTextField(
            value = formState.orderNumber,
            onValueChange = onOrderNumberChange,
            label = { Text("Bestellnummer *") },
            isError = formState.orderNumberError != null,
            supportingText = formState.orderNumberError?.let { { Text(it) } },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = formState.shopName,
            onValueChange = onShopNameChange,
            label = { Text("Shop *") },
            isError = formState.shopNameError != null,
            supportingText = formState.shopNameError?.let { { Text(it) } },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Optional fields section
        Text(
            text = "Produktinformationen",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        OutlinedTextField(
            value = formState.productName,
            onValueChange = onProductNameChange,
            label = { Text("Produktname") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = formState.productDescription,
            onValueChange = onProductDescriptionChange,
            label = { Text("Produktbeschreibung") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5
        )

        // Price section
        Text(
            text = "Preis",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = formState.totalAmount,
                onValueChange = onTotalAmountChange,
                label = { Text("Betrag") },
                isError = formState.totalAmountError != null,
                supportingText = formState.totalAmountError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.weight(2f),
                singleLine = true
            )

            OutlinedTextField(
                value = formState.currency,
                onValueChange = onCurrencyChange,
                label = { Text("Währung") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
        }

        // Shipping section
        Text(
            text = "Versandinformationen",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        OutlinedTextField(
            value = formState.carrierName,
            onValueChange = onCarrierNameChange,
            label = { Text("Versanddienstleister") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = formState.trackingNumber,
            onValueChange = onTrackingNumberChange,
            label = { Text("Sendungsnummer") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Gift checkbox
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = formState.isGift,
                onCheckedChange = onIsGiftChange
            )
            Text(
                text = "Dies ist ein Geschenk",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        // Save button
        Button(
            onClick = onSaveClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Bestellung speichern")
        }
    }
}
