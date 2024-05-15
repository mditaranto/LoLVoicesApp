package com.example.lolvoices.room.Entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Audio(
    @PrimaryKey var id:Int,
    @ColumnInfo (name = "link") var link:String?,
    @ColumnInfo (name = "idCampeon") var idCampeon: Int
)
