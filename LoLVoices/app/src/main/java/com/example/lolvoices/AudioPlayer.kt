package com.example.lolvoices

import android.media.AudioManager
import android.media.MediaPlayer


fun AudioPlayer(url: String, onDurationRetrieved: (Int) -> Unit) {
    val mediaPlayer = MediaPlayer()
    val audioUrl = url

    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)

    try {
        mediaPlayer.setDataSource(audioUrl)
        mediaPlayer.prepare()
        onDurationRetrieved(mediaPlayer.duration)

        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener {
            // Lógica cuando el audio termina
            mediaPlayer.release()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
