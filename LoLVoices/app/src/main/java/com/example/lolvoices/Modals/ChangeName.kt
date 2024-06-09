package com.example.lolvoices.Modals

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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.lolvoices.Components.Recurrentes.BordesDoraditos
import com.example.lolvoices.FireBase.FireStoreBBDD
import com.example.lolvoices.ui.theme.ColorDorado

// Modal para guardar la puntuacion
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeName(
    onDismiss: () -> Unit,
    onAccept: () -> Unit,
    puntuacion: Int
) {
    var usuario by remember {
        mutableStateOf("Guess")
    }

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        BordesDoraditos(ancho = 310, alto = 150) {
            Box(
                modifier = Modifier
                    .background(Color(0xFF021119))
                    .width(300.dp)
                    .height(140.dp)
                    .border(1.dp, ColorDorado)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(3f)) {
                            TextField(
                                value = usuario,
                                onValueChange = { usuario = it },
                                textStyle = TextStyle(color = ColorDorado),
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth(),
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = Color.Transparent,
                                    cursorColor = ColorDorado,
                                    focusedIndicatorColor = ColorDorado,
                                    unfocusedIndicatorColor = ColorDorado
                                )
                            )
                        }
                        Column {
                            IconButton(
                                // Guardar la puntuacion en firebase
                                onClick = {
                                    onAccept()
                                    onDismiss()
                                    FireStoreBBDD().addPuntuacion(puntuacion, usuario)
                                }) {
                                Icon(Icons.Default.Done, contentDescription = "Done", tint = ColorDorado)
                            }
                        }

                    }
                }
            }
        }
    }
}