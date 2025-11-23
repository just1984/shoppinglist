package com.gifttrack.feature.shops.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gifttrack.core.domain.model.Shop
import com.gifttrack.core.domain.repository.ShopRepository
import com.gifttrack.feature.shops.ui.EditShopUiState
import com.gifttrack.feature.shops.ui.ShopFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Edit Shop screen.
 *
 * Manages loading existing shop, form state, validation, and shop updates.
 */
@HiltViewModel
class EditShopViewModel @Inject constructor(
    private val shopRepository: ShopRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val shopId: String = checkNotNull(savedStateHandle["shopId"]) {
        "shopId is required for EditShopViewModel"
    }

    private val _uiState = MutableStateFlow<EditShopUiState>(EditShopUiState.Loading)
    val uiState: StateFlow<EditShopUiState> = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(ShopFormState())
    val formState: StateFlow<ShopFormState> = _formState.asStateFlow()

    private var currentShop: Shop? = null

    init {
        loadShop()
    }

    /**
     * Loads the shop from the repository.
     */
    private fun loadShop() {
        viewModelScope.launch {
            try {
                val shop = shopRepository.getShopById(shopId)
                if (shop != null) {
                    currentShop = shop
                    _formState.value = ShopFormState(
                        name = shop.name,
                        url = shop.url ?: "",
                        logoUrl = shop.logoUrl ?: "",
                        color = shop.color ?: ""
                    )
                    _uiState.value = EditShopUiState.Editing
                } else {
                    _uiState.value = EditShopUiState.Error(
                        message = "Shop nicht gefunden"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = EditShopUiState.Error(
                    message = e.message ?: "Fehler beim Laden des Shops"
                )
            }
        }
    }

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
     * Validates the form and saves the updated shop.
     */
    fun saveShop() {
        val shop = currentShop ?: run {
            _uiState.value = EditShopUiState.Error(
                message = "Shop nicht geladen"
            )
            return
        }

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

        // Check if shop name is changed and already exists
        viewModelScope.launch {
            if (currentForm.name != shop.name) {
                val existingShop = shopRepository.getShopByName(currentForm.name)
                if (existingShop != null && existingShop.id != shopId) {
                    _formState.value = _formState.value.copy(
                        nameError = "Ein Shop mit diesem Namen existiert bereits"
                    )
                    return@launch
                }
            }

            // Update shop
            _uiState.value = EditShopUiState.Saving

            try {
                val updatedShop = shop.copy(
                    name = currentForm.name,
                    url = currentForm.url.ifBlank { null },
                    logoUrl = currentForm.logoUrl.ifBlank { null },
                    color = currentForm.color.ifBlank { null },
                    updatedAt = System.currentTimeMillis()
                )

                shopRepository.updateShop(updatedShop)
                _uiState.value = EditShopUiState.Success
            } catch (e: Exception) {
                _uiState.value = EditShopUiState.Error(
                    message = e.message ?: "Fehler beim Speichern der Änderungen"
                )
            }
        }
    }

    /**
     * Resets the UI state to editing after an error.
     */
    fun resetUiState() {
        _uiState.value = EditShopUiState.Editing
    }
}
