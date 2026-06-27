package com.example.pethealthtracker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Column

@Composable
fun PetHealthScreen() {

    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    // 2. BMI is a number, so we explicitly type it as a Float
    var bmi by remember { mutableStateOf<Float>(0f) }

    Column {
        Text("🐶 Pet Health Tracker")

        TextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Height (cm)") }
        )

        TextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Weight (kg)") }
        )

        Button(onClick = {
            val h = height.toFloatOrNull() ?: 0f
            val w = weight.toFloatOrNull() ?: 0f

            val meters = h / 100
            bmi = if (meters > 0) w / (meters * meters) else 0f
        }) {
            Text("Calculate BMI")
        }

        Text("BMI: $bmi")

        Text(
            text = when {
                bmi <= 0f -> "Enter details to check status"
                bmi < 18.5f -> "🐶 Pet is underweight"
                bmi < 25f -> "🐶 Pet is normal"
                else -> "🐶 Pet is overweight"
            }
        )
    }
}