//package com.example.firebaseauth.View
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.statusBarsPadding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.pager.HorizontalPager
//import androidx.compose.foundation.pager.rememberPagerState
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.Button
//import androidx.compose.material3.Card
//import androidx.compose.material3.MaterialTheme
//
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import coil.ImageLoader
//import coil.compose.AsyncImage
//import coil.decode.SvgDecoder
//import com.example.firebaseauth.Model.AuthViewModel
//import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
//import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
//import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType
//
//@Composable
//fun DetailsPage(
//    navController: NavController,
//    authViewModel: AuthViewModel,
//    id: String
//) {
//
//    val restaurant = authViewModel.restaurants.value
//        ?.firstOrNull { it.id == id }
//
//    val menu = restaurant?.menue ?: emptyList()
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .statusBarsPadding()
//            .padding(26.dp)
//               ) {
//        val pagerState = rememberPagerState(0) {
//            menu.size
//        }
//        Text(text = "${restaurant?.name} Menu", fontSize = 20.sp)
//        HorizontalPager(
//            state = pagerState,
//            pageSpacing = 20.dp
//        ) { page ->
//
//            AsyncImage(
//                model = menu[page],
//                contentDescription = restaurant?.name,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(200.dp)   // 👈 fixed height
//                    .clip(RoundedCornerShape(16.dp))
//            )
//        }
//        Spacer(modifier = Modifier.height(10.dp))
//        DotsIndicator(dotCount = menu.size,
//            type = ShiftIndicatorType(DotGraphic(color = MaterialTheme.colorScheme.primary,)),
//            pagerState = pagerState)
//
//
//
//
//        Box(
//            modifier = Modifier.fillMaxSize()
//        ) {
//            Button(
//                onClick = { navController.popBackStack() },
//                modifier = Modifier
//                    .align(Alignment.BottomStart)
//                    .padding(16.dp)
//            ) {
//                Text("Go Back")
//            }
//        }
//    }
//}





package com.example.firebaseauth.View

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.firebaseauth.Model.AuthViewModel
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType

@Composable
fun DetailsPage(
    navController: NavController,
    authViewModel: AuthViewModel,
    id: String
) {

    val restaurant = authViewModel.restaurants.value
        ?.firstOrNull { it.id == id }

    val menu = restaurant?.menue ?: emptyList()

    // 🔹 Load meals when screen opens
    LaunchedEffect(id) {
        Log.d("MEALS","WWWWWWWWWWWWWWWW ${id}")
        authViewModel.loadMeals(id)
    }

    // 🔹 Observe meals
    val meals = authViewModel.meals.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(26.dp)
    ) {

        val pagerState = rememberPagerState(0) {
            menu.size
        }

        Text(
            text = "${restaurant?.name} Menu",
            fontSize = 20.sp
        )

        HorizontalPager(
            state = pagerState,
            pageSpacing = 20.dp
        ) { page ->

            AsyncImage(
                model = menu[page],
                contentDescription = restaurant?.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        DotsIndicator(
            dotCount = menu.size,
            type = ShiftIndicatorType(
                DotGraphic(color = MaterialTheme.colorScheme.primary)
            ),
            pagerState = pagerState
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Meals",
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {

            items(meals.value) { meal ->

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {

                        AsyncImage(
                            model = meal.imgUrl,
                            contentDescription = meal.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = meal.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
//                            text = meal.ingredients,
                            text = meal.ingredients.split(".")[0] + ".",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "£${meal.price}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Button(
                                onClick = {
                                    // handle click (add to cart, etc.)
                                }
                            ) {
                                Text("Book")
                            }
                        }
                    }
                }
            }
        }

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 10.dp)
        ) {
            Text("Go Back")
        }
    }
}
