package com.oceanx.grocery.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.oceanx.grocery.R
import com.oceanx.grocery.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupListeners()
    }

    private fun setupListeners() {
        binding.apply {
            btnPastOrders.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, OrdersFragment())
                    .addToBackStack(null)
                    .commit()
            }
            
            btnWallets.setOnClickListener {
                showToast("Wallets & Payments coming soon")
            }
            
            btnSavedAddresses.setOnClickListener {
                showToast("Saved Addresses coming soon")
            }
            
            btnHelpCenter.setOnClickListener {
                showToast("Help Center coming soon")
            }
            
            btnCustomerSupport.setOnClickListener {
                showToast("Customer Support coming soon")
            }
            
            btnLogout.setOnClickListener {
                showToast("Logged out successfully")
            }
        }
    }
    
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
