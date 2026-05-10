package com.oceanx.grocery.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.oceanx.grocery.R
import com.oceanx.grocery.data.models.Product
import com.oceanx.grocery.data.viewmodel.HomeViewModel
import com.oceanx.grocery.databinding.FragmentHomeBinding
import com.oceanx.grocery.ui.adapters.CategoryAdapter
import com.oceanx.grocery.ui.adapters.ProductAdapter
import com.oceanx.grocery.ui.adapters.ProductAdapterListener

class HomeFragment : Fragment(), ProductAdapterListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private var productAdapter: ProductAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        setupRecyclerViews()
        observeData()
        setupSearchView()
    }

    private fun setupRecyclerViews() {
        // Products RecyclerView
        binding.productsRecycler.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(true)
        }

        // Categories RecyclerView
        binding.categoriesRecycler.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            setHasFixedSize(false)
        }
    }

    private fun observeData() {
        viewModel.filteredProducts.observe(viewLifecycleOwner) { products ->
            if (products.isEmpty()) {
                binding.emptyState.visibility = View.VISIBLE
                binding.productsRecycler.visibility = View.GONE
            } else {
                binding.emptyState.visibility = View.GONE
                binding.productsRecycler.visibility = View.VISIBLE
                productAdapter = ProductAdapter(products, this)
                binding.productsRecycler.adapter = productAdapter
            }
        }

        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            val categoryAdapter = CategoryAdapter(categories) { category ->
                viewModel.selectCategory(category?.name)
            }
            binding.categoriesRecycler.adapter = categoryAdapter
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.loadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val text = query?.trim().orEmpty()
                if (text.isNotEmpty()) {
                    viewModel.searchProducts(text)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val text = newText?.trim().orEmpty()
                if (text.isEmpty()) {
                    viewModel.clearSearch()
                } else {
                    viewModel.searchProducts(text)
                }
                return true
            }
        })
    }

    override fun onAddToCart(product: Product) {
        viewModel.addToCart(product)
        Toast.makeText(requireContext(), "${product.name} added to cart", Toast.LENGTH_SHORT).show()
    }

    override fun onProductClick(product: Product) {
        // Could navigate to product detail screen
        Toast.makeText(requireContext(), product.name, Toast.LENGTH_SHORT).show()
    }
}
