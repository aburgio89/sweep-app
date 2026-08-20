package com.example.sweepapp.data

// Pulls data documents from Firestore directly as a solution for scalability.
// Sweeps may be added or edited without touching base code.

data class SweepCategory(
    val id: String,
    val order: Int,
    val name: String,
    val colorHex: String,
    val description: String,
    val faq: String,
    val imageKey: String
)