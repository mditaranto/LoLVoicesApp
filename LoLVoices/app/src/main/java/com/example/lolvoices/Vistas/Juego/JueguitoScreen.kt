package com.example.lolvoices.Vistas.Juego

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.lolvoices.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.example.lolvoices.Components.Buttoms.JugarButtom
import com.example.lolvoices.Components.Buttoms.StatsButtom
import com.example.lolvoices.Modals.SettingsDialog

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JueguitoScreen(navController: NavHostController) {

    Scaffold(
        topBar = {
            // Toolbar con 2 botones
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF021119),
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                modifier = Modifier.background(Color(0xFF021119)),
                title = {
                    Text(
                        "Guess The Voice",
                        color = Color.White
                    )  // Cambia esto al título que desees mostrar
                },
                actions = {
                    IconButton(onClick = { navController.navigate("CampeonesScreen") }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Agregar",
                            tint = Color.White
                        )
                    }
                },
            )
        },
        content = { innerpadding ->
            var showDialog by remember { mutableStateOf(false) }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerpadding)
            ) {
                Divider(color = Color(0xFFC0A17B), thickness = 1.dp)

                Box(Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.fondo),
                        contentDescription = "Fondo de pantalla",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(30.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Escucha la voz y adivina a qué campeón pertenece",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            ),
                        )
                        if (showDialog) {
                            SettingsDialog(
                                onDismiss = { showDialog = false },
                                navController = navController
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Column {
                            JugarButtom(setShowDialog = { showDialog = it })
                            StatsButtom(navController = navController)
                        }
                    }
                }
            }
        }
    )
}
