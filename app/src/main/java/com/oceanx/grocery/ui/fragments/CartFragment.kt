package com.oceanx.grocery.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.oceanx.grocery.R
import com.oceanx.grocery.data.models.CartItem
import com.oceanx.grocery.data.viewmodel.CartViewModel
import com.oceanx.grocery.databinding.FragmentCartBinding
import com.oceanx.grocery.ui.adapters.CartAdapter
import com.oceanx.grocery.ui.adapters.CartAdapterListener
import com.oceanx.grocery.ui.fragments.HomeFragment

class CartFragment : Fragment(), CartAdapterListener {

    private lateinit var binding: FragmentCartBinding
    private lateinit var cartViewModel: CartViewModel
    private var cartAdapter: CartAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)

        setupRecyclerView()
        observeData()
        setupListeners()
    }

    private fun setupRecyclerView() {
        binding.cartRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(false)
        }
    }

    private fun observeData() {
        if (cartAdapter == null) {
            cartAdapter = CartAdapter(this)
            binding.cartRecycler.adapter = cartAdapter
        }

        cartViewModel.cart.observe(viewLifecycleOwner) { items ->
            if (items.isEmpty()) {
                binding.emptyCart.visibility = View.VISIBLE
                binding.cartContainer.visibility = View.GONE
            } else {
                binding.emptyCart.visibility = View.GONE
                binding.cartContainer.visibility = View.VISIBLE
                cartAdapter?.submitList(items)
            }
        }

        cartViewModel.cartTotal.observe(viewLifecycleOwner) { total ->
            binding.totalAmount.text = "₹${total.toInt()}"
        }

        cartViewModel.itemCount.observe(viewLifecycleOwner) { count ->
            binding.itemsCount.text = "Items: $count"
        }
    }

    private fun setupListeners() {
        binding.checkoutBtn.setOnClickListener {
            if (!cartViewModel.isCartEmpty()) {
                // Navigate to checkout
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, CheckoutFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

        binding.continueShopping.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }
    }

    override fun onRemove(item: CartItem) {
        cartViewModel.removeItem(item.product.id)
        Toast.makeText(requireContext(), "${item.product.name} removed", Toast.LENGTH_SHORT).show()
    }

    override fun onQuantityChanged(item: CartItem, newQuantity: Int) {
        cartViewModel.updateQuantity(item.product.id, newQuantity)
    }
}
