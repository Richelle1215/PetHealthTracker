package com.example.pethealthtracker // Make sure this matches your actual package path

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavController
import kotlin.math.absoluteValue

@Composable
fun HomeScreen(navController: NavController, viewModel: PetViewModel) {
    val pagerState = rememberPagerState(pageCount = { 3 }, initialPage = viewModel.petType)

    // Sync ViewModel when pager moves
    LaunchedEffect(pagerState.currentPage) {
        viewModel.petType = pagerState.currentPage
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Top Right: Settings
        IconButton(
            onClick = { /* TODO: navController.navigate("settings") */ },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(56.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                modifier = Modifier.size(35.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        // Top Left: Quick Actions Games and Closet
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            IconButton(
                onClick = { navController.navigate("games") },
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SportsEsports,
                    contentDescription = "Games",
                    modifier = Modifier.size(35.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))

            IconButton(
                onClick = { navController.navigate("closet") },
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Checkroom,
                    contentDescription = "Closet",
                    modifier = Modifier.size(35.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // Pet Slider (Carousel)
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentPadding = PaddingValues(horizontal = 80.dp)
            ) { page ->
                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            // Calculate the absolute offset for the current page from the scroll position.
                            // We use the `currentPageOffsetFraction` to determine how far the page is from being centered.
                            val pageOffset = (
                                    (pagerState.currentPage - page) + pagerState
                                        .currentPageOffsetFraction
                                    ).absoluteValue

                            // Scale the item based on its distance from the center
                            val scale = lerp(
                                start = 0.7f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            )
                            scaleX = scale
                            scaleY = scale
                            
                            // Adjust alpha/opacity based on distance
                            alpha = lerp(
                                start = 0.4f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    RobotPet(
                        modifier = Modifier.size(250.dp),
                        petType = page,
                        bodyColor = viewModel.petColor,
                        eyeColor = viewModel.eyeColor,
                        headphoneColor = viewModel.headphoneColor,
                        hasSunglasses = viewModel.hasSunglasses,
                        hasClothes = viewModel.hasClothes,
                        clothesColor = viewModel.clothesColor
                    )
                }
            }

            Spacer(modifier = Modifier.weight(0.5f))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { navController.navigate("pet") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Pet Health")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate("games") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Games")
                }
            }
        }
    }
}
