package com.example.lolvoices.Components.Recurrentes

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.example.lolvoices.ui.theme.ColorDorado

// Es un box que tiene bordes dorados, se utiliza para decorar dialogs y botones
// Son dos boxes, uno que sobresale por los lados y otro que sobresale por arriba y abajo
@Composable
fun BordesDoraditos(ancho : Int, alto : Int, content: @Composable () -> Unit) {
    val anchoC = ancho - 10
    val altoC = alto - 10
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(width = ancho.dp, height = alto.dp)
    ) {
        // Box que sobresale por los lados
        Box(
            modifier = Modifier
                .size(width = (anchoC + 10).dp, height = (altoC - 20).dp)
                .align(Alignment.Center)
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFC0A17B),
                            Color(0xFFD4AF37)
                        )
                    ),
                    shape = RectangleShape
                )
        )
        // Box que sobresale por arriba y abajo
        Box(
            modifier = Modifier
                .size(width = (anchoC - 20).dp, height = (altoC + 10).dp)
                .align(Alignment.Center)
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFC0A17B),
                            Color(0xFFD4AF37)
                        )
                    ),
                    shape = RectangleShape
                )
        )
        content()
    }
}