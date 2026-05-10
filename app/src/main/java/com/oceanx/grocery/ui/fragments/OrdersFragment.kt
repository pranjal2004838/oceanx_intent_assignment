package com.oceanx.grocery.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.oceanx.grocery.data.viewmodel.OrdersViewModel
import com.oceanx.grocery.databinding.FragmentOrdersBinding
import com.oceanx.grocery.ui.adapters.OrderAdapter

class OrdersFragment : Fragment() {

    private lateinit var binding: FragmentOrdersBinding
    private lateinit var ordersViewModel: OrdersViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ordersViewModel = ViewModelProvider(this).get(OrdersViewModel::class.java)

        setupRecyclerViews()
        observeData()
    }

    private fun setupRecyclerViews() {
        binding.activeOrdersRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(false)
        }

        binding.pastOrdersRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(false)
        }
    }

    private fun observeData() {
        // Active Orders
        ordersViewModel.activeOrder.observe(viewLifecycleOwner) { activeOrder ->
            if (activeOrder != null) {
                binding.activeOrdersHeader.visibility = View.VISIBLE
                binding.activeOrdersRecycler.visibility = View.VISIBLE
                binding.noActiveOrders.visibility = View.GONE

                val adapter = OrderAdapter(
                    listOf(activeOrder),
                    { ordersViewModel.getOrderStatusDisplay(it.status) },
                    { ordersViewModel.getOrderStatusIcon(it.status) }
                )
                binding.activeOrdersRecycler.adapter = adapter
            } else {
                binding.activeOrdersHeader.visibility = View.GONE
                binding.activeOrdersRecycler.visibility = View.GONE
                binding.noActiveOrders.visibility = View.VISIBLE
            }
        }

        // Past Orders
        ordersViewModel.pastOrders.observe(viewLifecycleOwner) { pastOrders ->
            if (pastOrders.isEmpty()) {
                binding.pastOrdersHeader.visibility = View.GONE
                binding.pastOrdersRecycler.visibility = View.GONE
                binding.noPastOrders.visibility = View.VISIBLE
            } else {
                binding.pastOrdersHeader.visibility = View.VISIBLE
                binding.pastOrdersRecycler.visibility = View.VISIBLE
                binding.noPastOrders.visibility = View.GONE

                val adapter = OrderAdapter(
                    pastOrders,
                    { ordersViewModel.getOrderStatusDisplay(it.status) },
                    { ordersViewModel.getOrderStatusIcon(it.status) }
                )
                binding.pastOrdersRecycler.adapter = adapter
            }
        }

        // Overall orders list
        ordersViewModel.orders.observe(viewLifecycleOwner) { allOrders ->
            if (allOrders.isEmpty()) {
                binding.emptyState.visibility = View.VISIBLE
                binding.ordersContainer.visibility = View.GONE
            } else {
                binding.emptyState.visibility = View.GONE
                binding.ordersContainer.visibility = View.VISIBLE
            }
        }
    }
}
