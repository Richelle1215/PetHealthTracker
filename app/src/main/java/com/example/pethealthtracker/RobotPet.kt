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
fun RobotPet(
    modifier: Modifier = Modifier,
    petType: Int = 0, // 0: Emo, 1: Eilik
    bodyColor: Color = Color.White,
    eyeColor: Color = Color(0xFF00E5FF),
    headphoneColor: Color = Color(0xFF3F51B5),
    hasSunglasses: Boolean = false,
    hasClothes: Boolean = false,
    clothesColor: Color = Color.Red
) {
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

    // Arm swaying animation (For Eilik)
    val armRotation by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "arms"
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
                    onHorizontalDrag = { change, dragAmount ->
                        change.consume()
                        scope.launch {
                            if (dragAmount > 10) { 
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
                            } else if (dragAmount < -10) {
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
        
        val mainColor = bodyColor
        val screenBlack = Color(0xFF0A0A0A)
        val faceAccent = if (petType == 0) eyeColor else Color(0xFFFFD1E1) // Emo uses custom eye color, Eilik uses pinkish face details

        val totalYOffset = bobbingY + jumpOffset.value

        withTransform({
            translate(top = totalYOffset, left = danceX.value)
            rotate(degrees = danceRotation.value, pivot = Offset(w * 0.5f, h * 0.5f))
        }) {
            if (petType == 0) {
                // PET TYPE 0: EMO STYLE
                // Feet (Stay on ground or move with jump)
                drawRoundRect(
                    color = Color(0xFF424242),
                    topLeft = Offset(w * 0.25f, h * 0.75f - totalYOffset + jumpOffset.value * 0.2f),
                    size = Size(w * 0.22f, h * 0.18f),
                    cornerRadius = CornerRadius(20f, 20f)
                )
                drawRoundRect(
                    color = Color(0xFF424242),
                    topLeft = Offset(w * 0.53f, h * 0.75f - totalYOffset + jumpOffset.value * 0.2f),
                    size = Size(w * 0.22f, h * 0.18f),
                    cornerRadius = CornerRadius(20f, 20f)
                )

                // Legs
                drawRect(color = mainColor, topLeft = Offset(w * 0.32f, h * 0.65f), size = Size(w * 0.08f, h * 0.12f))
                drawRect(color = mainColor, topLeft = Offset(w * 0.6f, h * 0.65f), size = Size(w * 0.08f, h * 0.12f))

                // Body (Squircle)
                drawRoundRect(
                    color = mainColor,
                    topLeft = Offset(w * 0.2f, h * 0.15f),
                    size = Size(w * 0.6f, h * 0.55f),
                    cornerRadius = CornerRadius(80f, 80f)
                )

                // Face Frame (Silver)
                drawRoundRect(
                    color = Color(0xFFE0E0E0),
                    topLeft = Offset(w * 0.24f, h * 0.19f),
                    size = Size(w * 0.52f, h * 0.47f),
                    cornerRadius = CornerRadius(70f, 70f)
                )

                // Screen
                drawRoundRect(
                    color = screenBlack,
                    topLeft = Offset(w * 0.26f, h * 0.21f),
                    size = Size(w * 0.48f, h * 0.43f),
                    cornerRadius = CornerRadius(60f, 60f)
                )

                // Eyes (Square-ish with blinking)
                val eyeHeight = 0.12f * h * blinkScale
                val eyeTopOffset = (0.12f * h - eyeHeight) / 2

                if (hasSunglasses) {
                    drawRect(color = Color.Black, topLeft = Offset(w * 0.28f, h * 0.3f), size = Size(w * 0.44f, h * 0.1f))
                } else {
                    drawRoundRect(
                        color = eyeColor,
                        topLeft = Offset(w * 0.33f, h * 0.32f + eyeTopOffset),
                        size = Size(w * 0.12f, eyeHeight),
                        cornerRadius = CornerRadius(15f, 15f)
                    )
                    drawRoundRect(
                        color = eyeColor,
                        topLeft = Offset(w * 0.55f, h * 0.32f + eyeTopOffset),
                        size = Size(w * 0.12f, eyeHeight),
                        cornerRadius = CornerRadius(15f, 15f)
                    )
                }

                // Mouth
                val mouthPath = Path().apply {
                    val mouthY = h * 0.52f
                    moveTo(w * 0.42f, mouthY)
                    if (isSmiling) quadraticTo(w * 0.5f, mouthY + h * 0.04f, w * 0.58f, mouthY)
                    else quadraticTo(w * 0.5f, mouthY - h * 0.04f, w * 0.58f, mouthY)
                }
                drawPath(path = mouthPath, color = eyeColor, style = Stroke(width = 6f, cap = StrokeCap.Round))

            } else {
                // PET TYPE 1: EILIK STYLE
                // Body (Egg)
                drawOval(color = mainColor, topLeft = Offset(w * 0.25f, h * 0.45f), size = Size(w * 0.5f, h * 0.5f))

                // Collar
                val collarPath = Path().apply {
                    moveTo(w * 0.35f, h * 0.45f)
                    quadraticTo(w * 0.5f, h * 0.65f, w * 0.65f, h * 0.45f)
                    close()
                }
                drawPath(path = collarPath, color = eyeColor) // Uses Eye Color as accent

                // Arms
                withTransform({ rotate(degrees = armRotation, pivot = Offset(w * 0.35f, h * 0.55f)) }) {
                    val armPath = Path().apply {
                        moveTo(w * 0.35f, h * 0.55f)
                        cubicTo(w * 0.15f, h * 0.55f, w * 0.1f, h * 0.45f, w * 0.18f, h * 0.35f)
                    }
                    drawPath(path = armPath, color = mainColor, style = Stroke(width = w * 0.12f, cap = StrokeCap.Round))
                    drawPath(path = armPath, color = eyeColor, style = Stroke(width = w * 0.06f, cap = StrokeCap.Round))
                }
                withTransform({ rotate(degrees = -armRotation, pivot = Offset(w * 0.65f, h * 0.55f)) }) {
                    val armPath = Path().apply {
                        moveTo(w * 0.65f, h * 0.55f)
                        cubicTo(w * 0.85f, h * 0.55f, w * 0.9f, h * 0.45f, w * 0.82f, h * 0.35f)
                    }
                    drawPath(path = armPath, color = mainColor, style = Stroke(width = w * 0.12f, cap = StrokeCap.Round))
                    drawPath(path = armPath, color = eyeColor, style = Stroke(width = w * 0.06f, cap = StrokeCap.Round))
                }

                // Head
                drawOval(color = mainColor, topLeft = Offset(w * 0.15f, h * 0.1f), size = Size(w * 0.7f, h * 0.42f))

                // Screen
                drawRoundRect(
                    color = screenBlack,
                    topLeft = Offset(w * 0.28f, h * 0.18f),
                    size = Size(w * 0.44f, h * 0.26f),
                    cornerRadius = CornerRadius(60f, 60f)
                )

                // Eyes
                val eyeHeight = 0.1f * h * blinkScale
                val eyeTopOffset = (0.1f * h - eyeHeight) / 2
                if (hasSunglasses) {
                    drawRect(color = Color.Black, topLeft = Offset(w * 0.28f, h * 0.23f), size = Size(w * 0.44f, h * 0.08f))
                } else {
                    drawArc(color = faceAccent, startAngle = 180f, sweepAngle = 180f, useCenter = false, topLeft = Offset(w * 0.35f, h * 0.23f + eyeTopOffset), size = Size(w * 0.12f, eyeHeight))
                    drawArc(color = faceAccent, startAngle = 180f, sweepAngle = 180f, useCenter = false, topLeft = Offset(w * 0.53f, h * 0.23f + eyeTopOffset), size = Size(w * 0.12f, eyeHeight))
                }

                // Mouth
                val mouthPath = Path().apply {
                    val mouthY = h * 0.35f
                    moveTo(w * 0.45f, mouthY)
                    if (isSmiling) quadraticTo(w * 0.5f, mouthY + h * 0.03f, w * 0.55f, mouthY)
                    else quadraticTo(w * 0.5f, mouthY - h * 0.03f, w * 0.55f, mouthY)
                }
                drawPath(path = mouthPath, color = faceAccent, style = Stroke(width = 6f, cap = StrokeCap.Round))
            }

            // Shared Accessories
            // Clothes
            if (hasClothes) {
                drawRoundRect(
                    color = clothesColor,
                    topLeft = Offset(w * 0.25f, h * 0.55f),
                    size = Size(w * 0.5f, h * 0.2f),
                    cornerRadius = CornerRadius(20f, 20f)
                )
            }

            // Headphones
            drawArc(
                color = headphoneColor,
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(w * 0.2f, h * 0.1f),
                size = Size(w * 0.6f, h * 0.2f),
                style = Stroke(width = 12f)
            )
            drawRoundRect(color = headphoneColor, topLeft = Offset(w * 0.15f, h * 0.3f), size = Size(w * 0.1f, h * 0.2f), cornerRadius = CornerRadius(30f, 30f))
            drawRoundRect(color = headphoneColor, topLeft = Offset(w * 0.75f, h * 0.3f), size = Size(w * 0.1f, h * 0.2f), cornerRadius = CornerRadius(30f, 30f))

            // Song Visualization
            if (isSinging) {
                val noteOffset = songProgress.value
                drawCircle(color = eyeColor, center = Offset(w * 0.75f + noteOffset * 20f, h * 0.15f - noteOffset * 50f), radius = 8f * (1f - noteOffset), alpha = 1f - noteOffset)
            }
        }
    }
}
