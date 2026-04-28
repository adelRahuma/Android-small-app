package com.example.firebaseauth.Model


import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()



//    fun login(email: String, password: String): Task<AuthResult> =
//        auth.signInWithEmailAndPassword(email, password)

    suspend fun login(email: String, password: String): AuthResult {
        return auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun signup(email: String, password: String): AuthResult {
        return auth.createUserWithEmailAndPassword(email, password).await()
    }


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

    // Get profile image URL from Firestore
    fun getProfileImageUrl(onResult: (String?) -> Unit) {
        val uid = auth.currentUser?.uid ?: return onResult(null)
        db.collection("users").document(uid).get()
            .addOnSuccessListener { onResult(it.getString("ImgUrl")) }  // ← fix here
            .addOnFailureListener { onResult(null) }
    }

    data class Restaurant(
        val id: String = "",
        val order: Int = 0,
        val name: String = "",
        val address: String = "",
        val Icon: String = "",
        val menue: List<String> = emptyList()
    )

    data class Meal(
        val id: String = "",
        val name: String = "",
        val imgUrl: String = "",
        val ingredients: String = "",
        val price: Double = 0.0
    )

    fun getRestaurants(onResult: (List<Restaurant>) -> Unit) {
        db.collection("Resturants")  // ← your collection name
            .orderBy("order")
            .get()
            .addOnSuccessListener { result ->
                Log.d("MEALS", "Size: ${result.size()}")
                val restaurants = result.map { document ->
                    val restaurant = document.toObject(Restaurant::class.java)
                    restaurant.copy(id = document.id)
                }
                onResult(restaurants)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun getMeals(restaurantId: String, onResult: (List<Meal>) -> Unit) {
//        Log.d("MEALS_FLOW5", "restaurantId = $restaurantId")
        db.collection("Resturants")
            .document(restaurantId)
            .collection("Meals")
            .get()
            .addOnSuccessListener { result ->
//                Log.d("MEALS_FLOW", "Firestore size = ${result.size()}")
                Log.d("MEALS_FLOW5", "RESULT = $result")

                result.documents.forEach {
                    Log.d("MEALS_FLOW5", "RAW DOC = ${it.data}")
                }

                val meals = result.map { doc ->
                    Meal(
                        id = doc.id,
                        name = doc.getString("name") ?: "",
                        imgUrl = doc.getString("imgUrl") ?: "",
                        ingredients = doc.getString("ingredients") ?: "",
                        price = doc.getDouble("price") ?: 0.0
                    )
                }

                onResult(meals)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }


}