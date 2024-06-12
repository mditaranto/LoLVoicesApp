package com.example.lolvoices.Components.Listas

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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lolvoices.ViewModels.AudioPlayerViewModel
import com.example.lolvoices.Components.Recurrentes.LoadingIndicator
import com.example.lolvoices.Components.Recurrentes.ProgressIndicator
import com.example.lolvoices.R
import com.example.lolvoices.dataClasses.ChampionAudio
import com.example.lolvoices.room.Entidades.Audio
import com.example.lolvoices.ui.theme.ColorDorado
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Es la lista que utilizo para mostrar los audios de un campeón,
// se compone de celdas que contienen un icono de play y el nombre del audio
// Al darle click a una celda se reproduce el audio
@Composable
fun ListaAudios(
    audioList: List<Audio>,
    scope: CoroutineScope,
    setSelectedAudio: (ChampionAudio) -> Unit,
    viewModel: AudioPlayerViewModel = viewModel()
) {

    LazyColumn(
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
                        val progress by viewModel.progress(item.url.toString())
                            .observeAsState(0f)
                        val isLoading by viewModel.isLoading(item.url.toString())
                            .observeAsState(false)
                        val isPlaying by viewModel.isPlaying(item.url.toString())
                            .observeAsState(false)

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
                                        if (progress == 0f) {
                                            scope.launch {
                                                viewModel.play(
                                                    url = it.url!!
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