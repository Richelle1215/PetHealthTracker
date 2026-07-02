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
    bodyColor: Color = Color.White,
    eyeColor: Color = Color(0xFFF48FB1), // Using this for accents (pink parts)
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

    // Arm swaying animation
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
                            } else if (dragAmount < -10) { // Swipe Left -> kanta
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
        
        val mainWhite = bodyColor
        val accentColor = eyeColor // Using this for the pink parts
        val screenBlack = Color(0xFF121212)
        val facePink = Color(0xFFFFD1E1)

        val totalYOffset = bobbingY + jumpOffset.value

        withTransform({
            translate(top = totalYOffset, left = danceX.value)
            rotate(degrees = danceRotation.value, pivot = Offset(w * 0.5f, h * 0.5f))
        }) {
            // 1. Body (Egg shape)
            drawOval(
                color = mainWhite,
                topLeft = Offset(w * 0.25f, h * 0.45f),
                size = Size(w * 0.5f, h * 0.5f)
            )

            // 2. Accent Collar/Bib
            val collarPath = Path().apply {
                moveTo(w * 0.35f, h * 0.45f)
                quadraticTo(w * 0.5f, h * 0.65f, w * 0.65f, h * 0.45f)
                close()
            }
            drawPath(path = collarPath, color = accentColor)

            // 3. Clothes
            if (hasClothes) {
                drawOval(
                    color = clothesColor,
                    topLeft = Offset(w * 0.25f, h * 0.65f),
                    size = Size(w * 0.5f, h * 0.3f)
                )
            }

            // 4. Curved Arms (Left)
            withTransform({
                rotate(degrees = armRotation, pivot = Offset(w * 0.35f, h * 0.55f))
            }) {
                val leftArmPath = Path().apply {
                    moveTo(w * 0.35f, h * 0.55f)
                    cubicTo(w * 0.15f, h * 0.55f, w * 0.1f, h * 0.45f, w * 0.18f, h * 0.35f)
                }
                drawPath(path = leftArmPath, color = mainWhite, style = Stroke(width = w * 0.12f, cap = StrokeCap.Round))
                drawPath(path = leftArmPath, color = accentColor, style = Stroke(width = w * 0.06f, cap = StrokeCap.Round))
            }

            // 5. Curved Arms (Right)
            withTransform({
                rotate(degrees = -armRotation, pivot = Offset(w * 0.65f, h * 0.55f))
            }) {
                val rightArmPath = Path().apply {
                    moveTo(w * 0.65f, h * 0.55f)
                    cubicTo(w * 0.85f, h * 0.55f, w * 0.9f, h * 0.45f, w * 0.82f, h * 0.35f)
                }
                drawPath(path = rightArmPath, color = mainWhite, style = Stroke(width = w * 0.12f, cap = StrokeCap.Round))
                drawPath(path = rightArmPath, color = accentColor, style = Stroke(width = w * 0.06f, cap = StrokeCap.Round))
            }

            // 6. Head
            drawOval(
                color = mainWhite,
                topLeft = Offset(w * 0.15f, h * 0.1f),
                size = Size(w * 0.7f, h * 0.42f)
            )

            // 7. Face Screen
            drawRoundRect(
                color = screenBlack,
                topLeft = Offset(w * 0.28f, h * 0.18f),
                size = Size(w * 0.44f, h * 0.26f),
                cornerRadius = CornerRadius(60f, 60f)
            )

            // 8. Eyes (with blinking)
            val eyeHeight = 0.1f * h * blinkScale
            val eyeTopOffset = (0.1f * h - eyeHeight) / 2

            if (hasSunglasses) {
                drawRect(color = Color.Black, topLeft = Offset(w * 0.28f, h * 0.23f), size = Size(w * 0.44f, h * 0.08f))
            } else {
                drawArc(
                    color = facePink,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(w * 0.35f, h * 0.23f + eyeTopOffset),
                    size = Size(w * 0.12f, eyeHeight)
                )
                drawArc(
                    color = facePink,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(w * 0.53f, h * 0.23f + eyeTopOffset),
                    size = Size(w * 0.12f, eyeHeight)
                )
            }
            
            // 9. Mouth (Smile/Sad)
            val mouthPath = Path().apply {
                val midX = w * 0.5f
                val mouthY = h * 0.35f
                moveTo(w * 0.45f, mouthY)
                if (isSmiling) {
                    quadraticTo(midX, mouthY + h * 0.03f, w * 0.55f, mouthY)
                } else {
                    quadraticTo(midX, mouthY - h * 0.03f, w * 0.55f, mouthY)
                }
            }
            drawPath(path = mouthPath, color = facePink, style = Stroke(width = 6f, cap = StrokeCap.Round))

            // 10. Singing notes
            if (isSinging) {
                val noteOffset = songProgress.value
                drawCircle(color = accentColor, center = Offset(w * 0.75f + noteOffset * 20f, h * 0.15f - noteOffset * 50f), radius = 8f * (1f - noteOffset), alpha = 1f - noteOffset)
            }

            // 11. Headphones (Band on head)
            drawArc(
                color = headphoneColor,
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(w * 0.15f, h * 0.05f),
                size = Size(w * 0.7f, h * 0.2f),
                style = Stroke(width = 10f)
            )
        }
    }
}
