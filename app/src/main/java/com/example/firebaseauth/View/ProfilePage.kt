package com.example.firebaseauth.View

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.firebaseauth.Model.AuthViewModel

private val BgDark        = Color(0xFF0F1117)
private val SurfaceDark   = Color(0xFF1A1D27)
private val AccentYellow  = Color(0xFFFFC107)
private val AccentAmber   = Color(0xFFFF9800)
private val FieldBg       = Color(0xFF23263A)
private val FieldBorder   = Color(0xFF2E3250)
private val TextPrimary   = Color(0xFFF0F0F0)
private val TextSecondary = Color(0xFF8A8FA8)

@Composable
fun ProfilePage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val context    = LocalContext.current
    val photoUrl   = authViewModel.photoUrl.observeAsState()
    val phone      = authViewModel.phone.observeAsState("")

    var phoneInput    by remember { mutableStateOf("") }
    var imgUrlInput   by remember { mutableStateOf("") }
    var isSaving      by remember { mutableStateOf(false) }

    // Load profile data when page opens
    LaunchedEffect(Unit) { authViewModel.loadUserProfile() }

    // Sync loaded values into input fields
    LaunchedEffect(phone.value)    { phoneInput  = phone.value ?: "" }
    LaunchedEffect(photoUrl.value) { imgUrlInput = photoUrl.value ?: "" }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        // Subtle glow
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "My Profile",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Manage your account details",
                fontSize = 14.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 28.dp)
            )

            // Profile image preview
            if (imgUrlInput.isNotEmpty()) {
                AsyncImage(
                    model = imgUrlInput,
                    contentDescription = "Profile photo",
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .border(2.dp, AccentYellow, CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                val initial = authViewModel.currentUser()?.email
                    ?.firstOrNull()?.uppercaseChar() ?: "U"
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .background(FieldBg)
                        .border(2.dp, AccentYellow, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initial.toString(),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentYellow
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(24.dp))
                    .clip(RoundedCornerShape(24.dp))
                    .background(SurfaceDark)
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    // Email (read-only)
                    OutlinedTextField(
                        value = authViewModel.currentUser()?.email ?: "",
                        onValueChange = {},
                        label = { Text("Email") },
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor      = TextSecondary,
                            disabledContainerColor = FieldBg,
                            disabledBorderColor    = FieldBorder,
                            disabledLabelColor     = TextSecondary
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Image URL
                    OutlinedTextField(
                        value = imgUrlInput,
                        onValueChange = { imgUrlInput = it },
                        label = { Text("Profile Image URL") },
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

                    // Phone number
                    OutlinedTextField(
                        value = phoneInput,
                        onValueChange = { phoneInput = it },
                        label = { Text("Phone Number") },
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

                    // Save button
                    Button(
                        onClick = {
                            isSaving = true
                            authViewModel.saveUserProfile(phoneInput, imgUrlInput) { success ->
                                isSaving = false
                                Toast.makeText(
                                    context,
                                    if (success) "Profile saved!" else "Save failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        enabled = !isSaving,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    if (!isSaving)
                                        Brush.horizontalGradient(listOf(AccentYellow, AccentAmber))
                                    else
                                        Brush.horizontalGradient(listOf(AccentYellow.copy(0.4f), AccentAmber.copy(0.4f))),
                                    RoundedCornerShape(14.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSaving) {
                                CircularProgressIndicator(color = BgDark, strokeWidth = 2.dp, modifier = Modifier.size(22.dp))
                            } else {
                                Text("Save Changes", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = BgDark)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}