package com.example.pethealthtracker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavController
import kotlin.math.absoluteValue

@Composable
fun ClosetScreen(navController: NavController, viewModel: PetViewModel) {
    val pagerState = rememberPagerState(pageCount = { 3 }, initialPage = viewModel.petType)

    // Sync ViewModel when pager moves
    LaunchedEffect(pagerState.currentPage) {
        viewModel.petType = pagerState.currentPage
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0B1F)) // Deep dark background
            .padding(top = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text(
                text = "Closet 👕",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Swipeable Pet Carousel
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
                        val pageOffset = (
                                (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                                ).absoluteValue
                        val scale = lerp(
                            start = 0.7f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                        scaleX = scale
                        scaleY = scale
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

        Spacer(modifier = Modifier.height(24.dp))

        // Customization Options
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text("Body Color", color = Color.White, fontWeight = FontWeight.Bold)
            ColorPicker(selectedColor = viewModel.petColor) { viewModel.petColor = it }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Accent/Eye Color", color = Color.White, fontWeight = FontWeight.Bold)
            ColorPicker(selectedColor = viewModel.eyeColor) { viewModel.eyeColor = it }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Headphone Color", color = Color.White, fontWeight = FontWeight.Bold)
            ColorPicker(selectedColor = viewModel.headphoneColor) { viewModel.headphoneColor = it }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Sunglasses", color = Color.White, modifier = Modifier.weight(1f))
                Switch(checked = viewModel.hasSunglasses, onCheckedChange = { viewModel.hasSunglasses = it })
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Clothes", color = Color.White, modifier = Modifier.weight(1f))
                Switch(checked = viewModel.hasClothes, onCheckedChange = { viewModel.hasClothes = it })
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ColorPicker(selectedColor: Color, onColorSelected: (Color) -> Unit) {
    val colors = listOf(
        Color.White, Color.Gray, Color.Red, Color.Blue, 
        Color.Green, Color.Yellow, Color.Magenta, Color.Cyan, Color(0xFFF48FB1)
    )
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 8.dp)) {
        items(colors) { color ->
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color, CircleShape)
                    .clickable { onColorSelected(color) }
                    .padding(4.dp)
            ) {
                if (selectedColor == color) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                    )
                }
            }
        }
    }
}
