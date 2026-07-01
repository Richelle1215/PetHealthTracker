package com.example.pethealthtracker

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun RobotPet(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "robotTransition")
    val scope = rememberCoroutineScope()
    val jumpOffset = remember { Animatable(0f) }
    val danceRotation = remember { Animatable(0f) }
    val danceX = remember { Animatable(0f) }
    val songProgress = remember { Animatable(0f) }
    
    var isSmiling by remember { mutableStateOf(true) }
    var isSinging by remember { mutableStateOf(false) }

    // Blinking animation
    val blinkScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3000
                1f at 0
                1f at 2800
                0.1f at 2900
                1f at 3000
            }
        ),
        label = "blink"
    )


    val bobbingY by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bobbing"
    )

    Canvas(
        modifier = modifier
            .size(250.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        // Logic for detecting swipe direction
                    },
                    onHorizontalDrag = { change, dragAmount ->
                        change.consume()
                        scope.launch {
                            if (dragAmount > 10) { // Swipe Right -> sayaw
                                isSmiling = true
                                launch {
                                    danceRotation.animateTo(10f, tween(150))
                                    danceRotation.animateTo(-10f, tween(300))
                                    danceRotation.animateTo(0f, tween(150))
                                }
                                launch {
                                    danceX.animateTo(20f, tween(150))
                                    danceX.animateTo(-20f, tween(300))
                                    danceX.animateTo(0f, tween(150))
                                }
                            } else if (dragAmount < -10) { // Swipe Left -> Song
                                isSinging = true
                                isSmiling = true
                                songProgress.snapTo(0f)
                                songProgress.animateTo(1f, tween(2000))
                                isSinging = false
                            }
                        }
                    }
                )
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isSmiling = !isSmiling
                scope.launch {
                    jumpOffset.animateTo(-30f, tween(150, easing = FastOutLinearInEasing))
                    jumpOffset.animateTo(0f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                }
            }
    ) {
        val w = size.width
        val h = size.height
        
        val bodyWhite = Color.White
        val frameSilver = Color(0xFFE0E0E0)
        val screenBlack = Color(0xFF0A0A0A)
        val tealEyes = Color(0xFF00E5FF)
        val blueHeadphones = Color(0xFF3F51B5)
        val footGrey = Color(0xFF424242)

        val totalYOffset = bobbingY + jumpOffset.value

        // 1. Feet
        drawRoundRect(
            color = footGrey,
            topLeft = Offset(w * 0.25f, h * 0.75f + jumpOffset.value * 0.2f),
            size = Size(w * 0.22f, h * 0.18f),
            cornerRadius = CornerRadius(20f, 20f)
        )
        drawRoundRect(
            color = footGrey,
            topLeft = Offset(w * 0.53f, h * 0.75f + jumpOffset.value * 0.2f),
            size = Size(w * 0.22f, h * 0.18f),
            cornerRadius = CornerRadius(20f, 20f)
        )

        withTransform({
            translate(top = totalYOffset, left = danceX.value)
            rotate(degrees = danceRotation.value, pivot = Offset(w * 0.5f, h * 0.5f))
        }) {
            // 2. Legs
            drawRect(color = bodyWhite, topLeft = Offset(w * 0.32f, h * 0.65f), size = Size(w * 0.08f, h * 0.12f))
            drawRect(color = bodyWhite, topLeft = Offset(w * 0.6f, h * 0.65f), size = Size(w * 0.08f, h * 0.12f))

            // 3. Main Body/Head (Squircle)
            drawRoundRect(
                color = bodyWhite,
                topLeft = Offset(w * 0.2f, h * 0.15f),
                size = Size(w * 0.6f, h * 0.55f),
                cornerRadius = CornerRadius(80f, 80f)
            )

            // 4. Silver Face Frame
            drawRoundRect(
                color = frameSilver,
                topLeft = Offset(w * 0.24f, h * 0.19f),
                size = Size(w * 0.52f, h * 0.47f),
                cornerRadius = CornerRadius(70f, 70f)
            )

            // 5. Screen
            drawRoundRect(
                color = screenBlack,
                topLeft = Offset(w * 0.26f, h * 0.21f),
                size = Size(w * 0.48f, h * 0.43f),
                cornerRadius = CornerRadius(60f, 60f)
            )

            // 6. Eyes (with blinking)
            val eyeHeight = 0.12f * h * blinkScale
            val eyeTopOffset = (0.12f * h - eyeHeight) / 2
            
            drawRoundRect(
                color = tealEyes,
                topLeft = Offset(w * 0.33f, h * 0.32f + eyeTopOffset),
                size = Size(w * 0.12f, eyeHeight),
                cornerRadius = CornerRadius(15f, 15f)
            )
            drawRoundRect(
                color = tealEyes,
                topLeft = Offset(w * 0.55f, h * 0.32f + eyeTopOffset),
                size = Size(w * 0.12f, eyeHeight),
                cornerRadius = CornerRadius(15f, 15f)
            )

            // 7. Lips (Smile or Sad)
            val mouthPath = Path().apply {
                val startX = w * 0.42f
                val endX = w * 0.58f
                val midX = w * 0.5f
                val mouthY = h * 0.52f
                
                moveTo(startX, mouthY)
                if (isSmiling) {
                    quadraticTo(midX, mouthY + h * 0.04f, endX, mouthY)
                } else {
                    quadraticTo(midX, mouthY - h * 0.04f, endX, mouthY)
                }
            }
            drawPath(
                path = mouthPath,
                color = tealEyes,
                style = Stroke(width = 6f, cap = StrokeCap.Round)
            )

            // 8. Song Visualization (Musical notes)
            if (isSinging) {
                val noteOffset = songProgress.value
                drawCircle(
                    color = tealEyes,
                    center = Offset(w * 0.7f + noteOffset * 20f, h * 0.2f - noteOffset * 50f),
                    radius = 8f * (1f - noteOffset),
                    alpha = 1f - noteOffset
                )
                drawCircle(
                    color = tealEyes,
                    center = Offset(w * 0.8f + noteOffset * 10f, h * 0.3f - noteOffset * 40f),
                    radius = 6f * (1f - noteOffset),
                    alpha = 1f - noteOffset
                )
            }

            // 9. Headphones
            drawArc(
                color = blueHeadphones,
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(w * 0.2f, h * 0.1f),
                size = Size(w * 0.6f, h * 0.2f),
                style = Stroke(width = 12f)
            )
            drawRoundRect(
                color = blueHeadphones,
                topLeft = Offset(w * 0.15f, h * 0.3f),
                size = Size(w * 0.1f, h * 0.2f),
                cornerRadius = CornerRadius(30f, 30f)
            )
            drawRoundRect(
                color = blueHeadphones,
                topLeft = Offset(w * 0.75f, h * 0.3f),
                size = Size(w * 0.1f, h * 0.2f),
                cornerRadius = CornerRadius(30f, 30f)
            )
        }
    }
}

