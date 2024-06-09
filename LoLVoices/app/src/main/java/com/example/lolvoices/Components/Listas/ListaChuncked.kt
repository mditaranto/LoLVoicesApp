package com.example.lolvoices.Components.Listas

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.lolvoices.Components.AudioPlayerViewModel
import com.example.lolvoices.Components.Recurrentes.LoadingIndicator
import com.example.lolvoices.Components.Recurrentes.ProgressIndicator
import com.example.lolvoices.dataClasses.ChampionAudio
import com.example.lolvoices.dataClasses.ChampionData
import com.example.lolvoices.ui.theme.ColorDorado
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Es la lista que utilizo para mostrar los campeones, se compone de celdas que contienen
// la imagen del campeón y su nombre
// Al darle click a una celda se reproduce un audio aleatorio del campeón
@Composable
fun listaChuncked(
    filteredCampeones: List<ChampionData>,
    scope: CoroutineScope,
    setSelectedChampion: (ChampionData) -> Unit,
    setSelectedAudio: (ChampionAudio) -> Unit,
    viewModel : AudioPlayerViewModel = viewModel()
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),

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

                        var randomAudio by remember { mutableStateOf(item.audios.random()) }
                        val progress by viewModel.progress(randomAudio.url).observeAsState(0f)
                        val isLoading by viewModel.isLoading(randomAudio.url).observeAsState(false)
                        val isPlaying by viewModel.isPlaying(randomAudio.url).observeAsState(false)

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)
                                .clickable(
                                    enabled = !isPlaying,
                                    onClick = {
                                        setSelectedAudio(randomAudio)
                                        if (progress == 0f) {
                                            randomAudio = item.audios.random()
                                            setSelectedChampion(it)
                                            setSelectedAudio(randomAudio)
                                            scope.launch {
                                                viewModel.play(
                                                    url = randomAudio.url
                                                )
                                            }
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
                                            Image(
                                                painter = rememberAsyncImagePainter(
                                                    item.imagen
                                                ),
                                                contentDescription = "Campeón ${item.nombre}",
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .clip(CircleShape)
                                            )
                                        }
                                    }
                                }
                            }

                            // Nombre del campeón
                            Text(
                                text = item.nombre,
                                color = Color.White,
                                modifier = Modifier.padding(top = 5.dp)
                            )
                        }
                    } ?: Spacer(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(8.dp))
                }
            }
        }
    }
}