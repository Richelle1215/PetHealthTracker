package com.example.pethealthtracker // Make sure this matches your actual package path

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text // Use androidx.compose.material.Text if using Material 2
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GamesScreen() {

    val questions = listOf(
        "What is Kotlin used for?",
        "What does HTML stand for?",
        "What is Android Studio?"
    )

    // Added a small padding modifier to prevent the text from sticking to the screen edges
    Column(modifier = Modifier.padding(16.dp)) {
        Text("🎮 Coding Games")

        questions.forEach { q ->
            Text("• $q")
        }
    }
}