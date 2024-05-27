package com.example.lolvoices.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.lolvoices.room.Dao.CampeonDao
import com.example.lolvoices.room.Entidades.Audio
import com.example.lolvoices.room.Entidades.Campeon

@Database(entities = [Campeon::class, Audio::class], version = 1)
abstract class VoicesDDBB : RoomDatabase(){
    abstract fun campeonDao(): CampeonDao
}