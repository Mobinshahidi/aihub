package com.foss.aihub.utils

import android.content.Context
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

object DomainConfigUpdater {
    private const val CONFIG_URL = "https://silentcoderhere.github.io/DomainDataStore/"
    private const val FILE_NAME = "domain_config.json"
    private val client = HttpClient(CIO)

    suspend fun updateIfNeeded(context: Context): String = withContext(Dispatchers.IO) {
        try {
            val response = client.get(CONFIG_URL + FILE_NAME)
            if (!response.status.isSuccess()) {
                return@withContext "Failed: HTTP ${response.status.value}"
            }

            val json = response.bodyAsText()
            val remote = Json.decodeFromString<RemoteDomainConfig>(json)

            val manager = SettingsManager(context)
            val currentVersion = manager.getDomainConfigVersion() ?: "0.0.0"

            if (remote.version == currentVersion) {
                return@withContext "Up to date (v$currentVersion)"
            }

            manager.saveDomainConfig(
                version = remote.version,
                serviceDomains = remote.service_domains,
                alwaysBlockedDomains = remote.always_blocked_domains,
                commonAuthDomains = remote.common_auth_domains,
                trackingParams = remote.tracking_params
            )

            "Updated to v${remote.version}"
        } catch (e: Exception) {
            "Update failed: ${e.message?.take(60) ?: "unknown error"}"
        }
    }
}