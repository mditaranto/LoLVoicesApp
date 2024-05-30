package com.example.lolvoices.Vistas.Juego

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.lolvoices.AudioPlayer
import com.example.lolvoices.R
import com.example.lolvoices.dataClasses.ChampionAudio
import com.example.lolvoices.dataClasses.ChampionData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JuegoScreen(NavController: NavHostController, CampeonesInfo: List<ChampionData>, numJugadores: Int) {

    var audioPantalla by remember {
        mutableStateOf(ChampionAudio("", ""))
    }

    var campeon by remember {
        mutableStateOf(ChampionData("", listOf(), ""))
    }

    var searchText by remember {
        mutableStateOf("")
    }

    var isPlaying by remember {
        mutableStateOf(false)
    }

    var progress by remember { mutableStateOf(0f) }
    val scope = rememberCoroutineScope()

    var puntuacion by remember {
        mutableStateOf(0)
    }

    LaunchedEffect(Unit) {
        campeon = CampeonesInfo.random()
        val audioAleat = campeon.audios.random()
        audioPantalla = audioAleat
    }
    // Pantalla de juego
    // Filtrar la lista de campeones según el texto de búsqueda
    val filteredCampeones = CampeonesInfo.filter {
        it.nombre.contains(searchText, ignoreCase = true)
    }


    Column(
        modifier = Modifier.fillMaxSize().background(Color.Black)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Fondo de pantalla
            Image(
                painter = painterResource(id = R.drawable.fondo),
                contentDescription = "Fondo de pantalla",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Text(
                text = "Puntuación: $puntuacion",
                style = TextStyle(color = Color.White),
                modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
            )

            // Botón de reproducción
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .clickable(
                        enabled = !isPlaying,
                        onClick = {
                            isPlaying = true
                            progress = 0f
                            audioPantalla.url.let { it1 ->
                                AudioPlayer(it1) { duration ->
                                    scope.launch {
                                        val step = 50L
                                        val steps = (duration / step).toInt()
                                        repeat(steps) {
                                            delay(step)
                                            progress += 1f / steps
                                        }
                                        progress = 0f
                                        isPlaying = false
                                    }
                                }
                            }
                        }
                    )
            ) {
                Box(
                    modifier = Modifier
                        .size(250.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF021119))
                        .border(
                            width = 2.dp,
                            color = Color(0xFFD4AF37),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_play),
                        contentDescription = "Reproducir",
                        tint = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )

                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .zIndex(1f)
                    ) {
                        drawArc(
                            color = Color(0xFFD4AF37),
                            startAngle = -90f,
                            sweepAngle = 360 * progress,
                            useCenter = false,
                            style = Stroke(
                                width = 8.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.align(Alignment.BottomCenter).padding(8.dp)
            ) {
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(color = Color.White),
                    placeholder = { Text("Buscar...", color = Color.White) },
                    singleLine = true,
                    shape = MaterialTheme.shapes.small,
                    leadingIcon = {
                        IconButton(onClick = { searchText = "" }) {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = "Limpiar",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        cursorColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (searchText.equals(campeon.nombre, ignoreCase = true)) {
                            puntuacion += 100
                        } else {
                            puntuacion -= 50
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Responder")
                }
            }
        }
    }
}