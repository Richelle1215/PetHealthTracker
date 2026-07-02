package com.example.pethealthtracker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel

class PetViewModel : ViewModel() {
    var petType by mutableStateOf(0) // 0: Emo, 1: Eilik
    var petColor by mutableStateOf(Color.White)
    var eyeColor by mutableStateOf(Color(0xFF00E5FF)) // Default Teal
    var hasSunglasses by mutableStateOf(false)
    var headphoneColor by mutableStateOf(Color(0xFF3F51B5)) // Blue
    var hasClothes by mutableStateOf(false)
    var clothesColor by mutableStateOf(Color.Red)
}
