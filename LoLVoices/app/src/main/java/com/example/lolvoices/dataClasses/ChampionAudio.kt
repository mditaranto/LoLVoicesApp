package com.example.lolvoices.dataClasses

import kotlinx.serialization.Serializable

@Serializable
data class ChampionAudio(
    var nombre: String,
    val url: String
)
