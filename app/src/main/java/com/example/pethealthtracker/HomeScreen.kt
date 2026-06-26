package com.example.pethealthtracker // Make sure this matches your actual package path

import androidx.compose.foundation.layout.Column
import androidx.navigation.NavController
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HomeScreen(navController: NavController) {
    Column {
        Text("🐾 Pet Health Tracker")

        Button(onClick = { navController.navigate("pet") }) {
            Text("Pet Health")
        }

        Button(onClick = { navController.navigate("games") }) {
            Text("Games")
        }
    }
}