package com.example.sweepapp.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.UUID

object AppDataRepository : UserScopedFirestoreRepository() {
    private val _doomBoxEntries = MutableStateFlow<List<DoomBoxEntry>>(emptyList())
    val doomBoxEntries: StateFlow<List<DoomBoxEntry>> = _doomBoxEntries.asStateFlow()

    private val _lastFullSweepDate = MutableStateFlow<LocalDate?>(null)
    val lastFullSweepDate: StateFlow<LocalDate?> = _lastFullSweepDate.asStateFlow()

    override fun onStart(userDoc: DocumentReference) {
        track(
            userDoc.addSnapshotListener { snapshot, _ ->
                _lastFullSweepDate.value = snapshot?.getTimestamp("lastFullSweepDate")?.toLocalDate()
            }
        )

        track(
            userDoc.collection("doomBoxEntries")
                .addSnapshotListener { snapshot, _ ->
                    _doomBoxEntries.value = snapshot?.documents?.mapNotNull { doc ->
                        val name = doc.getString("name") ?: return@mapNotNull null
                        val created = doc.getTimestamp("dateCreated") ?: return@mapNotNull null
                        DoomBoxEntry (
                            id = doc.id,
                            name = name,
                            note = doc.getString("note"),
                            dateCreated = created.toLocalDate(),
                            resolved = doc.getBoolean("resolved") ?: false
                        )
                    } ?: emptyList()
                }
        )
    }

    override fun onStop() {
        _doomBoxEntries.value = emptyList()
        _lastFullSweepDate.value = null
    }

    fun addDoomBoxEntry(name: String, note: String?) {
        val userDoc = currentUserDoc() ?: return
        val entry = hashMapOf(
            "name" to name,
            "note" to note?.takeIf { it.isNotBlank() },
            "dateCreated" to Date(),
            "resolved" to false
        )
        userDoc.collection("doomBoxEntries").add(entry)
    }

    fun resolveDoomBoxEntry(id: String) {
        currentUserDoc()?.collection("doomBoxEntries")?.document(id)?.update("resolved", true)
    }

    fun removeDoomBoxEntry(id: String) {
        currentUserDoc()?.collection("doomBoxEntries")?.document(id)?.delete()
    }
    fun recordFullSweepCompleted() {
        val userDoc = currentUserDoc() ?: return
        userDoc.set(mapOf("lastFullSweepDate" to Date()), SetOptions.merge())
    }
    private fun Timestamp.toLocalDate(): LocalDate =
        Instant.ofEpochSecond(seconds, nanoseconds.toLong()).atZone(ZoneId.systemDefault()).toLocalDate()
}