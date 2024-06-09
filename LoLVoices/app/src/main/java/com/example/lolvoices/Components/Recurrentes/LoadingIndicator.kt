package com.example.lolvoices.Components.Recurrentes

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

// Indicador de carga
// Se compone de un arco que gira en el centro de la pantalla
@Composable
fun LoadingIndicator(width: Int = 4) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing)
        ), label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1f)
            .drawBehind {
                rotate(rotation) {
                    drawArc(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFC0A17B),
                                Color(0xFFD4AF37)
                            )
                        ),
                        startAngle = 0f,
                        sweepAngle = 90f,
                        useCenter = false,
                        style = Stroke(
                            width = width.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    )
                }
            }
    )
}