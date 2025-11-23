package com.gifttrack.feature.orders.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gifttrack.core.domain.model.Order
import com.gifttrack.core.domain.repository.OrderRepository
import com.gifttrack.feature.orders.ui.AddOrderFormState
import com.gifttrack.feature.orders.ui.EditOrderUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Edit Order screen.
 *
 * Manages loading existing order, form state, validation, and order updates.
 */
@HiltViewModel
class EditOrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val orderId: String = checkNotNull(savedStateHandle["orderId"]) {
        "orderId is required for EditOrderViewModel"
    }

    private val _uiState = MutableStateFlow<EditOrderUiState>(EditOrderUiState.Loading)
    val uiState: StateFlow<EditOrderUiState> = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(AddOrderFormState())
    val formState: StateFlow<AddOrderFormState> = _formState.asStateFlow()

    private var currentOrder: Order? = null

    init {
        loadOrder()
    }

    /**
     * Loads the order from the repository.
     */
    private fun loadOrder() {
        viewModelScope.launch {
            try {
                val order = orderRepository.getOrderById(orderId)
                if (order != null) {
                    currentOrder = order
                    _formState.value = AddOrderFormState(
                        orderNumber = order.orderNumber,
                        shopName = order.shopName,
                        productName = order.productName ?: "",
                        productDescription = order.productDescription ?: "",
                        totalAmount = order.totalAmount?.toString() ?: "",
                        currency = order.currency,
                        carrierName = order.carrierName ?: "",
                        trackingNumber = order.trackingNumber ?: "",
                        isGift = order.isGift
                    )
                    _uiState.value = EditOrderUiState.Editing
                } else {
                    _uiState.value = EditOrderUiState.Error(
                        message = "Bestellung nicht gefunden"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = EditOrderUiState.Error(
                    message = e.message ?: "Fehler beim Laden der Bestellung"
                )
            }
        }
    }

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
     * Validates the form and saves the updated order.
     */
    fun saveOrder() {
        val order = currentOrder ?: run {
            _uiState.value = EditOrderUiState.Error(
                message = "Bestellung nicht geladen"
            )
            return
        }

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

        // Update order
        _uiState.value = EditOrderUiState.Saving

        viewModelScope.launch {
            try {
                val updatedOrder = order.copy(
                    orderNumber = currentForm.orderNumber,
                    shopName = currentForm.shopName,
                    productName = currentForm.productName.ifBlank { null },
                    productDescription = currentForm.productDescription.ifBlank { null },
                    totalAmount = currentForm.totalAmount.toDoubleOrNull(),
                    currency = currentForm.currency,
                    carrierName = currentForm.carrierName.ifBlank { null },
                    trackingNumber = currentForm.trackingNumber.ifBlank { null },
                    isGift = currentForm.isGift,
                    updatedAt = System.currentTimeMillis()
                )

                orderRepository.insertOrder(updatedOrder)
                _uiState.value = EditOrderUiState.Success
            } catch (e: Exception) {
                _uiState.value = EditOrderUiState.Error(
                    message = e.message ?: "Fehler beim Speichern der Änderungen"
                )
            }
        }
    }

    /**
     * Resets the UI state to editing after an error.
     */
    fun resetUiState() {
        _uiState.value = EditOrderUiState.Editing
    }
}
