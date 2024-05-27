package com.example.lolvoices.room.Entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Campeon(
    @PrimaryKey(autoGenerate = true) var id:Long = 0,
    @ColumnInfo(name = "nombre") var nombre:String?,
    @ColumnInfo(name = "imagen") var imagen:String?
)
