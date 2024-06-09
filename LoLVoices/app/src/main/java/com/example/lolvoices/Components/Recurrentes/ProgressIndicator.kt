package com.example.lolvoices.Components.Recurrentes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.lolvoices.ui.theme.ColorDorado

// Indicador de progreso
// Se compone de un arco que se va llenando conforme el progreso del audio
@Composable
fun ProgressIndicator(progress: Float, width: Int = 4, played : Boolean = false) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1f)
    ) {
        drawArc(brush =
        if (!played) {
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFC0A17B),
                        Color(0xFFD4AF37)
                    )
                )
            } else {
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF021119),
                        Color(0xFF021119)
                    )
                )
            }
                ,
            startAngle = -90f,
            sweepAngle = 360 * progress,
            useCenter = false,
            style = Stroke(
                width = width.dp.toPx(),
                cap = StrokeCap.Round
            )
        )
    }
}