package com.example.lolvoices.Components.BotonesUtils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.lolvoices.Components.BotonesUtils.HexagonoNivel
import com.example.lolvoices.R

//Es un box que utilizo en el boton de estadisticas, es meramente estetico

//Se compone de un box dentro de otro con forma de circulos, a los que le pongo border y una foto.
@Composable
fun BoxForStats() {
    Box(
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .offset(x = 170.dp, y = (-10).dp)
                .background(Color.Transparent)
                .clip(CircleShape)
        ) {
            // Replace Icon with Box
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .rotate(90f)
                    .background(
                        brush = Brush.sweepGradient(
                            center = Offset.Unspecified,
                            colors = listOf(
                                Color(0xFF0495A8),
                                Color(0xFF0B323D)
                            ),
                        ),
                    )
                    .border(
                        width = 1.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF816328),
                                Color(0xFFB4985C)
                            )
                        ),
                        shape = CircleShape
                    )
            ) {
                Box(
                    modifier = Modifier
                        .rotate(-90f)
                        .fillMaxSize()
                        .padding(4.dp) // Espacio para crear el hueco
                        .border(
                            width = 1.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF816328),
                                    Color(0xFFB4985C)
                                )
                            ),
                            shape = CircleShape
                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.iconoinvocador),
                        contentDescription = "icono",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }
            }
        }
        HexagonoNivel()
    }
}