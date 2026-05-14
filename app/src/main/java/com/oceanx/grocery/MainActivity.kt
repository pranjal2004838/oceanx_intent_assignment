package com.oceanx.grocery

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.oceanx.grocery.databinding.ActivityMainBinding
import com.oceanx.grocery.data.viewmodel.CartViewModel
import com.oceanx.grocery.ui.fragments.CartFragment
import com.oceanx.grocery.ui.fragments.HomeFragment
import com.oceanx.grocery.ui.fragments.OrdersFragment
import com.oceanx.grocery.ui.fragments.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var cartViewModel: CartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup ViewModel
        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)

        // Setup toolbar
        setSupportActionBar(binding.toolbar)

        // Setup bottom navigation
        setupBottomNavigation()

        // Load home fragment by default and keep nav state in sync
        if (savedInstanceState == null) {
            binding.bottomNavigation.selectedItemId = R.id.nav_home
        }

        // Observe cart for badge update
        observeCart()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_cart -> CartFragment()
                R.id.nav_orders -> OrdersFragment()
                R.id.nav_profile -> ProfileFragment()
                else -> return@setOnItemSelectedListener false
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()

            true
        }
    }

    private fun observeCart() {
        cartViewModel.itemCount.observe(this) { count ->
            val cartItem = binding.bottomNavigation.menu.findItem(R.id.nav_cart)
            if (count > 0) {
                cartItem?.let { item ->
                    val badge = binding.bottomNavigation.getOrCreateBadge(item.itemId)
                    badge.number = count
                    badge.isVisible = true
                }
            } else {
                binding.bottomNavigation.removeBadge(R.id.nav_cart)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
