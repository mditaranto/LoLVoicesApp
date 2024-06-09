package com.example.lolvoices.Components.BotonesUtils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.lolvoices.ui.theme.ColorDorado

// Es un box que contiene un hexagono con un numero en su interior, meramente estitico para
// el box del btn de estadisticas
@Composable
fun HexagonoNivel() {
// Hexagon positioned at the bottom
    Box {
        Box(
            modifier = Modifier
                .size(38.dp, 15.dp)
                .align(Alignment.BottomCenter)
                .offset(y = 22.dp, x = 170.dp) // Adjust this to position the hexagon correctly
                .clip(CustomHexagonShape())
                .background(Color(0xFF021119))
                .border(1.dp, ColorDorado, CustomHexagonShape())
        ) {
            Text(
                text = "25",
                color = Color(0xFF9B9789),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

class CustomHexagonShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(Path().apply {
            val shortOffset = size.width * 0.15f
            val longOffset = size.width * 0.85f

            moveTo(shortOffset, 0f)
            lineTo(longOffset, 0f)
            lineTo(size.width, size.height * 0.5f)
            lineTo(longOffset, size.height)
            lineTo(shortOffset, size.height)
            lineTo(0f, size.height * 0.5f)
            close()
        })
    }
}