package com.example.lolvoices.Components

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun AudioPlayer(
    url: String,
    onLoading: (Boolean) -> Unit,
    onProgress: (Float) -> Unit,
    onDurationRetrieved: (Int) -> Unit
) {
    val mediaPlayer = MediaPlayer()

    mediaPlayer.setAudioAttributes(
        AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build()
    )

    try {
        onLoading(true)
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            onLoading(false)
            onDurationRetrieved(it.duration)
            it.start()
            mediaPlayer.setOnCompletionListener {
                it.release()
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        onLoading(false)
        mediaPlayer.release()
    }
}