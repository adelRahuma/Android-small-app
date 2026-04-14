//package com.example.firebaseauth
//
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import com.example.firebaseauth.Model.AuthViewModel
//import com.example.firebaseauth.View.*
//
//@Composable
//fun MyAppNavigator(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
//    val navController = rememberNavController()
//    NavHost(navController = navController, startDestination = "Login", builder = {
//        composable("Login"){
//            LoginPage(modifier, navController,authViewModel )
//        }
//        composable("Home") {
//            HomePage(modifier, navController, authViewModel) // keep modifier ✅
//        }
//        composable("SignUp"){
//            SignUpPage(modifier, navController,authViewModel )
//        }
////        composable("Home"){
////            HomePage(modifier, navController,authViewModel )
////        }
//    })
//}
//
//
//
//
//
//



package com.example.firebaseauth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.firebaseauth.Model.AuthState
import com.example.firebaseauth.Model.AuthViewModel
import com.example.firebaseauth.View.*

@Composable
fun MyAppNavigator(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated   -> navController.navigate("Home") {
                popUpTo("Login") { inclusive = true }
            }
            is AuthState.Unauthenticated -> navController.navigate("Login") {
                popUpTo("Home") { inclusive = true } // can't go back to Home after logout
            }
            else -> Unit
        }
    }

    NavHost(navController = navController, startDestination = "Login") {
        composable("Login")  { LoginPage(modifier, navController, authViewModel) }
        composable("SignUp") { SignUpPage(modifier, navController, authViewModel) }
        composable("Home")   { HomePage(modifier, navController, authViewModel) }
    }
}

