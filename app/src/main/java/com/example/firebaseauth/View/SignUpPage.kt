package com.example.firebaseauth.View

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.firebaseauth.Model.AuthState
import com.example.firebaseauth.Model.AuthViewModel
import com.example.firebaseauth.R

private val BgDark       = Color(0xFF0F1117)
private val SurfaceDark  = Color(0xFF1A1D27)
private val AccentYellow = Color(0xFFFFC107)
private val AccentAmber  = Color(0xFFFF9800)
private val FieldBg      = Color(0xFF23263A)
private val FieldBorder  = Color(0xFF2E3250)
private val TextPrimary  = Color(0xFFF0F0F0)
private val TextSecondary = Color(0xFF8A8FA8)

@Composable
fun SignUpPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        if (authState.value is AuthState.Error) {
            Toast.makeText(
                context,
                (authState.value as AuthState.Error).message,
                Toast.LENGTH_SHORT
            ).show()

        }

    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(AccentYellow.copy(alpha = 0.07f), Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.signup),
                contentDescription = "Sign up illustration",
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Create Account",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Join us — it only takes a minute",
                fontSize = 14.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(24.dp))
                    .clip(RoundedCornerShape(24.dp))
                    .background(SurfaceDark)
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(text = "Email") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor        = TextPrimary,
                            unfocusedTextColor      = TextPrimary,
                            focusedContainerColor   = FieldBg,
                            unfocusedContainerColor = FieldBg,
                            focusedBorderColor      = AccentYellow.copy(alpha = 0.8f),
                            unfocusedBorderColor    = FieldBorder,
                            focusedLabelColor       = AccentYellow,
                            unfocusedLabelColor     = TextSecondary,
                            cursorColor             = AccentYellow
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(text = "Password") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor        = TextPrimary,
                            unfocusedTextColor      = TextPrimary,
                            focusedContainerColor   = FieldBg,
                            unfocusedContainerColor = FieldBg,
                            focusedBorderColor      = AccentYellow.copy(alpha = 0.8f),
                            unfocusedBorderColor    = FieldBorder,
                            focusedLabelColor       = AccentYellow,
                            unfocusedLabelColor     = TextSecondary,
                            cursorColor             = AccentYellow
                        )
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    Button(
                        onClick = { authViewModel.signup(email, password) },
                        enabled = authState.value != AuthState.Loading,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    if (authState.value != AuthState.Loading)
                                        Brush.horizontalGradient(listOf(AccentYellow, AccentAmber))
                                    else
                                        Brush.horizontalGradient(listOf(AccentYellow.copy(0.4f), AccentAmber.copy(0.4f))),
                                    RoundedCornerShape(14.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (authState.value == AuthState.Loading) {
                                CircularProgressIndicator(color = BgDark, strokeWidth = 2.dp, modifier = Modifier.size(22.dp))
                            } else {
                                Text(text = "Create account", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = BgDark)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { navController.navigate("Login") }) {
                Text(text = "Already have an account? ", color = TextSecondary, fontSize = 14.sp)
                Text(text = "Login", color = AccentYellow, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}