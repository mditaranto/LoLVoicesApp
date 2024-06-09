package com.example.lolvoices.Components.Buttoms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lolvoices.Components.BotonesUtils.CustomShape
import com.example.lolvoices.R

// Boton para jugar, tiene una forma de flecha y un icono del juego en la izquierda
@Composable
fun JugarButtom(
    setShowDialog: (Boolean) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Box(
            modifier = Modifier.offset(x = 20.dp) // Adjust this value as needed to balance the layout
        ) {
            Button(
                onClick = { setShowDialog(true) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                shape = CustomShape(),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .height(50.dp)
                    .width(200.dp)
                    .border(2.dp, Color(0xFF0EAAD2), shape = CustomShape())
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF021119),
                                Color(0xFF00A2FF)
                            )
                        ),
                        shape = CustomShape()
                    )
            ) {
                Text(
                    text = "JUGAR",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 25.dp)
                )
            }

            Icon(
                painter = painterResource(id = R.drawable.lol_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .offset(x = (-40).dp, y = (-15).dp),
                tint = Color.Unspecified
            )
        }
    }
}