package com.example.lolvoices.Vistas

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.graphics.Path
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.lolvoices.R
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.graphics.createBitmap
import coil.decode.ImageSource

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JueguitoScreen(navController: NavHostController) {

    Scaffold(
        topBar = {
            // Toolbar con 2 botones
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF021119),
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                modifier = Modifier.background(Color(0xFF021119)),
                title = {
                    Text(
                        "Guess The Voice",
                        color = Color.White
                    )  // Cambia esto al título que desees mostrar
                },
                actions = {
                    IconButton(onClick = { navController.navigate("CampeonesScreen") }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Agregar",
                            tint = Color.White
                        )
                    }
                },
            )
        },
        content = {
            //Columna con texto de explicacion del juego y 2 botones.
            Column(
                modifier = Modifier.padding(vertical = 2.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(61.dp))
                Divider(color = Color(0xFFC0A17B), thickness = 1.dp)

                Box (Modifier.fillMaxSize()){
                    Image(
                        painter = painterResource(id = R.drawable.fondo),
                        contentDescription = "Fondo de pantalla", contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(30.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Escucha la voz y adivina a qué campeón pertenece",
                            style = TextStyle(color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        Box(contentAlignment = Alignment.CenterStart) {
                            Button(
                                onClick = { /* TODO */ },
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
                                            colors = listOf(Color(0xFF021119), Color(0xFF00A2FF))
                                        ),
                                        shape = CustomShape()
                                    )
                            ) {
                                Text(
                                    text = "JUGAR",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }

                            Icon(
                                painter = painterResource(id = R.drawable.lol_logo),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(80.dp)
                                    .offset(x = (-40).dp),
                                tint = Color.Unspecified
                            )
                        }
                    }
                }
            }
        }
    )
}

class CustomShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ) = Outline.Generic(Path().apply {
        moveTo(0f, 0f)
        lineTo(size.width - 60f, 0f)
        lineTo(size.width, size.height / 2)
        lineTo(size.width - 60f, size.height)
        lineTo(0f, size.height)
        close()


    })
}

