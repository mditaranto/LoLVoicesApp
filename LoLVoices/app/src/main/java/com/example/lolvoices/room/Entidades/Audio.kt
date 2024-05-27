package com.example.lolvoices.room.Entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Audio(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo (name = "url") var url:String?,
    @ColumnInfo (name = "idCampeon") var idCampeon: Long,
    @ColumnInfo (name = "nombre") var nombre:String?
)
