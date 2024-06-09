package com.example.lolvoices.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.lolvoices.R

// Gif de carga
@Composable
fun GifImage(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(ImageDecoderDecoder.Factory())
        }
        .build()
    Column (modifier = Modifier.fillMaxSize()
        .background(color = Color(0xFF020911)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        // Loading gif
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(context).data(data = R.drawable.loadinggif).apply(block = {
                    size(Size(10, 10))
                }).build(), imageLoader = imageLoader
            ),
            contentDescription = null,
            modifier = modifier.fillMaxSize(),
        )
    }
}