package com.example.lolvoices.Vistas.Juego

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.lolvoices.AudioPlayer
import com.example.lolvoices.R
import com.example.lolvoices.dataClasses.ChampionAudio
import com.example.lolvoices.dataClasses.ChampionData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun JuegoScreen(
    NavController: NavHostController,
    CampeonesInfo: List<ChampionData>,
    numJugadores: Int
) {
    // Variables estandar
    var audioPantalla by remember {
        mutableStateOf(ChampionAudio("", ""))
    }
    var campeon by remember {
        mutableStateOf(ChampionData("", listOf(), ""))
    }

    // Variables de busqueda
    var searchText by remember {
        mutableStateOf("")
    }

    //Variables de reproduccion
    var isPlaying by remember {
        mutableStateOf(false)
    }
    var played by remember {
        mutableStateOf(false)
    }
    var progress by remember { mutableStateOf(0f) }
    val scope = rememberCoroutineScope()

    // Variables de UI
    var showLazyColumn by remember {
        mutableStateOf(false)
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Variables de juego
    var numJugadores by remember {
        mutableStateOf(numJugadores)
    }
    val puntuacionesJugadores = remember {
        mutableListOf<Int>()
    }
    val erroresJugadores = remember {
        mutableListOf<Int>()
    }
    for (i in numJugadores downTo 1) {
        puntuacionesJugadores.add(0)
        erroresJugadores.add(0)
    }
    var jugadorActual by remember {
        mutableStateOf(0)
    }
    var sigTurno by remember {
        mutableStateOf(false)
    }

    // Efectos de lanzamiento
    LaunchedEffect(searchText) {
        showLazyColumn = !CampeonesInfo.any { it.nombre.equals(searchText, ignoreCase = false) }
    }
    LaunchedEffect(Unit) {
        campeon = CampeonesInfo.random()
        val audioAleat = campeon.audios.random()
        audioPantalla = audioAleat

        numJugadores--
        if (numJugadores == 0) {
            numJugadores = 10
        }
    }

    LaunchedEffect(jugadorActual) {
        if (erroresJugadores[jugadorActual] >= 3) {
            jugadorActual++
            //Comprobar si se ha acabado el juego
        }
    }

    // Pantalla de juego
    // Filtrar la lista de campeones según el texto de búsqueda
    val filteredCampeones = CampeonesInfo.filter {
        it.nombre.contains(searchText, ignoreCase = true)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        // Fondo de pantalla
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = "Fondo de pantalla",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = "Jugador : ${jugadorActual + 1}",
                    style = TextStyle(color = Color.White),
                    modifier = Modifier.padding(8.dp)
                )

                Text(
                    text = "Puntuacion : ${puntuacionesJugadores[jugadorActual]}",
                    style = TextStyle(color = Color.White),
                    modifier = Modifier.padding(8.dp)
                )

            }

            // Botón de reproducción
            Column(Modifier.weight(3f), horizontalAlignment = Alignment.CenterHorizontally) {
                Row (Modifier.height(50.dp)){
                    for (i in 0 until erroresJugadores[jugadorActual]) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Corazon",
                            tint = Color.Red,
                            modifier = Modifier.size(30.dp),

                        )
                    }
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clickable(
                            enabled = !isPlaying,
                            onClick = {
                                isPlaying = true
                                progress = 0f
                                audioPantalla.url.let { it1 ->
                                    AudioPlayer(it1) { duration ->
                                        scope.launch {
                                            val step = 20L
                                            val steps = (duration / step).toInt()
                                            repeat(steps) {
                                                delay(step)
                                                progress += 1f / steps
                                            }
                                            progress = 0f
                                            isPlaying = false
                                            played = true
                                        }
                                    }
                                }
                            }
                        )
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(230.dp)
                            .clip(CircleShape)
                            .background(if (!played) Color.Transparent else Color(0xFFD4AF37))
                            .border(
                                width = 2.dp,
                                color = Color(0xFFD4AF37),
                                shape = CircleShape
                            )
                    ) {
                        if (sigTurno) {
                            Image(painter = rememberAsyncImagePainter(campeon.imagen),
                                contentDescription = "Campeón ${campeon.nombre}",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape))
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_play),
                                contentDescription = "Reproducir",
                                tint = Color.White,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }

                        Canvas(
                            modifier = Modifier
                                .fillMaxSize()
                                .zIndex(1f)
                        ) {
                            drawArc(
                                color = (if (!played) Color(0xFFD4AF37) else Color(0xFF021119)),
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
                if (sigTurno) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(14.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (searchText.uppercase() == campeon.nombre.uppercase()) {
                            Text(text = "¡Respuesta correcta!", color = Color.Green, fontSize = 25.sp)
                            Text(text = "${campeon.nombre}", color = Color.White, fontSize = 20.sp)
                        } else {
                            Text(text = "¡Respuesta incorrecta!", color = Color.Red, fontSize = 25.sp)
                            Text(text = "${campeon.nombre}", color = Color.White, fontSize = 20.sp)
                        }
                    }
                }
            }


            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Bottom
                    ) {
                if (searchText.isNotEmpty() && showLazyColumn) {
                    val itemHeight = 50.dp
                    val maxVisibleItems = 2
                    val listHeight =
                        itemHeight * minOf(filteredCampeones.size, maxVisibleItems)

                    LazyColumn(
                        modifier = Modifier
                            .height(listHeight)
                            .background(Color.Transparent),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        items(filteredCampeones) { campeon ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        searchText = campeon.nombre
                                        keyboardController?.hide() // Oculta el teclado
                                    }
                                    .padding(8.dp),
                            ) {
                                Text(
                                    text = campeon.nombre,
                                    color = Color.White,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Divider(color = Color(0xFFD4AF37), thickness = 1.dp)
                            }
                        }
                    }
                }
            }

            // TextField y botón abajo
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .imePadding()
            ) {


                TextField(
                    value = searchText,
                    onValueChange = { searchText = it},
                    modifier = Modifier
                        .fillMaxWidth(),
                    textStyle = TextStyle(color = Color.White),
                    placeholder = { Text("El campeon es...", color = Color.White) },
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
                        focusedIndicatorColor = Color(0xFFD4AF37),
                        unfocusedIndicatorColor = Color(0xFFD4AF37)
                    ),
                    enabled = !sigTurno
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (!WindowInsets.isImeVisible) {
                    Button(
                        onClick = {
                            if (!sigTurno) {
                                if (searchText.equals(
                                        campeon.nombre,
                                        ignoreCase = true
                                    )
                                ) {
                                    puntuacionesJugadores[jugadorActual] += 100
                                } else {
                                    puntuacionesJugadores[jugadorActual] -= 50
                                    erroresJugadores[jugadorActual]+=1;
                                }
                                sigTurno = true
                            } else {
                                sigTurno = false
                                if (jugadorActual == numJugadores - 1) {
                                    jugadorActual = 0
                                } else {
                                    jugadorActual++
                                }
                                campeon = CampeonesInfo.random()
                                val audioAleat = campeon.audios.random()
                                audioPantalla = audioAleat
                                searchText = ""
                                played = false
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(8.dp)
                    ) {
                        Text("Responder")
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))
            }
        }
    }
}
