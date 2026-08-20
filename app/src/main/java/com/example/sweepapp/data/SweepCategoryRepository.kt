package com.example.sweepapp.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SweepCategoryRepository {
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private var listener: ListenerRegistration? = null
    private val _categories = MutableStateFlow<List<SweepCategory>>(emptyList())
    val categories: StateFlow<List<SweepCategory>> = _categories.asStateFlow()

    fun start() {
        stop()
        listener = db.collection("sweepCategories")
            .orderBy("order")
            .addSnapshotListener { snapshot, _ ->
                _categories.value = snapshot?.documents?.mapNotNull { doc ->
                    val name = doc.getString("name") ?: return@mapNotNull null
                    SweepCategory(
                        id = doc.id,
                        order = (doc.getLong("order") ?: 0L).toInt(),
                        name = name,
                        colorHex = doc.getString("colorHex") ?: "#FFFFFF",
                        description = doc.getString("description") ?: "",
                        faq = doc.getString("faq") ?: "",
                        imageKey = doc.getString("imageKey") ?: "sweep1icon"
                    )
                } ?: emptyList()
            }
    }

    fun stop() {
        listener?.remove()
        listener = null
        _categories.value = emptyList()
    }

}