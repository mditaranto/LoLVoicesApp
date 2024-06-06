package com.example.lolvoices.Modals

import CustomButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.lolvoices.Components.BordesDoraditos
import com.example.lolvoices.InfiniteCircularList
import com.example.lolvoices.R
import com.example.lolvoices.ui.theme.ColorDorado


@Composable
fun SettingsDialog(onDismiss: () -> Unit, navController: NavHostController) {
    var selectedPlayers by remember { mutableIntStateOf(1) }
    val playerRange = (1..10).toList()
    val circularPlayerRange = List(1000) { playerRange[it % playerRange.size] } // Circular list
    val lazyListState = rememberLazyListState(initialFirstVisibleItemIndex = circularPlayerRange.size / 2)

// Update the selectedPlayers based on the scroll position
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemIndex }
            .collect { firstVisibleIndex ->
                val centerIndex = lazyListState.layoutInfo.visibleItemsInfo.size / 2
                selectedPlayers = circularPlayerRange[firstVisibleIndex + centerIndex]
            }
    }

    Dialog(
        onDismissRequest = { onDismiss },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        BordesDoraditos(ancho = 310, alto = 410) {
            Box(
                modifier = Modifier
                    .background(Color(0xFF021119))
                    .width(300.dp)
                    .height(400.dp)
                    .border(1.dp, ColorDorado)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.settingwall),
                    contentDescription = "Fondo de pantalla",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.3f)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "AJUSTES",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp, top = 16.dp)
                    )

                    Text(
                        text = "Número de Jugadores",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Box(
                        modifier = Modifier
                            .height(200.dp)
                            .padding(horizontal = 36.dp)
                            .fillMaxWidth()
                    ) {
                        InfiniteCircularList(
                            width = 300.dp,
                            itemHeight = 55.dp,
                            items = playerRange,
                            initialItem = 1,
                            textStyle = TextStyle(fontSize = 15.sp),
                            textColor = Color.White,
                            selectedTextColor = Color(0xFF0EAAD2),
                            onItemSelected = { index, item ->
                                selectedPlayers = item
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom
                    ) {

                        CustomButton(
                            text = "Cancelar",
                            onClick = { onDismiss() },
                            ancho = 120,
                            alto = 55
                        )

                        CustomButton(
                            text = "Jugar",
                            onClick = { onDismiss(); navController.navigate("JuegoScreen/${selectedPlayers}") },
                            ancho = 120,
                            alto = 55
                        )
                    }
                }
            }
        }
    }
}