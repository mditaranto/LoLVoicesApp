package com.example.lolvoices.dataClasses

import kotlinx.serialization.Serializable

@Serializable
data class ChampionAudio(
    val nombre: String,
    val url: String
)
