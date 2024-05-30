package com.example.lolvoices.Vistas

import android.annotation.SuppressLint
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.lolvoices.AgregarFav
import com.example.lolvoices.AudioPlayer
import com.example.lolvoices.R
import com.example.lolvoices.dataClasses.ChampionAudio
import com.example.lolvoices.dataClasses.ChampionData
import com.example.lolvoices.room.Entidades.Audio
import com.example.lolvoices.room.Entidades.Campeon
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CampeonScreen(navController: NavHostController, campeon: String) {
    var champion by remember { mutableStateOf<Campeon?>(null) }
    var audios by remember { mutableStateOf<List<Audio>>(emptyList()) }

    val audioViewModel: AgregarFav = viewModel()
    val coroutineScope = rememberCoroutineScope()

    var isPlaying by remember { mutableStateOf(false) }
    var selectedAudio by remember { mutableStateOf<ChampionAudio?>(null) }
    val scope = rememberCoroutineScope()
    var selectedCampeon by remember { mutableStateOf<ChampionData?>(null) }
    var isFavorito by remember { mutableStateOf(false) }

    var showBottomBar by remember { mutableStateOf(true) }

    LaunchedEffect(campeon) {
        val retrievedChampion = audioViewModel.getCheapionByName(campeon)
        champion = retrievedChampion
        audios = audioViewModel.getAudioByChampion(champion!!.id)
    }

    LaunchedEffect(selectedAudio) {
        selectedAudio?.let {
            isFavorito = audioViewModel.comprobarFavorito(selectedAudio!!)
        }
    }

    Scaffold(
        topBar = {
            // Box para el TopBar personalizado
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1 / 6f * LocalConfiguration.current.screenHeightDp.dp)
                    .background(Color(0xFF021119))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        champion?.imagen?.let { imagen ->
                            Image(
                                painter = rememberAsyncImagePainter(imagen),
                                contentDescription = champion?.nombre,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color(0xFFD4AF37), CircleShape)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        champion?.nombre?.let {
                            Text(it, color = Color(0xFFD4AF37), fontSize = 30.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Row {

                        IconButton(onClick = { navController.navigate("FavoritosScreen") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Favoritos", tint = Color.White)
                        }
                    }
                }
            }
        },
        content = {

            Column() {
                Spacer(modifier = Modifier.height(1 / 6f * LocalConfiguration.current.screenHeightDp.dp))
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
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        items(audios.chunked(4)) { rowData ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                // Rellenar la fila con celdas vacías si no hay suficientes datos
                                val filledRowData = rowData + List(4 - rowData.size) { null }

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
                                                        selectedAudio = item!!.url?.let { it1 ->
                                                            item!!.nombre?.let { it2 ->
                                                                ChampionAudio(
                                                                    nombre = it2,
                                                                    url = it1
                                                                )
                                                            }
                                                        }
                                                        isPlaying = true
                                                        progress = 0f
                                                        it.url?.let { it1 ->
                                                            AudioPlayer(it1) { duration ->
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
                                                    }
                                                )
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .aspectRatio(1f)
                                                    .clip(CircleShape)
                                                    .background(Color(0xFF021119))
                                                    .border(
                                                        width = 2.dp,
                                                        color = Color(0xFFD4AF37),
                                                        shape = CircleShape
                                                    )
                                            ) {
                                                // Icono de reproducción
                                                Icon(
                                                    painter = painterResource(id = R.drawable.ic_play), // Asegúrate de que tienes este recurso
                                                    contentDescription = "Reproducir",
                                                    tint = Color.White,
                                                    modifier = Modifier.align(Alignment.Center)
                                                )

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
                                            item.nombre?.let { it1 ->
                                                Text(
                                                    text = it1,
                                                    color = Color.White,
                                                    modifier = Modifier.padding(top = 5.dp)
                                                )
                                            }
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
