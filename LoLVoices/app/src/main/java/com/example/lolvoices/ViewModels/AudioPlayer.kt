package com.example.lolvoices.ViewModels

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// ViewModel que se encarga de la lógica de reproducción de audios
// Se encarga de reproducir, pausar y detener audios
// También se encarga de actualizar el estado de isLoading, isPlaying, isPaused y el progreso del audio
class AudioPlayerViewModel : ViewModel() {
    private val mediaPlayers = mutableMapOf<String, MediaPlayer>()
    private val handlers = mutableMapOf<String, Handler>()
    private val progressRunnables = mutableMapOf<String, Runnable>()
    private val _isPlayingMap = mutableMapOf<String, MutableLiveData<Boolean>>()
    private val _isPausedMap = mutableMapOf<String, MutableLiveData<Boolean>>()
    private val _isLoadingMap = mutableMapOf<String, MutableLiveData<Boolean>>()
    private val _progressMap = mutableMapOf<String, MutableLiveData<Float>>()

    fun play(
        url: String,
        Played: (Boolean) -> Unit = {}
    ) {
        // Si no existe la clave en el mapa, se crea un nuevo LiveData
        if (!_isLoadingMap.containsKey(url)) {
            _isLoadingMap[url] = MutableLiveData(true)
        }
        // Si no existe la clave en el mapa, se crea un nuevo LiveData
        if (!mediaPlayers.containsKey(url)) {
            val mediaPlayer = MediaPlayer().apply {
                // Se configura el audio para que se reproduzca en el modo correcto
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                // Se configura el audio para que se reproduzca en el modo correcto
                setDataSource(url)
                prepareAsync()
                _isLoadingMap[url]?.postValue(true) // Set isLoading to true
            }

            // Se crea un nuevo Handler para actualizar el progreso del audio
            val handler = Handler(Looper.getMainLooper())
            handlers[url] = handler

            // Se crea un nuevo Runnable para actualizar el progreso del audio
            val progressRunnable = object : Runnable {
                override fun run() {
                    try {
                        if (mediaPlayer.isPlaying) {
                            // Se obtiene la posición actual y la duración del audio
                            val currentPosition = mediaPlayer.currentPosition
                            val duration = mediaPlayer.duration
                            if (duration > 0) {
                                // Se calcula el progreso del audio
                                val progress = currentPosition.toFloat() / duration
                                _progressMap[url]?.postValue(progress)
                            }
                            // Se actualiza el progreso del audio cada 50ms
                            handler.postDelayed(this, 50)
                        }
                    } catch (e: IllegalStateException) {
                        e.printStackTrace()
                    }
                }
            }
            // Se agrega el Runnable al mapa de Runnables
            progressRunnables[url] = progressRunnable

            // Se crea un Runnable para detener la reproducción del audio si no se ha reproducido
            val timeoutRunnable = Runnable {
                if (!mediaPlayer.isPlaying) {
                    // Se detiene la reproducción del audio
                    mediaPlayer.release()
                    _isLoadingMap[url]?.postValue(false) // Set isLoading to false
                    mediaPlayers.remove(url)
                    handlers.remove(url)
                    progressRunnables.remove(url)
                }
            }
            // Se ejecuta el Runnable después de 2 segundos
            handler.postDelayed(timeoutRunnable, 2000)

            // Se agrega un Listener para saber cuando el audio está listo para ser reproducido
            mediaPlayer.setOnPreparedListener {
                handler.removeCallbacks(timeoutRunnable)
                _isLoadingMap[url]?.postValue(false) // Set isLoading to false
                mediaPlayer.start()
                _isPlayingMap[url]?.postValue(true)
                handler.post(progressRunnable)
            }

            // Se agrega un Listener para saber cuando el audio ha terminado de reproducirse
            mediaPlayer.setOnCompletionListener {
                mediaPlayer.release()
                mediaPlayers.remove(url)
                handlers[url]?.removeCallbacks(progressRunnable)
                handlers.remove(url)
                progressRunnables.remove(url)
                _isPlayingMap[url]?.postValue(false)
                _progressMap[url]?.postValue(0f)
                Played(true)
            }
            // Se agrega el MediaPlayer al mapa de MediaPlayers
            mediaPlayers[url] = mediaPlayer
        } else {
            // Si ya esta cargado el audio, se reproduce
            mediaPlayers[url]?.start()
            _isPlayingMap[url]?.postValue(true)
            handlers[url]?.post(progressRunnables[url]!!)

        }
    }

