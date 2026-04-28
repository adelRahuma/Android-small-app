package com.example.firebaseauth.Model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _phone = MutableLiveData<String>("")
    val phone: LiveData<String> = _phone

    private val _photoUrl = MutableLiveData<String?>()
    val photoUrl: LiveData<String?> = _photoUrl
    private val _restaurants = MutableLiveData<List<AuthRepository.Restaurant>>(emptyList())
    val restaurants: LiveData<List<AuthRepository.Restaurant>> = _restaurants

    fun loadRestaurants() {
        authRepository.getRestaurants { _restaurants.value = it }
    }
    private val _meals = MutableLiveData<List<AuthRepository.Meal>>(emptyList())
    val meals: LiveData<List<AuthRepository.Meal>> = _meals

    fun loadMeals(id: String) {
        Log.d("MEALS_FLOW", "STEP 1: ViewModel called with id = $id")
        authRepository.getMeals(id) {
            Log.d("MEALS", "Meals received: ${it.size}")
            _meals.value = it
        }
    }
    fun loadUserProfile() {
        authRepository.getUserProfile { _phone.value = it ?: "" }
        authRepository.getProfileImageUrl { _photoUrl.value = it }
    }
    fun currentUser() = authRepository.currentUser()
    fun saveUserProfile(phone: String, imgUrl: String, onResult: (Boolean) -> Unit) {
        authRepository.saveUserProfile(phone, imgUrl, onResult)
    }

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        if (authRepository.currentUser() == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            _authState.value = AuthState.Authenticated
        }
    }

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }

        _authState.value = AuthState.Loading

        viewModelScope.launch {
            try {
                authRepository.login(email, password)
                _authState.value = AuthState.Authenticated
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun signup(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }

        _authState.value = AuthState.Loading

        viewModelScope.launch {
            try {
                authRepository.signup(email, password)

                // If no exception → success
                _authState.value = AuthState.Authenticated

            } catch (e: Exception) {
                _authState.value = AuthState.Error(
                    e.message ?: "Something went wrong"
                )
            }
        }
    }

    fun signout() {
        authRepository.signout()
        _authState.value = AuthState.Unauthenticated
    }
}

sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}