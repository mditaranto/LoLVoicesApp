package com.example.lolvoices.Vistas

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.lolvoices.Components.Listas.ListaChampsFav
import com.example.lolvoices.Components.Recurrentes.SearchBar
import com.example.lolvoices.R
import com.example.lolvoices.ViewModels.RoomGestor
import com.example.lolvoices.dataClasses.ChampionAudio


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritosScreen(navController: NavHostController) {
    val audioViewModel: RoomGestor = viewModel()
    val campeones = audioViewModel.getChampion()

    //LaunchedEffect para borrar los campeones sin audios
    LaunchedEffect(Unit) {
        for (campeon in campeones) {
            val audios = audioViewModel.getAudioByChampion(campeon.id)
            if (audios.isEmpty()) {
                audioViewModel.eliminarCampeon(campeon)
            }
        }
    }

    var searchText by remember { mutableStateOf("") }
    var isSearchVisible by remember { mutableStateOf(false) }
    var selectedAudio by remember { mutableStateOf<ChampionAudio?>(null) }
    var isFavorito by remember { mutableStateOf(false) }

    // Comprobar si el audio seleccionado es favorito
    LaunchedEffect(selectedAudio) {
        selectedAudio?.let {
            isFavorito = audioViewModel.comprobarFavorito(selectedAudio!!)
        }
    }

    Scaffold(
        topBar = {
            // Toolbar con 2 botones
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF021119),
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                modifier = Modifier.background(Color(0xFF021119)),
                title = {
                    if (isSearchVisible) {
                        SearchBar(
                            searchText = searchText,
                            onSearchTextChange = { searchText = it },
                            onSearchVisibilityChange = { isSearchVisible = it}
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
                    IconButton(onClick = { navController.popBackStack()}) {
                        Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White)
                    }
                    IconButton(onClick = {  navController.popBackStack(); navController.navigate("JueguitoScreen")}) {
                        Icon(painter = painterResource(id = R.drawable.videogame), contentDescription = "Juego", tint = Color.White)
                    }
                },

                )
        },
        content = { innerpadding ->

            // Filtrar la lista de campeones según el texto de búsqueda
            val filteredCampeones = campeones.sortedBy { it.nombre }.filter {
                it.nombre?.contains(searchText, ignoreCase = true) ?: true
            }

            // LazyColumn con 3 columnas para mostrar los datos actualizados

            Column (
                Modifier
                    .padding(innerpadding)
                    .fillMaxSize()) {
                Divider(color = Color(0xFFC0A17B), thickness = 1.dp)

                Box(modifier = Modifier.fillMaxSize()) {
                    // Fondo de pantalla

                    Image(painter = painterResource(id = R.drawable.fondo),
                        contentDescription = "Fondo de pantalla", contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    ListaChampsFav(filteredCampeones = filteredCampeones,
                        navController = navController)
                }
            }
        }
    )
}