    // Método para pausar la reproducción de un audio
    // Se actualiza el estado de isPlaying y isPaused
    fun pause(url: String) {
        mediaPlayers[url]?.let {
            if (it.isPlaying) {
                it.pause()
                handlers[url]?.removeCallbacks(progressRunnables[url]!!)
                _isPlayingMap[url]?.postValue(false)
                _isPausedMap[url]?.postValue(true)
            }
        }
    }

    // Método para reanudar la reproducción de un audio
    //  Se actualiza el estado de isPlaying y isPaused
    fun resume(url: String) {
        mediaPlayers[url]?.let {
            if (!it.isPlaying) {
                it.start()
                handlers[url]?.post(progressRunnables[url]!!)
                _isPlayingMap[url]?.postValue(true)
                _isPausedMap[url]?.postValue(false)
            }
        }
    }

    // Método para detener la reproducción de un audio
    // Se actualiza el estado de isPlaying y se libera el MediaPlayer
    fun stop(url: String) {
        mediaPlayers[url]?.let {
            it.stop()
            it.release()
            mediaPlayers.remove(url)
            handlers[url]?.removeCallbacks(progressRunnables[url]!!)
            handlers.remove(url)
            progressRunnables.remove(url)
            _isPlayingMap[url]?.postValue(false)
            _progressMap[url]?.postValue(0f)
            _isPausedMap[url]?.postValue(false)
        }
    }

    // Método para obtener el estado de isPlaying para un URL específico
    // Si no existe la clave en el mapa, se crea un nuevo LiveData
    fun isPlaying(url: String): LiveData<Boolean> {
        if (!_isPlayingMap.containsKey(url)) {
            _isPlayingMap[url] = MutableLiveData(false)
        }
        return _isPlayingMap[url]!!
    }

    // Método para obtener el estado de isPaused para un URL específico
    // Si no existe la clave en el mapa, se crea un nuevo LiveData
    fun isPaused(url: String): LiveData<Boolean> {
        if (!_isPausedMap.containsKey(url)) {
            _isPausedMap[url] = MutableLiveData(false)
        }
        return _isPausedMap[url]!!
    }

    // Método para obtener el estado de isLoading para un URL específico
    fun isLoading(url: String): LiveData<Boolean> {
        if (!_isLoadingMap.containsKey(url)) {
            _isLoadingMap[url] = MutableLiveData(false)
        }
        return _isLoadingMap[url]!!
    }

    // Método para obtener el progreso de la reproducción de un audio
    // Si no existe la clave en el mapa, se crea un nuevo LiveData
    fun progress(url: String): LiveData<Float> {
        if (!_progressMap.containsKey(url)) {
            _progressMap[url] = MutableLiveData(0f)
        }
        return _progressMap[url]!!
    }

    // Método para detener la reproducción de todos los audios
    fun stopAll() {
        mediaPlayers.values.forEach { it.release() }
        mediaPlayers.clear()
        handlers.values.forEach { it.removeCallbacksAndMessages(null) }
        handlers.clear()
        progressRunnables.clear()
        _isPlayingMap.clear()
        _isPausedMap.clear()
        _isLoadingMap.clear()
        _progressMap.clear()
    }

    override fun onCleared() {
        super.onCleared()
        stopAll()
    }

    // LiveData que se encarga de mostrar el SplashScreen
    private val _splashScreenShown = MutableLiveData<Boolean>()
    val splashScreenShown: LiveData<Boolean> = _splashScreenShown

    // Método para actualizar el estado del SplashScreen
    fun setSplashScreenShown(shown: Boolean) {
        _splashScreenShown.value = shown
    }
}