package com.example.lolvoices.Components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.lolvoices.ui.theme.ColorDorado

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
                .border(1.dp, ColorDorado)
        )
        // Box que sobresale por arriba y abajo
        Box(
            modifier = Modifier
                .size(width = (anchoC - 20).dp, height = (altoC + 10).dp)
                .align(Alignment.Center)
                .border(1.dp, ColorDorado)
        )
        content()
    }
}