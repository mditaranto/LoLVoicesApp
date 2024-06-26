package com.example.lolvoices.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lolvoices.MainActivity
import com.example.lolvoices.dataClasses.ChampionAudio
import com.example.lolvoices.dataClasses.ChampionData
import com.example.lolvoices.room.Entidades.Audio
import com.example.lolvoices.room.Entidades.Campeon
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class RoomGestor(application: Application) : AndroidViewModel(application) {
    private val campeonDao = MainActivity.database.campeonDao()

    fun agregarFavorito(campeon: ChampionData, audio: ChampionAudio) {
        viewModelScope.launch {
            val nuevo = campeonDao.getChampionByName(campeon.nombre)

            if (nuevo == null) {
                val idCampeon = campeonDao.addChampion(Campeon(nombre = campeon.nombre, imagen = campeon.imagen))
                campeonDao.addAudio(Audio(idCampeon = idCampeon, nombre = audio.nombre, url = audio.url))
            } else {
                campeonDao.addAudio(Audio(idCampeon = nuevo.id, nombre = audio.nombre, url = audio.url))
            }
        }
    }

    fun comprobarFavorito(audio: ChampionAudio): Boolean {

        var favorito = false
        runBlocking {
            val nuevo = campeonDao.getAudioByUrl(audio.url)
            if (nuevo != null) {
                favorito = true
            }
        }
        return favorito
    }

    fun eliminarFavorito(audio: ChampionAudio) {
        viewModelScope.launch {
            val nuevo = campeonDao.getAudioByUrl(audio.url)
            if (nuevo != null) {
                campeonDao.deleteAudio(nuevo)
            }
        }
    }

    fun getChampion(): List<Campeon> {
        return runBlocking {
            campeonDao.getChampions()
        }
    }

    fun getAudioByChampion(idElegido: Long): MutableList<Audio> {
        return runBlocking {
            campeonDao.getAudioByChampion(idElegido)
        }
    }

    fun eliminarCampeon(campeon: Campeon) {
        viewModelScope.launch {
            val nuevo = campeon.nombre?.let { campeonDao.getChampionByName(it) }
            if (nuevo != null) {
                campeonDao.deleteChampion(nuevo)
            }
        }
    }

    fun getCheapionByName(nombre: String): Campeon {
        return runBlocking {
            campeonDao.getChampionByName(nombre)
        }
    }

    fun updateAudio(url: String, newName: String) {
        viewModelScope.launch {
            val nuevo = campeonDao.getAudioByUrl(url)
            nuevo.nombre = newName
            campeonDao.updateAudio(nuevo)
        }
    }

    fun getAudioByUrl(url: String): Audio {
        return runBlocking {
            campeonDao.getAudioByUrl(url)
        }
    }

}