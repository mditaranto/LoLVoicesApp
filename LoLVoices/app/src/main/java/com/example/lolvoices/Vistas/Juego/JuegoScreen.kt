package com.example.lolvoices.Vistas.Juego

import CustomButton
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.lolvoices.Components.AudioPlayerViewModel
import com.example.lolvoices.Components.Recurrentes.LoadingIndicator
import com.example.lolvoices.Components.Recurrentes.ProgressIndicator
import com.example.lolvoices.Funciones.manejarTurno
import com.example.lolvoices.Modals.EndGameAloneDialog
import com.example.lolvoices.Modals.EndGameDialog
import com.example.lolvoices.R
import com.example.lolvoices.dataClasses.ChampionAudio
import com.example.lolvoices.dataClasses.ChampionData
import com.example.lolvoices.ui.theme.ColorDorado
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun JuegoScreen(
    NavController: NavHostController,
    CampeonesInfo: List<ChampionData>,
    numJugadores: Int,
    viewModel: AudioPlayerViewModel = viewModel()
) {

    var showDialog by remember {
        mutableStateOf(false)
    }

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
    var played by remember {
        mutableStateOf(false)
    }
    val progress by viewModel.progress(audioPantalla.url).observeAsState(0f)
    val isLoading by viewModel.isLoading(audioPantalla.url).observeAsState(false)
    val isPlaying by viewModel.isPlaying(audioPantalla.url).observeAsState(false)

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
            if (erroresJugadores.count { it >= 3 } == numJugadores) {
                //Modal de fin de juego
                showDialog = true
            }
        }
    }

    // Filtrar la lista de campeones según el texto de búsqueda
    val filteredCampeones = CampeonesInfo.filter {
        it.nombre.contains(searchText, ignoreCase = true)
    }

    // Pantalla de juego
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
                Row(Modifier.height(50.dp)) {
                    for (i in 0 until erroresJugadores[jugadorActual]) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Corazon",
                            tint = Color.Red,
                            modifier = Modifier.size(30.dp),

                            )
                    }
                }

                if (showDialog) {
                    if (numJugadores == 1) {
                        EndGameAloneDialog(
                            onDismiss = { showDialog = false },
                            navController = NavController,
                            puntuacion = puntuacionesJugadores[0]
                        )
                    } else {
                        EndGameDialog(
                            onDismiss = { showDialog = false },
                            navController = NavController,
                            jugGanador = puntuacionesJugadores.indexOf(puntuacionesJugadores.maxOrNull()),
                            numJugadores = numJugadores,
                            puntuacion = puntuacionesJugadores.maxOrNull() ?: 0
                        )
                    }
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clickable(
                            enabled = !isPlaying,
                            onClick = {
                                audioPantalla.url.let { it1 ->
                                    scope.launch {
                                        viewModel.play(
                                            url = it1,
                                            Played = {if (!sigTurno) played = it}
                                            )
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
                            .background(brush =
                                if (played) {
                                    Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFFC0A17B),
                                            Color(0xFFD4AF37)
                                        )
                                    )
                                } else {
                                    Brush.linearGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Transparent
                                        )
                                    )
                                }
                            )
                            .border(
                                width = 4.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        if (!isPlaying && !isLoading && progress == 0f) Color(
                                            0xFFC0A17B
                                        ) else Color.Transparent,
                                        if (!isPlaying && !isLoading && progress == 0f) Color(
                                            0xFFD4AF37
                                        ) else Color.Transparent
                                    )
                                ),
                                shape = CircleShape
                            )
                    ) {
                        if (isLoading) {
                            LoadingIndicator(width = 8)
                        } else {
                            ProgressIndicator(progress = progress, width = 8, played = played)
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                                .clip(CircleShape)
                                .background(Color.Transparent)
                                .border(
                                    width = 2.dp,
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFFC0A17B),
                                            Color(0xFFD4AF37)
                                        )
                                    ),
                                    shape = CircleShape
                                )
                        ) {
                            if (sigTurno) {
                                Image(
                                    painter = rememberAsyncImagePainter(campeon.imagen),
                                    contentDescription = "Campeón ${campeon.nombre}",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_play),
                                    contentDescription = "Reproducir",
                                    tint = Color.White,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
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
                            Text(
                                text = "¡Respuesta correcta!",
                                color = Color.Green,
                                fontSize = 25.sp
                            )
                            Text(text = campeon.nombre, color = Color.White, fontSize = 20.sp)
                        } else {
                            Text(
                                text = "¡Respuesta incorrecta!",
                                color = Color.Red,
                                fontSize = 25.sp
                            )
                            Text(text = campeon.nombre, color = Color.White, fontSize = 20.sp)
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
                    .weight(if (!WindowInsets.isImeVisible) 1f else 0.5f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
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
                        cursorColor = ColorDorado,
                        focusedIndicatorColor = ColorDorado,
                        unfocusedIndicatorColor = ColorDorado
                    ),
                    enabled = !sigTurno
                )

                if (!WindowInsets.isImeVisible) {
                    CustomButton(
                        enabled = (searchText.isNotEmpty()),
                        text = ">",
                        ancho = 200,
                        alto = 50,
                        onClick = {
                            played = false
                            val (sigTurnoLocal, jugadorActualLocal, showDialogLocal) = manejarTurno(
                                searchText,
                                campeon.nombre,
                                puntuacionesJugadores,
                                erroresJugadores,
                                jugadorActual,
                                numJugadores,
                                sigTurno
                            )
                            sigTurno = sigTurnoLocal
                            jugadorActual = jugadorActualLocal
                            showDialog = showDialogLocal
                            if (!sigTurno) {
                                campeon = CampeonesInfo.random()
                                val audioAleat = campeon.audios.random()
                                audioPantalla = audioAleat
                                searchText = ""
                                played = false
                                viewModel.stopAll()
                            }
                        },
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
        }
    }
}

