package com.gifttrack.feature.orders.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gifttrack.core.domain.model.Order
import com.gifttrack.core.domain.model.OrderStatus
import com.gifttrack.core.domain.repository.OrderRepository
import com.gifttrack.feature.orders.ui.AddOrderFormState
import com.gifttrack.feature.orders.ui.AddOrderUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

/**
 * ViewModel for the Add Order screen.
 *
 * Manages form state, validation, and order creation.
 */
@HiltViewModel
class AddOrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddOrderUiState>(AddOrderUiState.Idle)
    val uiState: StateFlow<AddOrderUiState> = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(AddOrderFormState())
    val formState: StateFlow<AddOrderFormState> = _formState.asStateFlow()

    /**
     * Updates the order number field.
     */
    fun updateOrderNumber(value: String) {
        _formState.value = _formState.value.copy(
            orderNumber = value,
            orderNumberError = null
        )
    }

    /**
     * Updates the shop name field.
     */
    fun updateShopName(value: String) {
        _formState.value = _formState.value.copy(
            shopName = value,
            shopNameError = null
        )
    }

    /**
     * Updates the product name field.
     */
    fun updateProductName(value: String) {
        _formState.value = _formState.value.copy(productName = value)
    }

    /**
     * Updates the product description field.
     */
    fun updateProductDescription(value: String) {
        _formState.value = _formState.value.copy(productDescription = value)
    }

    /**
     * Updates the total amount field.
     */
    fun updateTotalAmount(value: String) {
        _formState.value = _formState.value.copy(
            totalAmount = value,
            totalAmountError = null
        )
    }

    /**
     * Updates the currency field.
     */
    fun updateCurrency(value: String) {
        _formState.value = _formState.value.copy(currency = value)
    }

    /**
     * Updates the carrier name field.
     */
    fun updateCarrierName(value: String) {
        _formState.value = _formState.value.copy(carrierName = value)
    }

    /**
     * Updates the tracking number field.
     */
    fun updateTrackingNumber(value: String) {
        _formState.value = _formState.value.copy(trackingNumber = value)
    }

    /**
     * Updates the is gift flag.
     */
    fun updateIsGift(value: Boolean) {
        _formState.value = _formState.value.copy(isGift = value)
    }

    /**
     * Validates the form and saves the order.
     */
    fun saveOrder() {
        val currentForm = _formState.value

        // Validate required fields
        var hasErrors = false

        if (currentForm.orderNumber.isBlank()) {
            _formState.value = currentForm.copy(
                orderNumberError = "Bestellnummer ist erforderlich"
            )
            hasErrors = true
        }

        if (currentForm.shopName.isBlank()) {
            _formState.value = _formState.value.copy(
                shopNameError = "Shop-Name ist erforderlich"
            )
            hasErrors = true
        }

        if (currentForm.totalAmount.isNotBlank()) {
            val amount = currentForm.totalAmount.toDoubleOrNull()
            if (amount == null || amount < 0) {
                _formState.value = _formState.value.copy(
                    totalAmountError = "Ungültiger Betrag"
                )
                hasErrors = true
            }
        }

        if (hasErrors) {
            return
        }

        // Save order
        _uiState.value = AddOrderUiState.Saving

        viewModelScope.launch {
            try {
                val order = Order(
                    id = UUID.randomUUID().toString(),
                    orderNumber = currentForm.orderNumber,
                    shopId = null,
                    shopName = currentForm.shopName,
                    productName = currentForm.productName.ifBlank { null },
                    productDescription = currentForm.productDescription.ifBlank { null },
                    totalAmount = currentForm.totalAmount.toDoubleOrNull(),
                    currency = currentForm.currency,
                    carrierName = currentForm.carrierName.ifBlank { null },
                    trackingNumber = currentForm.trackingNumber.ifBlank { null },
                    status = OrderStatus.ORDERED,
                    orderDate = System.currentTimeMillis(),
                    estimatedDelivery = null,
                    actualDelivery = null,
                    recipientId = null,
                    recipientName = null,
                    productUrl = null,
                    productImageUrl = null,
                    notes = null,
                    isGift = currentForm.isGift,
                    isHidden = false,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )

                orderRepository.insertOrder(order)
                _uiState.value = AddOrderUiState.Success
            } catch (e: Exception) {
                _uiState.value = AddOrderUiState.Error(
                    message = e.message ?: "Fehler beim Speichern der Bestellung"
                )
            }
        }
    }

    /**
     * Resets the UI state to idle after navigation.
     */
    fun resetUiState() {
        _uiState.value = AddOrderUiState.Idle
    }

    /**
     * Clears the form state.
     */
    fun clearForm() {
        _formState.value = AddOrderFormState()
        _uiState.value = AddOrderUiState.Idle
    }
}
