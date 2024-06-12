package com.example.lolvoices.Vistas

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.lolvoices.ViewModels.AudioPlayerViewModel
import com.example.lolvoices.Components.Recurrentes.BottomAudioBar
import com.example.lolvoices.Components.Recurrentes.SearchBar
import com.example.lolvoices.Components.Listas.listaChuncked
import com.example.lolvoices.R
import com.example.lolvoices.dataClasses.ChampionAudio
import com.example.lolvoices.dataClasses.ChampionData
import com.example.lolvoices.ui.theme.ColorDorado

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CampeonesScreen(
    navController: NavHostController,
    CampeonesInfo: List<ChampionData>,
    viewModel: AudioPlayerViewModel = viewModel()
) {

    var searchText by remember { mutableStateOf("") }
    var isSearchVisible by remember { mutableStateOf(false) }

    var selectedAudio by remember { mutableStateOf<ChampionAudio?>(null) }
    val scope = rememberCoroutineScope()
    var selectedCampeon by remember { mutableStateOf<ChampionData?>(null) }

    var showNameModal by remember { mutableStateOf(false) }
    var nuevoNombre by remember { mutableStateOf("") }

    LaunchedEffect(showNameModal) {
        selectedAudio?.nombre ?: nuevoNombre
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
                        SearchBar(
                            searchText = searchText,
                            onSearchTextChange = { searchText = it },
                            onSearchVisibilityChange = { isSearchVisible = it })
                    } else {
                        Text(
                            "LoLVoices",
                            color = Color.White
                        )
                    }
                },
                actions = {
                    if (!isSearchVisible) {
                        IconButton(onClick = { isSearchVisible = true }) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Buscar",
                                tint = Color.White
                            )
                        }
                    }
                    IconButton(onClick = { navController.navigate("FavoritosScreen"); viewModel.stopAll() }) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Agregar",
                            tint = Color.White
                        )

                    }
                    IconButton(onClick = { navController.navigate("JueguitoScreen"); viewModel.stopAll() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.videogame),
                            contentDescription = "Actualizar",
                            tint = Color.White
                        )
                    }
                },
            )
        },
        bottomBar = {
            AnimatedVisibility (selectedAudio != null) {
                Column {
                    Divider(color = Color(0xFFC0A17B), thickness = 1.dp)
                    selectedCampeon?.let {
                        BottomAudioBar(
                            audio = selectedAudio!!,
                            selectedChampion = it,
                            changeName = { selectedAudio!!.nombre = it },
                            viewModel = viewModel,
                        )
                    }
                }
            }
        }, content = { innerpadding ->

            // Filtrar la lista de campeones según el texto de búsqueda
            val filteredCampeones = CampeonesInfo.sortedBy { it.nombre }.filter {
                it.nombre.contains(searchText, ignoreCase = true)
            }

            Column(
                Modifier
                    .padding(innerpadding)
                    .fillMaxSize()
            ) {
                Divider(color = ColorDorado, thickness = 1.dp)

                Box(modifier = Modifier.fillMaxSize()) {
                    // Fondo de pantalla
                    Image(
                        painter = painterResource(id = R.drawable.fondo),
                        contentDescription = "Fondo de pantalla",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Lista de campeones
                    listaChuncked(
                        filteredCampeones = filteredCampeones,
                        setSelectedAudio = { selectedAudio = it },
                        setSelectedChampion = { selectedCampeon = it },
                        scope = scope,
                        viewModel = viewModel
                    )

                }
            }
        }
    )
}

