package com.foss.aihub.utils

import android.content.Context
import android.util.Log
import androidx.compose.ui.graphics.Color
import com.foss.aihub.models.AiService

const val USER_AGENT =
    "Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Mobile Safari/537.36"

var aiServices: List<AiService> = emptyList()
    private set

private fun generateId(name: String): String {
    return name.lowercase().replace("\\s+".toRegex(), "")
}

fun loadAiServices(rawList: List<List<String>>) {
    Log.d("AI_HUB", "Loading AI services... $rawList")
    aiServices = rawList.mapNotNull { row ->
        if (row.size < 5) return@mapNotNull null

        val name = row[0].trim()
        if (name.isBlank()) return@mapNotNull null

        val id = generateId(name)
        val url = row[1].trim()
        val category = row.getOrNull(2)?.trim() ?: "General"
        val description = row.getOrNull(3)?.trim() ?: ""
        val hex = row[4].trim().removePrefix("#").uppercase()

        val accentColor = try {
            val colorHex = when (hex.length) {
                6 -> "FF$hex"      // add full opacity
                8 -> hex
                else -> "FF6366F1" // fallback indigo
            }
            Color(colorHex.toLong(16))
        } catch (e: Exception) {
            Color(0xFF6366F1)
        }

        AiService(
            id = id,
            name = name,
            url = url,
            category = category,
            description = description,
            accentColor = accentColor
        )
    }
}

fun refreshAiServicesFromSettings(context: Context) {
    val settingsManager = SettingsManager(context)
    val raw = settingsManager.getAiServices()

        loadAiServices(raw)

}