package com.example.lolvoices.Vistas

import android.annotation.SuppressLint
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CampeonScreen(navController: NavHostController, campeon: String) {
    Scaffold(
        topBar = {
            // Toolbar con 2 botones
            TopAppBar(
                title = { Text(text = campeon) },
                actions = {
                    IconButton(onClick = { /* Acción para el primer botón */ }) {
                        Icon(Icons.Default.Add, contentDescription = "Agregar")
                    }
                    IconButton(onClick = { /* Acción para el segundo botón */ }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Actualizar")
                    }
                }
            )
        },
        content = {

            Column {
                //Espacio
                Spacer(modifier = Modifier.height(60.dp))

                //Datos de prueba
                val tipos = listOf("Seleccion", "Ataque", "Muerte", "Provocacion", "Broma", "Risa")
                val datosPorTipo = listOf(
                    // Datos para el tipo "Seleccion"
                    listOf("Audio 1", "Audio 2", "Audio 3", "Audio 4"),
                    // Datos para el tipo "Ataque"
                    listOf("Audio 5", "Audio 6", "Audio 7"),
                    // Datos para el tipo "Muerte"
                    listOf("Audio 8", "Audio 9"),
                    // Datos para el tipo "Provocacion"
                    listOf("Audio 10"),
                    // Datos para el tipo "Broma"
                    listOf("Audio 11", "Audio 12", "Audio 13", "Audio 14", "Audio 15"),
                    // Datos para el tipo "Risa"
                    listOf("Audio 16", "Audio 17", "Audio 18")
                )
                val context = LocalContext.current

                // LazyColumn con 4 columnas para mostrar los datos actualizados
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray)
                ) {
                    datosPorTipo.forEachIndexed { index, tipoData ->
                        val tipo = tipos[index]

                        item {
                            Text(
                                text = tipo,
                                modifier = Modifier.padding(16.dp)
                            )
                        }

                        // LazyVerticalGrid con 4 columnas para mostrar los datos de cada tipo
                        items(tipoData.chunked(4)) { rowData ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                // Rellenar la fila con celdas vacías si no hay suficientes datos
                                val filledRowData = rowData + List(4 - rowData.size) { null }

                                filledRowData.forEach { item ->
                                    IconButton(
                                        onClick = {


                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .padding(8.dp)
                                            // Estilo del botón
                                            .background(
                                                color = if (item == null) Color.Transparent else Color.Blue,
                                                shape = CircleShape
                                            )
                                    ) {
                                        item?.let {
                                            Icon(
                                                imageVector = Icons.Default.PlayArrow,
                                                contentDescription = "Play",
                                                tint = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}
