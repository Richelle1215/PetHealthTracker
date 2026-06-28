package com.example.pethealthtracker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class CodeChallenge(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val emoji: String? = null,
    val colorStart: Color,
    val colorEnd: Color,
    val questionsCount: Int = 8
)

@Composable
fun GamesScreen() {
    val challenges = listOf(
        CodeChallenge(
            "Python", "Snakes & lambdas", Icons.Default.Terminal, "🐍",
            Color(0xFF1E2A47), Color(0xFF162036)
        ),
        CodeChallenge(
            "Kotlin", "Android mastery", Icons.Default.AdsClick, "🎯",
            Color(0xFF311B92), Color(0xFF1A237E)
        ),
        CodeChallenge(
            "JavaScript", "Web sorcery", Icons.Default.Bolt, "⚡",
            Color(0xFF4E342E), Color(0xFF3E2723)
        ),
        CodeChallenge(
            "Java", "OOP fundamentals", Icons.Default.Coffee, "☕",
            Color(0xFF3E2723), Color(0xFF1B0000)
        ),
        CodeChallenge(
            "CS Basics", "Data structures & algos", Icons.Default.Computer, "💻",
            Color(0xFF004D40), Color(0xFF002420)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0B1F)) // Deep dark background from image
            .padding(16.dp)
    ) {
        Text(
            text = "Code Challenges 🎮",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Level up your programming knowledge!",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(challenges) { challenge ->
                ChallengeCard(challenge)
            }
        }
    }
}

@Composable
fun ChallengeCard(challenge: CodeChallenge) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(challenge.colorStart, challenge.colorEnd)
                    )
                )
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Icon Box
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color(0x22FFFFFF), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (challenge.emoji != null) {
                        Text(challenge.emoji, fontSize = 24.sp)
                    } else {
                        Icon(challenge.icon, contentDescription = null, tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Content
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = challenge.title,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = challenge.description,
                        color = Color.LightGray,
                        fontSize = 13.sp
                    )
                    
                    Row(modifier = Modifier.padding(top = 8.dp)) {
                        Badge(
                            text = "${challenge.questionsCount} Questions",
                            containerColor = Color(0x3300E5FF),
                            contentColor = Color(0xFF00E5FF)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Badge(
                            text = "Multiple Choice",
                            containerColor = Color(0x11FFFFFF),
                            contentColor = Color.Gray
                        )
                    }
                }

                // Arrow
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = challenge.colorStart.copy(alpha = 0.8f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun Badge(text: String, containerColor: Color, contentColor: Color) {
    Surface(
        color = containerColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            color = contentColor,
            fontSize = 10.sp,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontWeight = FontWeight.Medium
        )
    }
}
