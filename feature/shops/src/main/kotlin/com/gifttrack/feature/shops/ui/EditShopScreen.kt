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
import com.gifttrack.feature.shops.viewmodel.EditShopViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditShopScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditShopViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        when (uiState) {
            is EditShopUiState.Success -> {
                snackbarHostState.showSnackbar("Änderungen erfolgreich gespeichert")
                onNavigateBack()
            }
            is EditShopUiState.Error -> {
                val errorMessage = (uiState as EditShopUiState.Error).message
                snackbarHostState.showSnackbar(errorMessage)
                viewModel.resetUiState()
            }
            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shop bearbeiten") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Zurück")
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
                is EditShopUiState.Loading, is EditShopUiState.Saving -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is EditShopUiState.Editing, is EditShopUiState.Error -> {
                    EditShopForm(
                        formState = formState,
                        onNameChange = viewModel::updateName,
                        onUrlChange = viewModel::updateUrl,
                        onLogoUrlChange = viewModel::updateLogoUrl,
                        onColorChange = viewModel::updateColor,
                        onSaveClick = viewModel::saveShop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                else -> Unit
            }
        }
    }
}

@Composable
private fun EditShopForm(
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

        Text(
            text = "Optionale Informationen",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        OutlinedTextField(
            value = formState.url,
            onValueChange = onUrlChange,
            label = { Text("Website-URL") },
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

        Button(
            onClick = onSaveClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Änderungen speichern")
        }
    }
}
