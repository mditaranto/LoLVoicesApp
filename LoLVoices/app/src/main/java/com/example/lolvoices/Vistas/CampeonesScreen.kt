package com.example.lolvoices.Vistas

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.lolvoices.AgregarFav
import com.example.lolvoices.AudioPlayer
import com.example.lolvoices.MainActivity
import com.example.lolvoices.R
import com.example.lolvoices.dataClasses.ChampionAudio
import com.example.lolvoices.dataClasses.ChampionData
import com.example.lolvoices.room.Entidades.Audio
import com.example.lolvoices.room.Entidades.Campeon
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CampeonesScreen(navController: NavHostController, CampeonesInfo: List<ChampionData>) {

    val audioViewModel: AgregarFav = viewModel()
    val coroutineScope = rememberCoroutineScope()

    var searchText by remember { mutableStateOf("") }
    var isSearchVisible by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    var selectedAudio by remember { mutableStateOf<ChampionAudio?>(null) }
    val scope = rememberCoroutineScope()
    var selectedCampeon by remember { mutableStateOf<ChampionData?>(null) }
    var isFavorito by remember { mutableStateOf(false) }

    var showBottomBar by remember { mutableStateOf(true) }

    LaunchedEffect(selectedAudio) {
        selectedAudio?.let {
            isFavorito = audioViewModel.comprobarFavorito(selectedAudio!!)
        }
    }

    Scaffold(
        topBar = {
            // Toolbar con 2 botones
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = Color(0xFF021119),
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                modifier = Modifier.background(Color(0xFF021119)),
                title = {
                    if (isSearchVisible) {
                        TextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(color = Color.White),
                            placeholder = { Text("Buscar...", color = Color.White) },
                            singleLine = true,
                            shape = MaterialTheme.shapes.small,
                            leadingIcon = {
                                IconButton(onClick = { isSearchVisible = false; searchText = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Limpiar", tint = Color.White)
                                }
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                            )
                        )
                    } else {
                        Text("LoLVoices", color = Color.White)  // Cambia esto al título que desees mostrar
                    }
                },
                actions = {
                    if (!isSearchVisible) {
                        IconButton(onClick = { isSearchVisible = true }) {
                            Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.White)
                        }
                    }
                    IconButton(onClick = { navController.navigate("FavoritosScreen") }) {
                        Icon(Icons.Default.Star, contentDescription = "Agregar", tint = Color.White)
                    }
                    IconButton(onClick = { navController.navigate("JueguitoScreen") }) {
                        Icon(painter = painterResource(id = R.drawable.videogame), contentDescription = "Actualizar", tint = Color.White)
                    }
                },
            )
        },
        content = {
            // Filtrar la lista de campeones según el texto de búsqueda
            val filteredCampeones = CampeonesInfo.filter {
                it.nombre.contains(searchText, ignoreCase = true)
            }

            Column() {
                Spacer(modifier = Modifier.height(97.dp))
                Divider(color = Color(0xFFC0A17B), thickness = 1.dp)

                Box(modifier = Modifier.fillMaxSize()) {
                    // Fondo de pantalla
                    Image(
                        painter = painterResource(id = R.drawable.fondo),
                        contentDescription = "Fondo de pantalla", contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    // LazyColumn con 3 columnas para mostrar los datos actualizados
                    val lazyListState = rememberLazyListState()

                    // Detecta el desplazamiento y oculta o muestra la barra inferior
                    LaunchedEffect(lazyListState) {
                        snapshotFlow { lazyListState.firstVisibleItemScrollOffset }
                            .collect { scrollOffset ->
                                showBottomBar = scrollOffset == 0
                            }
                    }

                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredCampeones.chunked(3)) { rowData ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                // Rellenar la fila con celdas vacías si no hay suficientes datos
                                val filledRowData = rowData + List(3 - rowData.size) { null }

                                filledRowData.forEach { item ->
                                    item?.let {
                                        var progress by remember { mutableStateOf(0f) }
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(8.dp)
                                                .clickable(
                                                    enabled = !isPlaying,
                                                    onClick = {
                                                        val randomAudio = it.audios.random()
                                                        selectedCampeon = it
                                                        selectedAudio = randomAudio
                                                        isPlaying = true
                                                        progress = 0f
                                                        AudioPlayer(randomAudio.url) { duration ->
                                                            scope.launch {
                                                                val step = 50L
                                                                val steps =
                                                                    (duration / step).toInt()
                                                                repeat(steps) {
                                                                    delay(step)
                                                                    progress += 1f / steps
                                                                }
                                                                progress = 0f
                                                                isPlaying = false
                                                            }
                                                        }
                                                    }
                                                )
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .aspectRatio(1f)
                                                    .clip(CircleShape)
                                                    .background(Color.Transparent)
                                            ) {
                                                Box(
                                                    modifier = Modifier.fillMaxSize()
                                                ) {
                                                    // Primer borde dorado grueso
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                            .padding(4.dp) // Pequeño hueco
                                                            .background(Color.Transparent)
                                                            .border(
                                                                width = 1.dp,
                                                                brush = Brush.linearGradient(
                                                                    colors = listOf(
                                                                        Color(0xFFC0A17B),
                                                                        Color(0xFFD4AF37)
                                                                    )
                                                                ),
                                                                shape = CircleShape
                                                            )
                                                            .clip(CircleShape)
                                                    ) {
                                                        // Segundo borde dorado fino
                                                        Box(
                                                            modifier = Modifier
                                                                .fillMaxSize()
                                                                .padding(2.dp) // Espacio para crear el hueco
                                                                .background(
                                                                    brush = Brush.linearGradient(
                                                                        colors = listOf(
                                                                            Color(0xFFC0A17B),
                                                                            Color(0xFFD4AF37)
                                                                        )
                                                                    ),
                                                                    shape = CircleShape
                                                                )
                                                        ) {
                                                            Image(
                                                                painter = rememberAsyncImagePainter(item.imagen),
                                                                contentDescription = "Campeón ${item.nombre}",
                                                                contentScale = ContentScale.Crop,
                                                                modifier = Modifier
                                                                    .fillMaxSize()
                                                                    .clip(CircleShape)
                                                            )
                                                        }
                                                    }
                                                }

                                                // Barra de progreso circular
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

                                            // Nombre del campeón
                                            Text(
                                                text = item.nombre,
                                                color = Color.White,
                                                modifier = Modifier.padding(top = 5.dp)
                                            )
                                        }
                                    } ?: Spacer(modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .padding(8.dp))
                                }
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            if (selectedAudio != null && showBottomBar) {
                Column {
                    Divider(color = Color(0xFFC0A17B), thickness = 1.dp)
                    BottomAudioBar(
                        audio = selectedAudio!!,
                        onFavoriteClick = {
                            coroutineScope.launch {
                                if (isFavorito) {
                                    audioViewModel.eliminarFavorito(selectedAudio!!)
                                    isFavorito = false
                                } else {
                                    selectedCampeon?.let {
                                        audioViewModel.agregarFavorito(it, selectedAudio!!)
                                    }
                                    isFavorito = true
                                }
                            }
                        },
                        icono = if (isFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder
                    )
                }
            }
        }
    )
}

@Composable
fun BottomAudioBar(audio: ChampionAudio, onFavoriteClick: () -> Unit, icono: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF021119))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = audio.nombre,
            color = Color.White,
            modifier = Modifier.weight(1f)
        )
        Row {
            IconButton(onClick = onFavoriteClick) {
                Icon(icono, contentDescription = "Favoritos", tint = Color.White)
            }
        }
    }
}