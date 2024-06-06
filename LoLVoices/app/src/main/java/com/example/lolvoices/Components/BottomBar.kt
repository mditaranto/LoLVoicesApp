package com.example.lolvoices.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lolvoices.AgregarFav
import com.example.lolvoices.dataClasses.ChampionAudio
import com.example.lolvoices.dataClasses.ChampionData
import kotlinx.coroutines.launch

@Composable
fun BottomAudioBar(
    audio: ChampionAudio,
    selectedChampion : ChampionData// Asegúrate de pasar el ViewModel como parámetro
) {
    val audioViewModel: AgregarFav = viewModel()
    var isFavorito by remember { mutableStateOf(false) }

    LaunchedEffect(audio) {
        audio?.let {
            isFavorito = audioViewModel.comprobarFavorito(audio!!)
        }
    }

    val coroutineScope = rememberCoroutineScope()

    val icono = if (isFavorito) {
        Icons.Default.Favorite
    } else {
        Icons.Default.FavoriteBorder
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
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        if (isFavorito) {
                            audioViewModel.eliminarFavorito(audio)
                            isFavorito = false
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