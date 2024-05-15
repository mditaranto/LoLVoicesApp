package com.example.lolvoices

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lolvoices.Vistas.CampeonesScreen
import com.example.lolvoices.Vistas.FavoritosScreen
import com.example.lolvoices.Vistas.JueguitoScreen
import com.example.lolvoices.dataClasses.ChampionLoader
import com.example.lolvoices.ui.theme.LoLVoicesTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val championData = runBlocking(Dispatchers.IO) {
            ChampionLoader.loadChampions()
        }
        setContent {
            val navController = rememberNavController()
            LoLVoicesTheme {
                // A surface container using the 'background' color from the theme
                NavHost (navController = navController, startDestination = "CampeonesScreen") {
                    composable("CampeonesScreen") {
                        CampeonesScreen(navController, championData)
                    }
                    composable( "JueguitoScreen") {
                        JueguitoScreen(navController)
                    }
                    composable( "FavoritosScreen") {
                        FavoritosScreen(navController)
                    }

                }
            }
        }
    }
}