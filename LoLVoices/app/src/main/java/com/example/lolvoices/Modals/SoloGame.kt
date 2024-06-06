package com.example.lolvoices.Modals

import CustomButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.lolvoices.Components.BordesDoraditos
import com.example.lolvoices.R
import com.example.lolvoices.ui.theme.ColorDorado


@Composable
fun EndGameAloneDialog(onDismiss: () -> Unit, navController: NavHostController, puntuacion: Int) {

    var botomenabled by remember { mutableStateOf(true) }


    Dialog(
        onDismissRequest = { onDismiss(); navController.navigate("JueguitoScreen") },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        BordesDoraditos(ancho = 310, alto = 470) {
            Box(
                modifier = Modifier
                    .background(Color(0xFF021119))
                    .width(300.dp)
                    .height(460.dp)
                    .border(2.dp, color = ColorDorado)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.lolnexo),
                    contentDescription = "Fondo de pantalla",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.3f)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Fin del juego",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp, top = 16.dp)
                    )

                    Text(
                        text = "$puntuacion puntos",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    Box(
                        modifier = Modifier
                            .height(200.dp)
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth()
                    ) {

                    }

                    Column (modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CustomButton(
                                text = "Salir",
                                onClick = { onDismiss(); navController.navigate("CampeonesScreen") },
                                ancho = 100, alto = 50
                            )

                            CustomButton(text = "Reintentar",
                                onClick = { onDismiss(); navController.navigate("JuegoScreen/2") },
                                ancho = 100, alto = 50)
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Bottom
                        ) {

                            CustomButton(text = "Guardar puntuación",
                                onClick = { botomenabled = false; },
                                ancho = 180, alto = 50,
                                enabled = botomenabled)

                        }
                    }
                }
            }
        }
    }
}