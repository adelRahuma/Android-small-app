package com.example.firebaseauth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.firebaseauth.Model.AuthRepository
import com.example.firebaseauth.Model.AuthViewModel
import com.example.firebaseauth.ui.theme.FirebaseAuthTheme

class MainActivity : ComponentActivity() {

    private val authRepository = AuthRepository()

    private val authViewModel: AuthViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AuthViewModel(authRepository) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirebaseAuthTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyAppNavigator(
                        modifier = Modifier.padding(innerPadding),
                        authViewModel = authViewModel
                    )
                }
            }
        }
    }
}