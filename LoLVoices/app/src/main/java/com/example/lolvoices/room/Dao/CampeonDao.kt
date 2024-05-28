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

    //READ
    @Query("SELECT * FROM audio WHERE idCampeon like :idElegido")
    suspend fun getAudioByChampion(idElegido: Long): MutableList<Audio>
    @Query("SELECT * FROM campeon")
    suspend fun getChampions(): List<Campeon>

    @Query("SELECT * FROM campeon WHERE nombre like :nombre")
    suspend fun getChampionByName(nombre: String): Campeon

    @Query("SELECT * FROM audio WHERE nombre like :nombre")
    suspend fun getAudioByName(nombre: String): Audio

    //CREATE
    @Insert
    suspend fun addChampion(champion : Campeon):Long
    @Insert
    suspend fun addAudio(audio : Audio): Long

    //DELETE
    @Delete
    suspend fun deleteAudio(audio: Audio):Int
    @Delete
    suspend fun deleteChampion(champion: Campeon):Int

    //UPDATE
    @Update
    suspend fun updateAudio(audio: Audio):Int
    @Update
    suspend fun updateChampion(champion: Campeon):Int

}