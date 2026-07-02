package com.example.pethealthtracker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ClosetScreen(navController: NavController, viewModel: PetViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0B1F)) // Deep dark background
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
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

        // Pet Preview
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentAlignment = Alignment.Center
        ) {
            RobotPet(
                bodyColor = viewModel.petColor,
                eyeColor = viewModel.eyeColor,
                headphoneColor = viewModel.headphoneColor,
                hasSunglasses = viewModel.hasSunglasses,
                hasClothes = viewModel.hasClothes,
                clothesColor = viewModel.clothesColor
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Customization Options
        Text("Body Color", color = Color.White, fontWeight = FontWeight.Bold)
        ColorPicker(selectedColor = viewModel.petColor) { viewModel.petColor = it }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Eye Color", color = Color.White, fontWeight = FontWeight.Bold)
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
    }
}

@Composable
fun ColorPicker(selectedColor: Color, onColorSelected: (Color) -> Unit) {
    val colors = listOf(
        Color.White, Color.Gray, Color.Red, Color.Blue, 
        Color.Green, Color.Yellow, Color.Magenta, Color.Cyan
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
