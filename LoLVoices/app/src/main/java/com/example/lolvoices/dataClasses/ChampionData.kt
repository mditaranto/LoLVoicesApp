package com.example.lolvoices.dataClasses

import kotlinx.serialization.Serializable

@Serializable
data class ChampionData(
    val nombre: String,
    val audios: List<ChampionAudio> = emptyList(),
    val imagen: String
)
