package com.example.sweepapp.data

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

abstract class UserScopedFirestoreRepository {
    protected val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val listeners = mutableListOf<ListenerRegistration>()

    fun start(uid: String) {
        stop()
        val userDoc = db.collection("users").document(uid)
        onStart(userDoc)
    }

    fun stop() {
        listeners.forEach { it.remove() }
        listeners.clear()
        onStop()
    }

    protected fun track(registration: ListenerRegistration) {
        listeners.add(registration)
    }

    protected abstract fun onStart(userDoc: DocumentReference)
    protected abstract fun onStop()
    protected fun currentUserDoc(): DocumentReference? =
        AuthRepository.currentUserId?.let { uid -> db.collection("users").document(uid)}

}