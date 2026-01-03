package com.foss.aihub.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.foss.aihub.models.AppSettings
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsManager(context: Context) {
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences("aihub_settings", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _settingsFlow = MutableStateFlow(loadSettings())
    val settingsFlow: StateFlow<AppSettings> = _settingsFlow

    fun updateSettings(update: (AppSettings) -> Unit) {
        val current = loadSettings()
        update(current)
        saveSettings(current)
        _settingsFlow.value = current
    }

    private fun loadSettings(): AppSettings {
        return AppSettings(
            enableZoom = sharedPref.getBoolean("enableZoom", true),
            loadLastOpenedAI = sharedPref.getBoolean("loadLastOpenedAI", true),
            fontSize = sharedPref.getString("fontSize", "medium") ?: "medium",
            defaultServiceId = sharedPref.getString("defaultServiceId", "chatgpt") ?: "chatgpt",
            enabledServices = loadEnabledServices(),
            serviceOrder = loadServiceOrder()
        )
    }

    private fun saveSettings(settings: AppSettings) {
        sharedPref.edit {
            putBoolean("enableZoom", settings.enableZoom)
            putBoolean("loadLastOpenedAI", settings.loadLastOpenedAI)
            putString("fontSize", settings.fontSize)
            putString("defaultServiceId", settings.defaultServiceId)
            saveEnabledServices(settings.enabledServices)
            saveServiceOrder(settings.serviceOrder)
        }
    }

    private fun loadEnabledServices(): Set<String> {
        val json = sharedPref.getString("enabledServices", null)
        return if (json.isNullOrEmpty()) {
            aiServices.map { it.id }.toSet()
        } else {
            val type = object : TypeToken<Set<String>>() {}.type
            gson.fromJson(json, type)
        }
    }

    private fun saveEnabledServices(services: Set<String>) {
        val json = gson.toJson(services)
        sharedPref.edit { putString("enabledServices", json) }
    }

    private fun loadServiceOrder(): List<String> {
        val json = sharedPref.getString("serviceOrder", null)
        return if (json.isNullOrEmpty()) {
            aiServices.map { it.id }
        } else {
            val type = object : TypeToken<List<String>>() {}.type
            gson.fromJson(json, type)
        }
    }

    private fun saveServiceOrder(order: List<String>) {
        val json = gson.toJson(order)
        sharedPref.edit { putString("serviceOrder", json) }
    }

    fun saveLastOpenedService(serviceId: String) {
        sharedPref.edit { putString("lastOpenedService", serviceId) }
    }

    fun getLastOpenedService(): String? {
        return sharedPref.getString("lastOpenedService", null)
    }
}