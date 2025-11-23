package com.gifttrack.feature.shops.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gifttrack.feature.shops.viewmodel.AddShopViewModel

/**
 * Screen for adding a new shop.
 *
 * @param onNavigateBack Callback when user navigates back.
 * @param viewModel ViewModel for managing add shop state.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddShopScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddShopViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle success state
    LaunchedEffect(uiState) {
        when (uiState) {
            is AddShopUiState.Success -> {
                snackbarHostState.showSnackbar("Shop erfolgreich hinzugefügt")
                viewModel.clearForm()
                onNavigateBack()
            }
            is AddShopUiState.Error -> {
                val errorMessage = (uiState as AddShopUiState.Error).message
                snackbarHostState.showSnackbar(errorMessage)
                viewModel.resetUiState()
            }
            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Neuer Shop") },
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
                is AddShopUiState.Saving -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    AddShopForm(
                        formState = formState,
                        onNameChange = viewModel::updateName,
                        onUrlChange = viewModel::updateUrl,
                        onLogoUrlChange = viewModel::updateLogoUrl,
                        onColorChange = viewModel::updateColor,
                        onSaveClick = viewModel::saveShop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

/**
 * Form content for adding a shop.
 */
@Composable
private fun AddShopForm(
    formState: ShopFormState,
    onNameChange: (String) -> Unit,
    onUrlChange: (String) -> Unit,
    onLogoUrlChange: (String) -> Unit,
    onColorChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Required fields
        Text(
            text = "Pflichtfelder",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        OutlinedTextField(
            value = formState.name,
            onValueChange = onNameChange,
            label = { Text("Shop-Name *") },
            isError = formState.nameError != null,
            supportingText = formState.nameError?.let { { Text(it) } },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Optional fields
        Text(
            text = "Optionale Informationen",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        OutlinedTextField(
            value = formState.url,
            onValueChange = onUrlChange,
            label = { Text("Website-URL") },
            isError = formState.urlError != null,
            supportingText = formState.urlError?.let { { Text(it) } },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text("https://www.example.com") }
        )

        OutlinedTextField(
            value = formState.logoUrl,
            onValueChange = onLogoUrlChange,
            label = { Text("Logo-URL") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = formState.color,
            onValueChange = onColorChange,
            label = { Text("Farbe (Hex)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text("#FF5722") }
        )

        // Save button
        Button(
            onClick = onSaveClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Shop speichern")
        }
    }
}
