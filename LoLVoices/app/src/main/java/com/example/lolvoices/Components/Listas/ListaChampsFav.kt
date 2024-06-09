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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.lolvoices.room.Entidades.Campeon
import com.example.lolvoices.ui.theme.ColorDorado

@Composable
fun ListaChampsFav(
    filteredCampeones: List<Campeon>,
    navController: NavController
) {
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
                            .padding(8.dp))
                }
            }
        }
    }
}