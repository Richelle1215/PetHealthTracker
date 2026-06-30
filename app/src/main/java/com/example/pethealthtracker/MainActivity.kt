package com.example.pethealthtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize // Idinagdag ito para mapuno ang screen
import androidx.compose.material3.MaterialTheme // Idinagdag para makuha ang kulay ng theme
import androidx.compose.material3.Surface // Idinagdag para sa background surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier // Idinagdag para sa sizing
import androidx.navigation.compose.*
import com.example.pethealthtracker.ui.theme.PetHealthTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PetHealthTrackerTheme(dynamicColor = false) {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("pet") { PetHealthScreen() }
        composable("games") { GamesScreen() }
    }
}