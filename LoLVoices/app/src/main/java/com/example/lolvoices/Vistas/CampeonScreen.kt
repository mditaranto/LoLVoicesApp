package com.example.lolvoices.Vistas

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.lolvoices.AgregarFav
import com.example.lolvoices.Components.AudioPlayer
import com.example.lolvoices.Components.BottomAudioBar
import com.example.lolvoices.Components.ListaAudios
import com.example.lolvoices.Components.LoadingIndicator
import com.example.lolvoices.Components.ProgressIndicator
import com.example.lolvoices.R
import com.example.lolvoices.dataClasses.ChampionAudio
import com.example.lolvoices.dataClasses.ChampionData
import com.example.lolvoices.room.Entidades.Audio
import com.example.lolvoices.room.Entidades.Campeon
import com.example.lolvoices.ui.theme.ColorDorado
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CampeonScreen(navController: NavHostController, campeon: String) {

    var champion by remember { mutableStateOf<Campeon?>(null) }
    var audios by remember { mutableStateOf<List<Audio>>(emptyList()) }

    val audioViewModel: AgregarFav = viewModel()

    var selectedAudio by remember { mutableStateOf<ChampionAudio?>(null) }
    val scope = rememberCoroutineScope()

    var showBottomBar by remember { mutableStateOf(true) }

    LaunchedEffect(campeon) {
        val retrievedChampion = audioViewModel.getCheapionByName(campeon)
        champion = retrievedChampion
        audios = audioViewModel.getAudioByChampion(champion!!.id).sortedBy { it.nombre }
    }


    Scaffold(
        topBar = {
            // Box para el TopBar personalizado
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1 / 4.5f * LocalConfiguration.current.screenHeightDp.dp)
                    .background(Color(0xFF021119))
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        champion?.imagen?.let { imagen ->
                            Image(
                                painter = rememberAsyncImagePainter(imagen),
                                contentDescription = champion?.nombre,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, ColorDorado, CircleShape)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        champion?.nombre?.let {
                            Text(it, color = ColorDorado, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Row {

                        IconButton(onClick = { navController.navigate("FavoritosScreen") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Favoritos", tint = Color.White)
                        }
                    }
                }
            }
        },
        content = {

            Column() {
                Spacer(modifier = Modifier.height(1 / 4.5f * LocalConfiguration.current.screenHeightDp.dp))
                Divider(color = ColorDorado, thickness = 1.dp)

                Box(modifier = Modifier.fillMaxSize()) {
                    // Fondo de pantalla
                    Image(
                        painter = painterResource(id = R.drawable.fondo),
                        contentDescription = "Fondo de pantalla", contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    // LazyColumn con 3 columnas para mostrar los datos actualizados
                    var lazyListState = rememberLazyListState()

                    // Detecta el desplazamiento y oculta o muestra la barra inferior
                    LaunchedEffect(lazyListState) {
                        snapshotFlow { lazyListState.firstVisibleItemScrollOffset }
                            .collect { scrollOffset ->
                                showBottomBar = scrollOffset == 0
                            }
                    }

                    ListaAudios(audioList = audios,
                        setlazyListState = { lazyListState = it },
                        setSelectedAudio = { selectedAudio = it },
                        scope = scope)
                }
            }
        },
        bottomBar = {
            if (selectedAudio != null && showBottomBar) {
                Column {
                    Divider(color = Color(0xFFC0A17B), thickness = 1.dp)
                    champion?.nombre?.let {
                        champion!!.imagen?.let { it1 ->
                            ChampionData(nombre = it,
                                imagen = it1
                            )?.let {
                                BottomAudioBar(
                                    audio = selectedAudio!!,
                                    selectedChampion = it,
                                )
                            }
                        }
                    }
                }
            }
        }
    )

}
