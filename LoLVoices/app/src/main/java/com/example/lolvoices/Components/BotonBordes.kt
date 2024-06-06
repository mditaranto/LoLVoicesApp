import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lolvoices.ui.theme.ColorDorado

@Composable
fun CustomButton(text: String, onClick: () -> Unit, ancho : Int, alto : Int, enabled : Boolean = true) {
    val anchoC = ancho - 10
    val altoC = alto - 10
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clickable(onClick = (if (enabled) onClick else ({})))
            .size(width = ancho.dp, height = alto.dp),
    ) {
        // Box que sobresale por los lados
        Box(
            modifier = Modifier
                .size(width = (anchoC + 10).dp, height = (altoC - 20).dp)
                .align(Alignment.Center)
                .border(1.dp, ColorDorado)
        )
        // Box que sobresale por arriba y abajo
        Box(
            modifier = Modifier
                .size(width = (anchoC - 20).dp, height = (altoC + 10).dp)
                .align(Alignment.Center)
                .border(1.dp, ColorDorado)
        )
        // Botón principal
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(width = anchoC.dp, height = altoC.dp)
                .background(if (enabled) Color(0xFF021119) else Color(0xFF3c4650))
                .align(Alignment.Center)
                .border(1.dp, ColorDorado)
        ) {
            Text(
                text = text,
                fontSize = 15.sp,
                color = Color.White
            )
        }
    }
}
