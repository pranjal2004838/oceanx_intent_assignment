package com.oceanx.grocery.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oceanx.grocery.data.models.Order
import com.oceanx.grocery.data.repository.GroceryRepository
import kotlinx.coroutines.launch

class CheckoutViewModel : ViewModel() {

    private val repository = GroceryRepository.getInstance()

    val currentOrder: LiveData<Order?> = repository.currentOrder
    val isLoading: LiveData<Boolean> = repository.isLoading
    val errorMessage: LiveData<String?> = repository.errorMessage

    private val _deliveryAddress = MutableLiveData<String>("123 Main Street, Your City")
    val deliveryAddress: LiveData<String> = _deliveryAddress

    private val _paymentMethod = MutableLiveData<String>("Credit Card")
    val paymentMethod: LiveData<String> = _paymentMethod

    private val _promoCode = MutableLiveData<String>("")
    val promoCode: LiveData<String> = _promoCode

    private val _discountAmount = MutableLiveData<Double>(0.0)
    val discountAmount: LiveData<Double> = _discountAmount

    private val _checkoutSuccess = MutableLiveData<Boolean>(false)
    val checkoutSuccess: LiveData<Boolean> = _checkoutSuccess

    fun setDeliveryAddress(address: String) {
        if (address.isNotBlank()) {
            _deliveryAddress.value = address
        }
    }

    fun setPaymentMethod(method: String) {
        if (method.isNotBlank()) {
            _paymentMethod.value = method
        }
    }

    fun applyPromoCode(code: String) {
        val normalizedCode = code.trim()
        val discount = repository.applyPromoCode(normalizedCode)
        if (discount > 0) {
            _promoCode.value = normalizedCode
            val cartTotal = repository.getCartTotal()
            _discountAmount.value = cartTotal * discount
        } else {
            _promoCode.value = ""
            _discountAmount.value = 0.0
        }
    }

    fun getFinalAmount(): Double {
        val total = repository.getCartTotal()
        val promoCodeValue = _promoCode.value.orEmpty()
        val discountPercent = repository.applyPromoCode(promoCodeValue)
        val discount = total * discountPercent
        return if (total > discount) total - discount else 0.0
    }

    fun proceedCheckout() {
        val address = _deliveryAddress.value
        if (address.isNullOrBlank()) {
            return
        }

        viewModelScope.launch {
            _checkoutSuccess.value = repository.checkoutOrder(address, _promoCode.value.orEmpty())
        }
    }

    fun resetCheckout() {
        _deliveryAddress.value = "123 Main Street, Your City"
        _paymentMethod.value = "Credit Card"
        _promoCode.value = ""
        _discountAmount.value = 0.0
        _checkoutSuccess.value = false
    }
}
