package com.example.sweepapp.data

import com.example.sweepapp.R

object SweepImageMap {
    private val map = mapOf(
        "sweep1icon" to R.drawable.sweep1icon,
        "sweep2icon" to R.drawable.sweep2icon,
        "sweep3icon" to R.drawable.sweep3icon,
        "sweep4icon" to R.drawable.sweep4icon,
        "sweep5icon" to R.drawable.sweep5icon,
        "sweep6icon" to R.drawable.sweep6icon,
        "sweep7icon" to R.drawable.sweep7icon
    )

    fun resolve(key: String): Int = map[key] ?: R.drawable.sweep_monster
}