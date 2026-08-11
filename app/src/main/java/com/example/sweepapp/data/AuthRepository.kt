package com.example.sweepapp.data

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

object AuthRepository {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance()}

    val currentUserId: String?
        get() = auth.currentUser?.uid
    val currentUserEmail: String?
        get() = auth.currentUser?.email
    val isLoggedIn: Boolean
        get() = auth.currentUser != null

    suspend fun signIn(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUp(email: String, password: String): Result<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        auth.signOut()
    }
}