package com.example.lolvoices.Vistas

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Divider
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.lolvoices.ViewModels.AudioPlayerViewModel
import com.example.lolvoices.Components.Recurrentes.BottomAudioBar
import com.example.lolvoices.Components.Listas.ListaAudios
import com.example.lolvoices.R
import com.example.lolvoices.ViewModels.RoomGestor
import com.example.lolvoices.dataClasses.ChampionAudio
import com.example.lolvoices.dataClasses.ChampionData
import com.example.lolvoices.room.Entidades.Audio
import com.example.lolvoices.room.Entidades.Campeon
import com.example.lolvoices.ui.theme.ColorDorado

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CampeonScreen(navController: NavHostController, campeon: String, viewModel : AudioPlayerViewModel = viewModel()) {

    var champion by remember { mutableStateOf<Campeon?>(null) }
    var audios by remember { mutableStateOf<List<Audio>>(emptyList()) }

    val audioViewModel: RoomGestor = viewModel()
    var changeName by remember {
        mutableStateOf("")
    }

    var selectedAudio by remember { mutableStateOf<ChampionAudio?>(null) }
    val scope = rememberCoroutineScope()

    // Fetch champion and audios from Room
    LaunchedEffect(campeon, changeName) {
        val retrievedChampion = audioViewModel.getCheapionByName(campeon)
        champion = retrievedChampion
        audios = audioViewModel.getAudioByChampion(champion!!.id).sortedBy { it.nombre }
    }

    //Scaffold que tiene el topbar y el bottombar
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                ) {
                    champion?.imagen?.let { imagen ->
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .height(120.dp)
                                .width(120.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(6.dp) // Pequeño hueco
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
                                Image(
                                    painter = rememberAsyncImagePainter(imagen),
                                    contentDescription = champion?.nombre,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(CircleShape)
                                        .border(1.dp, ColorDorado, CircleShape)
                                        .padding(4.dp)
                                        .align(Alignment.Center)
                                )
                            }
                        }
                        Column ( verticalArrangement = Arrangement.Center,
                            modifier = Modifier.weight(3f)){
                            champion?.nombre?.let {
                                Text(
                                    it,
                                    color = ColorDorado,
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Row (modifier = Modifier.weight(1f)){
                        IconButton(onClick = { navController.popBackStack(); viewModel.stopAll() }) {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = "Favoritos",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            if (selectedAudio != null) {
                Column {
                    Divider(color = Color(0xFFC0A17B), thickness = 1.dp)
                    champion?.nombre?.let {
                        champion!!.imagen?.let { it1 ->
                            ChampionData(
                                nombre = it,
                                imagen = it1
                            ).let { itn ->
                                BottomAudioBar(
                                    audio = selectedAudio!!,
                                    selectedChampion = itn,
                                    changeName = { changeName = it },
                                    viewModel = viewModel
                                )
                            }
                        }
                    }
                }
            }
        }, content = { innerpadding ->

            Column(modifier = Modifier.padding(innerpadding)) {
                Divider(color = ColorDorado, thickness = 1.dp)

                Box(modifier = Modifier.fillMaxSize()) {
                    // Fondo de pantalla
                    Image(
                        painter = painterResource(id = R.drawable.fondo),
                        contentDescription = "Fondo de pantalla", contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Lista de audios
                    ListaAudios(
                        audioList = audios,
                        setSelectedAudio = { selectedAudio = it },
                        scope = scope,
                        viewModel = viewModel
                    )
                }
            }
        }
    )
}
