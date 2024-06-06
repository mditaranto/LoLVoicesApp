package com.example.lolvoices.Components

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.lolvoices.R
import com.example.lolvoices.dataClasses.ChampionAudio
import com.example.lolvoices.dataClasses.ChampionData
import com.example.lolvoices.room.Entidades.Audio
import com.example.lolvoices.ui.theme.ColorDorado
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ListaAudios(
    audioList: List<Audio>,
    setlazyListState: (LazyListState) -> Unit,
    scope: CoroutineScope,
    setSelectedAudio: (ChampionAudio) -> Unit
) {
    var isPlaying by remember { mutableStateOf(false) }
    var lazyListState by remember { mutableStateOf(LazyListState()) }

    LaunchedEffect(lazyListState) {
        setlazyListState(lazyListState)
    }
    LazyColumn(
        state = lazyListState,
        modifier = Modifier.fillMaxSize()
    ) {
        items(audioList.chunked(4)) { rowData ->
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
                        var isLoading by remember { mutableStateOf(false) }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)
                                .clickable(
                                    enabled = !isPlaying,
                                    onClick = {
                                        it.nombre
                                            ?.let { it1 ->
                                                it.url?.let { it2 ->
                                                    ChampionAudio(
                                                        nombre = it1,
                                                        url = it2
                                                    )
                                                }
                                            }
                                            ?.let { it2 -> setSelectedAudio(it2) }
                                        isPlaying = true
                                        progress = 0f
                                        isLoading = true
                                        scope.launch {
                                            AudioPlayer(
                                                url = it.url!!,
                                                onLoading = { loading ->
                                                    isLoading = loading
                                                },
                                                onProgress = { prog ->
                                                    progress = prog
                                                },
                                                onDurationRetrieved = { duration ->
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
                                            )

                                        }
                                    }
                                )
                        )
                        {
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
                                                width = 2.dp,
                                                brush = Brush.linearGradient(
                                                    colors = listOf(
                                                        if (!isPlaying && !isLoading ) Color(0xFFC0A17B) else Color.Transparent,
                                                        if (!isPlaying && !isLoading ) Color(0xFFD4AF37) else Color.Transparent
                                                    )
                                                ),
                                                shape = CircleShape
                                            )
                                            .clip(CircleShape)
                                    ) {
                                        // Barra de progreso circular o barra de carga
                                        if (isLoading) {
                                            LoadingIndicator()
                                        } else {
                                            ProgressIndicator(progress = progress)
                                        }
                                        // Segundo borde dorado fino
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(4.dp) // Espacio para crear el hueco
                                                .background(
                                                    brush = Brush.linearGradient(
                                                        colors = listOf(
                                                            Color(0xFFC0A17B),
                                                            Color(0xFFD4AF37)
                                                        )
                                                    ),
                                                    shape = CircleShape
                                                )
                                                .border(
                                                    width = 1.dp,
                                                    color = ColorDorado,
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
                                        }
                                    }
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
                    } ?: Spacer(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}