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
    fun loadUserProfile() {
        authRepository.getUserProfile { _phone.value = it ?: "" }
        authRepository.getProfileImageUrl { _photoUrl.value = it }
    }
    fun currentUser() = authRepository.currentUser()
    fun saveUserProfile(phone: String, imgUrl: String, onResult: (Boolean) -> Unit) {
        authRepository.saveUserProfile(phone, imgUrl, onResult)
    }

    fun uploadProfileImage(uri: android.net.Uri, onResult: (String?) -> Unit) {
        authRepository.uploadProfileImage(uri) {
            _photoUrl.value = it
            onResult(it)
        }
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
            authRepository.login(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _authState.value = AuthState.Authenticated
                    } else {
//                        Log.e("FirebaseError", task.exception?.message ?: "Unknown error")
                        _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                    }
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
            authRepository.signup(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _authState.value = AuthState.Authenticated
                    } else {
                        _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                    }
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