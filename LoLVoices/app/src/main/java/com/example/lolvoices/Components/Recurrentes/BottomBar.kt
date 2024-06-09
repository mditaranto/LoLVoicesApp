package com.example.lolvoices.Components.Recurrentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lolvoices.Components.AudioPlayerViewModel
import com.example.lolvoices.Modals.ChangeAudioName
import com.example.lolvoices.R
import com.example.lolvoices.RoomGestor
import com.example.lolvoices.dataClasses.ChampionAudio
import com.example.lolvoices.dataClasses.ChampionData
import kotlinx.coroutines.launch

// Barra de audio que se muestra en la parte inferior de la pantalla
// Contiene los botones para reproducir, pausar, detener, editar y agregar a favoritos
@Composable
fun BottomAudioBar(
    audio: ChampionAudio,
    selectedChampion: ChampionData,
    changeName: (String) -> Unit,
    viewModel: AudioPlayerViewModel = viewModel()
) {
    val audioViewModel: RoomGestor = viewModel()
    val oldName = audio.nombre
    var isFavorito by remember { mutableStateOf(false) }
    val isPlaying by viewModel.isPlaying(audio.url).observeAsState(false)
    val isPaused by viewModel.isPaused(audio.url).observeAsState(false)
    var showNameModal by remember { mutableStateOf(false) }

    LaunchedEffect(audio) {
        audio?.let {
            isFavorito = audioViewModel.comprobarFavorito(audio)
            if (isFavorito) {
                audio.nombre = audioViewModel.getAudioByUrl(audio.url).nombre.toString()
            } else {
                audio.nombre = oldName
            }
        }
    }

    val coroutineScope = rememberCoroutineScope()

    val icono = if (isFavorito) {
        Icons.Default.Favorite
    } else {
        Icons.Default.FavoriteBorder
    }

    val iconoPlay = when {
        isPlaying -> painterResource(R.drawable.pause)
        isPaused -> painterResource(R.drawable.play)
        else -> painterResource(R.drawable.replay)
    }

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
            if (showNameModal) {
                ChangeAudioName(
                    onDismiss = { showNameModal = false },
                    audioSeleccionado = audio,
                    changeName = changeName
                )
            }
            if (isPlaying || isPaused) {
                IconButton(onClick = { viewModel.stop(audio.url) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.stop),
                        contentDescription = "Detener",
                        tint = Color.White
                    )
                }
            }

            //Boton para parar y reproducir el audio
            IconButton(onClick = {
                if (isPlaying) {
                    viewModel.pause(audio.url)
                } else if (isPaused) {
                    viewModel.resume(audio.url)
                } else {
                    coroutineScope.launch {
                        viewModel.play(audio.url)
                    }
                }
            }) {
                Icon(painter = iconoPlay, contentDescription = "Reproducir", tint = Color.White)
            }
            if (isFavorito) {
                //Boton para cambiarle el nombre al audio
                IconButton(onClick = {
                    showNameModal = true
                }) {
                    Icon(
                        Icons.Default.Create,
                        contentDescription = "Cambiar nombre",
                        tint = Color.White
                    )
                }
            }
            //Boton para agregar o quitar de favoritos
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        if (isFavorito) {
                            audioViewModel.eliminarFavorito(audio)
                            isFavorito = false
                            changeName(oldName)
                            viewModel.stop(audio.url)
                        } else {
                            audioViewModel.agregarFavorito(selectedChampion, audio)
                            isFavorito = true
                        }
                    }
                }
            ) {
                Icon(icono, contentDescription = "Favoritos", tint = Color.White)
            }
        }
    }
}