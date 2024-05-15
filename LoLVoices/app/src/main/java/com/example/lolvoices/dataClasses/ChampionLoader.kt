package com.example.lolvoices.dataClasses

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.URL

object ChampionLoader {
    private const val JSON_URL = "https://mditaranto.github.io/LoLVoices/campeones_audios.json"

    fun loadChampions(): List<ChampionData> {
        val jsonString = URL(JSON_URL).readText()
        return Json.decodeFromString(jsonString)
    }
}