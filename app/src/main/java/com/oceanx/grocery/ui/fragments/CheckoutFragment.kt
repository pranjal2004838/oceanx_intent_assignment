package com.oceanx.grocery.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.oceanx.grocery.R
import com.oceanx.grocery.data.models.CartItem
import com.oceanx.grocery.data.models.Order
import com.oceanx.grocery.data.viewmodel.CartViewModel
import com.oceanx.grocery.data.viewmodel.CheckoutViewModel
import com.oceanx.grocery.databinding.FragmentCheckoutBinding
import com.oceanx.grocery.ui.adapters.CartAdapter
import com.oceanx.grocery.ui.adapters.CartAdapterListener
import com.oceanx.grocery.ui.fragments.HomeFragment

class CheckoutFragment : Fragment(), CartAdapterListener {

    private lateinit var binding: FragmentCheckoutBinding
    private lateinit var checkoutViewModel: CheckoutViewModel
    private lateinit var cartViewModel: CartViewModel
    private var checkoutAdapter: CartAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkoutViewModel = ViewModelProvider(requireActivity()).get(CheckoutViewModel::class.java)
        cartViewModel = ViewModelProvider(requireActivity()).get(CartViewModel::class.java)

        setupUI()
        observeData()
        setupListeners()
    }

    private fun setupUI() {
        binding.apply {
            deliveryAddressInput.setText(checkoutViewModel.deliveryAddress.value ?: "")
            paymentSpinner.setSelection(0) // Default Credit Card
            subtotalAmount.text = "₹0"
            orderTotalAmount.text = "₹0"
            
            // Setup checkout items recycler
            checkoutItemsRecycler.layoutManager = LinearLayoutManager(requireContext())
            checkoutItemsRecycler.setHasFixedSize(false)
            
            if (checkoutAdapter == null) {
                checkoutAdapter = CartAdapter(this@CheckoutFragment)
                checkoutItemsRecycler.adapter = checkoutAdapter
            }
        }
    }

    private fun observeData() {
        // Observe cart items and populate the checkout recycler
        cartViewModel.cart.observe(viewLifecycleOwner) { items ->
            checkoutAdapter?.submitList(items)
        }

        // Observe cart total
        cartViewModel.cartTotal.observe(viewLifecycleOwner) { total ->
            binding.subtotalAmount.text = "₹${total.toInt()}"
            updateOrderTotal()
        }

        checkoutViewModel.checkoutSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                val order = checkoutViewModel.currentOrder.value
                if (order != null) {
                    showOrderConfirmation(order)
                }
            }
        }

        checkoutViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.placeOrderBtn.isEnabled = !isLoading
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        checkoutViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
            }
        }

        checkoutViewModel.discountAmount.observe(viewLifecycleOwner) { discount ->
            if (discount > 0) {
                binding.discountInfo.visibility = View.VISIBLE
                binding.discountInfo.text = "Discount Applied: ₹${discount.toInt()}"
            } else {
                binding.discountInfo.visibility = View.GONE
            }
            updateOrderTotal()
        }
    }

    private fun updateOrderTotal() {
        val subtotal = cartViewModel.cartTotal.value ?: 0.0
        val discount = checkoutViewModel.discountAmount.value ?: 0.0
        val finalTotal = if (subtotal > discount) subtotal - discount else subtotal
        binding.orderTotalAmount.text = "₹${finalTotal.toInt()}"
    }

    private fun setupListeners() {
        binding.apply {
            paymentSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selected = parent?.getItemAtPosition(position)?.toString().orEmpty()
                    if (selected.isNotBlank()) {
                        checkoutViewModel.setPaymentMethod(selected)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    checkoutViewModel.setPaymentMethod("Credit Card")
                }
            }

            applyPromoBtn.setOnClickListener {
                val code = promoCodeInput.text.toString().trim()
                if (code.isNotEmpty()) {
                    checkoutViewModel.applyPromoCode(code)
                    Toast.makeText(
                        requireContext(),
                        "Promo code applied successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            locationDemoBtn.setOnClickListener {
                val demoLocation = "123 Main Street, City (GPS demo)"
                deliveryAddressInput.setText(demoLocation)
                checkoutViewModel.setDeliveryAddress(demoLocation)
                Toast.makeText(requireContext(), "Location fetched (demo)", Toast.LENGTH_SHORT).show()
            }

            placeOrderBtn.setOnClickListener {
                val address = deliveryAddressInput.text.toString().trim()
                if (address.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Please enter delivery address",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                checkoutViewModel.setDeliveryAddress(address)
                checkoutViewModel.proceedCheckout()
            }

            backBtn.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
        }
    }

    private fun showOrderConfirmation(order: Order) {
        binding.apply {
            contentContainer.visibility = View.GONE
            confirmationContainer.visibility = View.VISIBLE

            confirmationOrderId.text = "Order ID: ${order.orderId}"
            confirmationTotal.text = "₹${order.totalAmount.toInt()}"
            confirmationDelivery.text = "Estimated Delivery: ${order.estimatedDelivery} minutes"

            trackOrderBtn.setOnClickListener {
                parentFragmentManager.popBackStack()
                // Navigate to orders
            }

            continueShoppingBtn.setOnClickListener {
                checkoutViewModel.resetCheckout()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment())
                    .commit()
            }
        }
    }

    override fun onRemove(item: CartItem) {
        cartViewModel.removeItem(item.product.id)
    }

    override fun onQuantityChanged(item: CartItem, newQuantity: Int) {
        cartViewModel.updateQuantity(item.product.id, newQuantity)
    }
}
