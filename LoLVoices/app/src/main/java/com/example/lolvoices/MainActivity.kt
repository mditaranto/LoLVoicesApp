package com.example.lolvoices

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.example.lolvoices.ViewModels.AudioPlayerViewModel
import com.example.lolvoices.Vistas.CampeonScreen
import com.example.lolvoices.Vistas.CampeonesScreen
import com.example.lolvoices.Vistas.FavoritosScreen
import com.example.lolvoices.Vistas.Juego.JuegoScreen
import com.example.lolvoices.Vistas.Juego.JueguitoScreen
import com.example.lolvoices.Vistas.LoadingScreen
import com.example.lolvoices.Vistas.PuntuacionesScreen
import com.example.lolvoices.dataClasses.ChampionData
import com.example.lolvoices.dataClasses.ChampionLoader
import com.example.lolvoices.room.VoicesDDBB
import com.example.lolvoices.ui.theme.LoLVoicesTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {

    companion object {
        lateinit var database: VoicesDDBB
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = Room.databaseBuilder(this, VoicesDDBB::class.java, "VoicesDDBB").build()

        val championData = runBlocking(Dispatchers.IO) {
            try {
                ChampionLoader.loadChampions()
            } catch (e: Exception) {
                emptyList<ChampionData>() // Devuelve una lista vacía en caso de error
            }
        }

        enableEdgeToEdge()
        setContent {
            val viewModel : AudioPlayerViewModel = viewModel()
            val navController = rememberNavController()
            LoLVoicesTheme {
                // A surface container using the 'background' color from the theme
                NavHost (navController = navController, startDestination = "LoadingScreen") {
                    composable("LoadingScreen") {
                        LoadingScreen(navController, championData, viewModel)
                    }
                    composable("CampeonesScreen") {
                        CampeonesScreen(navController, championData, viewModel)
                    }
                    composable( "JueguitoScreen") {
                        JueguitoScreen(navController)
                    }
                    composable( "FavoritosScreen") {
                        FavoritosScreen(navController)
                    }
                    composable( "CampeonScreen/{campeon}", arguments = listOf(navArgument("campeon") {
                        type = NavType.StringType}) ) {
                        CampeonScreen(navController, it.arguments?.getString("campeon").toString(), viewModel)
                    }
                    composable( "JuegoScreen/{numJugadores}", arguments = listOf(navArgument("numJugadores") {
                        type = NavType.IntType}) ) {
                        JuegoScreen(navController, championData, it.arguments?.getInt("numJugadores") ?: 1, viewModel)
                    }
                    composable("PuntuacionesScreen") {
                    PuntuacionesScreen(navController)
                    }
                }
            }
        }
    }
}