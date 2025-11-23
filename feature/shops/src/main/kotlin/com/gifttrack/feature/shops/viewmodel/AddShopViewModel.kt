package com.gifttrack.feature.shops.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gifttrack.core.domain.model.Shop
import com.gifttrack.core.domain.repository.ShopRepository
import com.gifttrack.feature.shops.ui.AddShopUiState
import com.gifttrack.feature.shops.ui.ShopFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

/**
 * ViewModel for the Add Shop screen.
 *
 * Manages form state, validation, and shop creation.
 */
@HiltViewModel
class AddShopViewModel @Inject constructor(
    private val shopRepository: ShopRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddShopUiState>(AddShopUiState.Idle)
    val uiState: StateFlow<AddShopUiState> = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(ShopFormState())
    val formState: StateFlow<ShopFormState> = _formState.asStateFlow()

    /**
     * Updates the shop name field.
     */
    fun updateName(value: String) {
        _formState.value = _formState.value.copy(
            name = value,
            nameError = null
        )
    }

    /**
     * Updates the shop URL field.
     */
    fun updateUrl(value: String) {
        _formState.value = _formState.value.copy(
            url = value,
            urlError = null
        )
    }

    /**
     * Updates the logo URL field.
     */
    fun updateLogoUrl(value: String) {
        _formState.value = _formState.value.copy(logoUrl = value)
    }

    /**
     * Updates the color field.
     */
    fun updateColor(value: String) {
        _formState.value = _formState.value.copy(color = value)
    }

    /**
     * Validates the form and saves the shop.
     */
    fun saveShop() {
        val currentForm = _formState.value

        // Validate required fields
        var hasErrors = false

        if (currentForm.name.isBlank()) {
            _formState.value = currentForm.copy(
                nameError = "Shop-Name ist erforderlich"
            )
            hasErrors = true
        }

        if (hasErrors) {
            return
        }

        // Check if shop with same name already exists
        viewModelScope.launch {
            val existingShop = shopRepository.getShopByName(currentForm.name)
            if (existingShop != null) {
                _formState.value = _formState.value.copy(
                    nameError = "Ein Shop mit diesem Namen existiert bereits"
                )
                return@launch
            }

            // Save shop
            _uiState.value = AddShopUiState.Saving

            try {
                val shop = Shop(
                    id = UUID.randomUUID().toString(),
                    name = currentForm.name,
                    url = currentForm.url.ifBlank { null },
                    logoUrl = currentForm.logoUrl.ifBlank { null },
                    color = currentForm.color.ifBlank { null },
                    orderCount = 0,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )

                shopRepository.insertShop(shop)
                _uiState.value = AddShopUiState.Success
            } catch (e: Exception) {
                _uiState.value = AddShopUiState.Error(
                    message = e.message ?: "Fehler beim Speichern des Shops"
                )
            }
        }
    }

    /**
     * Resets the UI state to idle after navigation.
     */
    fun resetUiState() {
        _uiState.value = AddShopUiState.Idle
    }

    /**
     * Clears the form state.
     */
    fun clearForm() {
        _formState.value = ShopFormState()
        _uiState.value = AddShopUiState.Idle
    }
}
