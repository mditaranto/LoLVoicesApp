package com.example.lolvoices.Vistas

import androidx.compose.foundation.Image
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter

@Composable
fun FavoritosScreen(navController: NavHostController) {
    Text(text = "Favoritos")
    
    Image(painter = rememberAsyncImagePainter("https://github.com/mditaranto/LoLVoices/raw/main/Voces/Aatrox/Aatrox.jpg"),
        contentDescription = null)

}