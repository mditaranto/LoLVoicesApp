package com.example.lolvoices.room.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.lolvoices.room.Entidades.Audio
import com.example.lolvoices.room.Entidades.Campeon

@Dao
interface CampeonDao {

    @Query("SELECT * FROM audio WHERE idCampeon like :idElegido")
    suspend fun getAudioByChampion(idElegido: Long): MutableList<Audio>

    @Query("SELECT * FROM campeon")
    suspend fun getChampion(): MutableList<Campeon>

    @Insert
    suspend fun addChampion(champion : Campeon):Long

    @Insert
    suspend fun addAudio(audio : Audio): Long

    @Delete
    suspend fun deleteAudio(audio: Audio):Int

    @Delete
    suspend fun deleteChampion(champion: Campeon):Int

}