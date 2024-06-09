package com.example.lolvoices.Vistas

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.lolvoices.Components.AudioPlayerViewModel
import com.example.lolvoices.Components.GifImage
import com.example.lolvoices.dataClasses.ChampionData
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(navController: NavHostController, championData: List<ChampionData>, viewModel: AudioPlayerViewModel) {

    var showSplashScreen by remember { mutableStateOf(true) }
    val alreadyLoaded by viewModel.splashScreenShown.observeAsState(false)

    // Muestra la pantalla de carga durante 1.5 segundos
    LaunchedEffect(Unit) {
        delay(1500)
        showSplashScreen = false
        viewModel.setSplashScreenShown(true)
    }

    // Muestra la pantalla de carga si no se ha cargado ya
    if (showSplashScreen && !alreadyLoaded) {
        GifImage()
    } else {
        // Muestra la pantalla de campeones
        CampeonesScreen(navController = navController, CampeonesInfo = championData, viewModel = viewModel)
    }
}
