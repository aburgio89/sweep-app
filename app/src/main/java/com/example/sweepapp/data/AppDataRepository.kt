package com.example.sweepapp.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.util.UUID

//Subject to change. Will be integrated into Firebase.

object AppDataRepository {
    private val _doomBoxEntries = MutableStateFlow<List<DoomBoxEntry>>(emptyList())
    val doomBoxEntries: StateFlow<List<DoomBoxEntry>> = _doomBoxEntries.asStateFlow()

    private val _lastFullSweepDate = MutableStateFlow<LocalDate?>(null)
    val lastFullSweepDate: StateFlow<LocalDate?> = _lastFullSweepDate.asStateFlow()

    fun addDoomBoxEntry(name: String, note: String?) {
        val entry = DoomBoxEntry(
            id = UUID.randomUUID().toString(),
            name = name,
            note = note?.takeIf {it.isNotBlank()},
            dateCreated = LocalDate.now()
        )
        _doomBoxEntries.value = _doomBoxEntries.value + entry
    }

    fun resolveDoomBoxEntry(id: String) {
        _doomBoxEntries.value = _doomBoxEntries.value.map {
            if (it.id == id) it.copy(resolved = true) else it
        }
    }

    fun recordFullSweepCompleted() {
        _lastFullSweepDate.value = LocalDate.now()
    }
}