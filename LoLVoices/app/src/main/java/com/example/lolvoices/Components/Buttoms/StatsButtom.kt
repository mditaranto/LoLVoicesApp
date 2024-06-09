package com.example.lolvoices.Components.Buttoms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.lolvoices.Components.BotonesUtils.BoxForStats
import com.example.lolvoices.Components.BotonesUtils.CustomShape
import com.example.lolvoices.Components.BotonesUtils.CustomShapeStats

// Es igual al boton de jugar pero invertido y con un box estetico en vez de una foto
@Composable
fun StatsButtom(
    navController: NavHostController
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.offset(x = (-20).dp) // Adjust this value as needed to balance the layout
        ) {
            Button(
                onClick = { navController.navigate("PuntuacionesScreen") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                shape = CustomShape(),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .height(50.dp)
                    .width(200.dp)
                    .border(2.dp, Color(0xFFC0A17B), shape = CustomShapeStats())
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFC0A17B),
                                Color(0xFFD4AF37)
                            )
                        ),
                        shape = CustomShapeStats()
                    )
            ) {
                Text(
                    text = "TOP 25",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 25.dp)
                )
            }
            BoxForStats()
        }
    }
}