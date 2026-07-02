package com.example.pethealthtracker // Make sure this matches your actual package path

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController, viewModel: PetViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {

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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            RobotPet(
                modifier = Modifier.size(250.dp),
                bodyColor = viewModel.petColor,
                eyeColor = viewModel.eyeColor,
                headphoneColor = viewModel.headphoneColor,
                hasSunglasses = viewModel.hasSunglasses,
                hasClothes = viewModel.hasClothes,
                clothesColor = viewModel.clothesColor
            )

            Spacer(modifier = Modifier.weight(1f))

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