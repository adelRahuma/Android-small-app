package com.example.firebaseauth.View

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.firebaseauth.Model.AuthState
import com.example.firebaseauth.Model.AuthViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest


data class BottomNavItem(val label: String, val icon: ImageVector)

val bottomNavItems = listOf(
    BottomNavItem("Home",     Icons.Default.Home),
    BottomNavItem("Profile",  Icons.Default.Person),
    BottomNavItem("Settings", Icons.Default.Settings)
)

@Composable
fun HomePage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
    val context = LocalContext.current
    val restaurants by authViewModel.restaurants.observeAsState(emptyList())
    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()
    LaunchedEffect(Unit) {
        authViewModel.loadRestaurants()
    }
    var selectedTab by remember { mutableStateOf(0) }


    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize().padding(bottom = 80.dp),
            contentAlignment = Alignment.Center
        ) {
            when (selectedTab) {
                0 -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Home Page",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFF0F0F0),
                        modifier = Modifier.padding(vertical = 16.dp)
                    )

                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(restaurants) { restaurant ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1D27)),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Icon badge
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0xFFFFC107).copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        AsyncImage(
                                            model = restaurant.Icon,
                                            imageLoader = imageLoader,
                                            contentDescription = restaurant.name,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(14.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = restaurant.name,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFF0F0F0)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = restaurant.address,
                                            fontSize = 13.sp,
                                            color = Color(0xFF8A8FA8),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                    Button(
                                        onClick = { navController.navigate("Details/${restaurant.id}") },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFFFFC107).copy(alpha = 0.15f),
                                            contentColor = Color(0xFFFFC107)
                                        )
                                    ) {
                                        Text("Visit")
                                    }
                                }
                            }
                        }
                    }

                    TextButton(onClick = { authViewModel.signout() }) {
                        Text("Sign out")
                    }
                }
                1 -> ProfilePage(navController = navController, authViewModel = authViewModel)
                2 -> Text("Settings Page", fontSize = 24.sp)

            }
        }
        NavigationBar(modifier = Modifier.align(Alignment.BottomCenter)) {
            bottomNavItems.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = selectedTab == index,
                    onClick  = { selectedTab = index },
                    icon     = { Icon(item.icon, contentDescription = item.label) },
                    label    = { Text(item.label) }
                )
            }
        }
    }
}