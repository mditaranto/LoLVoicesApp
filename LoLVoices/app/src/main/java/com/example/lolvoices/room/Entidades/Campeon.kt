package com.example.lolvoices.room.Entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Campeon(
    @PrimaryKey var id:Int,
    @ColumnInfo(name = "nombre") var nombre:String?,
    @ColumnInfo(name = "foto") var foto:String?
)
