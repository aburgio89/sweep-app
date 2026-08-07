package com.example.sweepapp.data

import java.time.LocalDate

data class DoomBoxEntry(
    val id: String,
    val name: String,
    val note: String? = null,
    val dateCreated: LocalDate,
    val resolved: Boolean = false
)