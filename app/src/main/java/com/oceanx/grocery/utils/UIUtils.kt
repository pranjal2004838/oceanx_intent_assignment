package com.oceanx.grocery.utils

import android.content.Context
import androidx.core.content.ContextCompat

object UIUtils {

    fun getColorCompat(context: Context, colorRes: Int): Int {
        return ContextCompat.getColor(context, colorRes)
    }

    fun formatPrice(price: Double): String {
        return "₹${price.toInt()}"
    }

    fun formatCurrency(amount: Double): String {
        return String.format("₹%.2f", amount)
    }
}
