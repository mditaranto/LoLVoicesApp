package com.example.lolvoices.Vistas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.lolvoices.Components.Recurrentes.SearchBar
import com.example.lolvoices.FireBase.FireStoreBBDD
import com.example.lolvoices.R
import com.example.lolvoices.ui.theme.ColorDorado

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PuntuacionesScreen(navController: NavHostController) {

    var isSearchVisible by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    val puntuaciones = remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var isLoaded by remember {
        mutableStateOf(false)
    }

    // Fetch puntuaciones from Firestore
    LaunchedEffect(Unit) {
        FireStoreBBDD().getPuntuaciones { fetchedPuntuaciones ->
            puntuaciones.value = fetchedPuntuaciones
            isLoaded = true

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
                        SearchBar(
                            searchText = searchText,
                            onSearchTextChange = { searchText = it },
                            onSearchVisibilityChange = { isSearchVisible = it })
                    } else {
                        Text(
                            "TOP 25",
                            color = Color.White
                        )  // Cambia esto al título que desees mostrar
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
                    IconButton(onClick = { navController.popBackStack(); navController.popBackStack() }) {
                        Icon(Icons.Default.Home, contentDescription = "Agregar", tint = Color.White)

                    }
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.videogame),
                            contentDescription = "Actualizar",
                            tint = Color.White
                        )
                    }
                },
            )
        }, content = { innerpadding ->
            // Pantalla de juego
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .background(Color(0xFF021119))
            ) {
                // Fondo de pantalla
                Image(
                    painter = painterResource(id = R.drawable.humito),
                    contentDescription = "Fondo de pantalla",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.15f)
                )

                if (!isLoaded){
                    // Muestra un mensaje de carga
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(progress = 0.5f, color = ColorDorado)
                    }
                } else {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(innerpadding)
                    ) {
                        Divider(color = ColorDorado, thickness = 1.dp)
                        LazyColumn {
                            items(puntuaciones.value) { item ->
                                val puntuacion = item["puntuacion"] as? Long ?: 0
                                val usuario = item["usuario"] as? String ?: "Guess"
                                // Display puntuacion and usuario
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = "${puntuaciones.value.indexOf(item) + 1}",
                                        color = ColorDorado,
                                        modifier = Modifier.padding(end = 16.dp)
                                    )
                                    Text(
                                        text = usuario,
                                        modifier = Modifier.weight(3f),
                                        color = Color.White
                                    )
                                    Text(
                                        text = "$puntuacion",
                                        modifier = Modifier.weight(1f),
                                        color = ColorDorado
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}
