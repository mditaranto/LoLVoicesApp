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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.lolvoices.AgregarFav
import com.example.lolvoices.Components.SearchBar
import com.example.lolvoices.R
import com.example.lolvoices.dataClasses.ChampionAudio
import com.example.lolvoices.ui.theme.ColorDorado


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritosScreen(navController: NavHostController) {

    val audioViewModel: AgregarFav = viewModel()
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
                    IconButton(onClick = { navController.navigate("CampeonesScreen") }) {
                        Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White)
                    }
                    IconButton(onClick = { navController.navigate("JueguitoScreen") }) {
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

            Column ( Modifier.padding(innerpadding)
                .fillMaxSize()) {
                Divider(color = Color(0xFFC0A17B), thickness = 1.dp)

                Box(modifier = Modifier.fillMaxSize()) {
                    // Fondo de pantalla

                    Image(painter = painterResource(id = R.drawable.fondo),
                        contentDescription = "Fondo de pantalla", contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    // LazyColumn con 3 columnas para mostrar los datos actualizados
                    LazyColumn(
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
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(8.dp)
                                                .clickable(
                                                    onClick = { navController.navigate("CampeonScreen/${item.nombre}") }
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
                                                                width = 2.dp,
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
                                                                    shape = CircleShape)
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
        }
    )
}