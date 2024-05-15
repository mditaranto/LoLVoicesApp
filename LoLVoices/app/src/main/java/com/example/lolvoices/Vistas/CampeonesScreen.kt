package com.example.lolvoices.Vistas

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.lolvoices.AudioPlayer
import com.example.lolvoices.R
import com.example.lolvoices.dataClasses.ChampionData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CampeonesScreen(navController: NavHostController, CampeonesInfo: List<ChampionData>) {

    var searchText by remember { mutableStateOf("") }

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
                    TextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(color = Color.White),
                        placeholder = { Text("Buscar...", color = Color.White) },
                        singleLine = true,
                        shape = MaterialTheme.shapes.small,
                        leadingIcon = {
                            IconButton(onClick = { /* Handle search action */ }) {
                                Icon(Icons.Default.Search, contentDescription = "Buscar")
                            }
                        },
                        trailingIcon = {
                            IconButton(onClick = { /* Handle clear action */ }) {
                                Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                            }
                        }
                    )
                },
                actions = {

                        IconButton(onClick = { navController.navigate("FavoritosScreen") }) {
                            Icon(Icons.Default.Add, contentDescription = "Agregar", tint = Color.White)
                        }
                        IconButton(onClick = { navController.navigate("JueguitoScreen") }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Actualizar", tint = Color.White)
                        }
                },
            )
        },
        content = {

            var isPlaying by remember { mutableStateOf(false) }

            val scope = rememberCoroutineScope()
            var audioAleatorio by remember { mutableStateOf("") }

            Column (modifier = Modifier.padding(vertical = 8.dp)) {
                Spacer(modifier = Modifier.height(60.dp))
                Divider(color = Color(0xFFC0A17B), thickness = 1.dp)

                Box(modifier = Modifier.fillMaxSize()) {
                    // Fondo de pantalla
                    Image(painter = painterResource(id = R.drawable.fondo),
                        contentDescription = "Fondo de pantalla", contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    // LazyColumn con 3 columnas para mostrar los datos actualizados
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()

                    ) {
                        // LazyVerticalGrid con 3 columnas para mostrar los datos de cada tipo
                        items(CampeonesInfo.chunked(3)) { rowData ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                // Rellenar la fila con celdas vacías si no hay suficientes datos
                                val filledRowData = rowData + List(3 - rowData.size) { null }

                                filledRowData.forEach { item ->
                                    item?.let {
                                        var progress by remember { mutableStateOf(0f) }
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .aspectRatio(1f)
                                                .padding(8.dp)
                                                .clip(CircleShape)
                                                .background(Color.Transparent)
                                                .clickable(
                                                    enabled = !isPlaying,
                                                    onClick = {
                                                        val urlsCampeon = item.audios.map { it.url }
                                                        if (urlsCampeon.isNotEmpty()) {
                                                            audioAleatorio = urlsCampeon.random()
                                                            isPlaying = true
                                                            progress = 0f
                                                            AudioPlayer(audioAleatorio) { duration ->
                                                                scope.launch {
                                                                    val step = 50L
                                                                    val steps = (duration / step).toInt()
                                                                    repeat(steps) {
                                                                        delay(step)
                                                                        progress += 1f / steps
                                                                    }
                                                                    isPlaying = false
                                                                    progress = 0f
                                                                }
                                                            }
                                                        }
                                                    }
                                                )
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                            ) {
                                                // Primer borde dorado grueso
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .padding(4.dp) // Pequeño hueco
                                                        .background(Color.Transparent)
                                                        .border(
                                                            width = 1.dp,
                                                            brush = Brush.linearGradient(
                                                                colors = listOf(Color(0xFFC0A17B), Color(0xFFD4AF37))
                                                            ),
                                                            shape = CircleShape
                                                        )
                                                        .clip(CircleShape)
                                                ) {
                                                    // Segundo borde dorado fino
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                            .padding(2.dp) // Espacio para crear el hueco
                                                            .background(
                                                                brush = Brush.linearGradient(
                                                                    colors = listOf(Color(0xFFC0A17B), Color(0xFFD4AF37))
                                                                ),
                                                                shape = CircleShape
                                                            )
                                                    ) {
                                                        Image(
                                                            painter = rememberAsyncImagePainter(item.imagen),
                                                            contentDescription = "Campeón ${item.nombre}",
                                                            contentScale = ContentScale.Crop,
                                                            modifier = Modifier
                                                                .fillMaxSize()
                                                                .clip(CircleShape)
                                                        )
                                                    }
                                                }
                                            }

                                            // Barra de progreso circular
                                            Canvas(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .zIndex(1f)
                                            ) {
                                                drawArc(
                                                    color = Color(0xFFD4AF37),
                                                    startAngle = -90f,
                                                    sweepAngle = 360 * progress,
                                                    useCenter = false,
                                                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                                                        width = 8.dp.toPx(),
                                                        cap = StrokeCap.Round
                                                    )
                                                )
                                            }
                                        }
                                    } ?: Spacer(modifier = Modifier.weight(1f).aspectRatio(1f).padding(8.dp))
                                }
                            }
                        }
                    }
                }
            }
        },
    )
}