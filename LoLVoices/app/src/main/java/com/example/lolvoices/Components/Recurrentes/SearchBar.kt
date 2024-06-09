package com.example.lolvoices.Components.Recurrentes

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

// Barra de busqueda
// Se compone de un TextField que se muestra cuando se activa la busqueda
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearchVisibilityChange: (Boolean) -> Unit
) {
        TextField(
            value = searchText,
            onValueChange = { onSearchTextChange(it) },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color.White),
            placeholder = { Text("Buscar...", color = Color.White) },
            singleLine = true,
            shape = MaterialTheme.shapes.small,
            leadingIcon = {
                IconButton(onClick = {
                    onSearchVisibilityChange(false)
                    onSearchTextChange("")
                }) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "Limpiar",
                        tint = Color.White
                    )
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
