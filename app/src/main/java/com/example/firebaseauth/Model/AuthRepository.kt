package com.example.firebaseauth.Model

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class AuthRepository {
    private val auth    = FirebaseAuth.getInstance()
    private val db      = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()


    fun login(email: String, password: String): Task<AuthResult> =
        auth.signInWithEmailAndPassword(email, password)

    fun signup(email: String, password: String): Task<AuthResult> =
        auth.createUserWithEmailAndPassword(email, password)

    fun signout() = auth.signOut()

    fun currentUser() = auth.currentUser

    // Save or update phone number in Firestore
    fun saveUserProfile(phone: String, imgUrl: String, onResult: (Boolean) -> Unit) {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid)
            .set(mapOf("phone" to phone, "ImgUrl" to imgUrl))
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    // Fetch user profile from Firestore
    fun getUserProfile(onResult: (String?) -> Unit) {
        val uid = auth.currentUser?.uid ?: return onResult(null)
        db.collection("users").document(uid).get()
            .addOnSuccessListener { onResult(it.getString("phone")) }
            .addOnFailureListener { onResult(null) }
    }

    // Upload image to Firebase Storage and save URL to Firestore
    fun uploadProfileImage(uri: Uri, onResult: (String?) -> Unit) {
        val uid = auth.currentUser?.uid ?: return onResult(null)
        val ref = storage.reference.child("profiles/$uid.jpg")
        ref.putFile(uri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { downloadUri ->
                    db.collection("users").document(uid)
                        .update("ImgUrl", downloadUri.toString())  // ← was "photoUrl"
                        .addOnSuccessListener { onResult(downloadUri.toString()) }
                }
            }
            .addOnFailureListener { onResult(null) }
    }
    // Get profile image URL from Firestore
    fun getProfileImageUrl(onResult: (String?) -> Unit) {
        val uid = auth.currentUser?.uid ?: return onResult(null)
        db.collection("users").document(uid).get()
            .addOnSuccessListener { onResult(it.getString("ImgUrl")) }  // ← fix here
            .addOnFailureListener { onResult(null) }
    }

    data class Restaurant(
        val order: Int = 0,
        val name: String = "",
        val address: String = ""
    )

    fun getRestaurants(onResult: (List<Restaurant>) -> Unit) {
        db.collection("Resturants")  // ← your collection name
            .orderBy("order")
            .get()
            .addOnSuccessListener { result ->
                val restaurants = result.map { it.toObject(Restaurant::class.java) }
                onResult(restaurants)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }
}