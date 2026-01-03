package com.foss.aihub.ui.webview

import android.util.Log
import androidx.core.net.toUri
import com.foss.aihub.utils.alwaysBlockedDomains
import com.foss.aihub.utils.commonAuthDomains
import com.foss.aihub.utils.serviceDomains

object WebViewSecurity {
    private var _isBlockingEnabled = true

    var isBlockingEnabled: Boolean
        get() = _isBlockingEnabled
        set(value) {
            _isBlockingEnabled = value
        }

    fun allowConnectivityForService(serviceId: String, url: String): Boolean {
        Log.d("AI_HUB", "Checking URL for $serviceId: $url")


        if (url.isBlank()) {
            Log.d("AI_HUB", "❌ Blank URL")
            return false
        }

        if (url.startsWith("blob:")) {
            Log.d("AI_HUB", "✅ Allowed: Blob URL for download - $url")
            return true
        }

        val allowedInternalUrls = listOf(
            "about:blank", "data:text/html", "file://", "content://"
        )

        if (allowedInternalUrls.any { url.startsWith(it) }) {
            Log.d("AI_HUB", "✅ Allowed internal URL: $url")
            return true
        }

        val uri = url.toUri()
        val scheme = uri.scheme ?: ""
        val host = uri.host ?: ""

        Log.d("AI_HUB", "Scheme: $scheme, Host: $host")

        if (scheme != "https" && host.isNotEmpty()) {
            Log.d("AI_HUB", "❌ Non-HTTPS external URL: $url")
            return false
        }


        if (host.isEmpty()) {
            Log.d("AI_HUB", "✅ No host (data URI)")
            return true
        }

        for (blockedDomain in alwaysBlockedDomains.getOrDefault(serviceId, emptyList())) {
            if (host == blockedDomain || host.endsWith(".$blockedDomain")) {
                Log.d("AI_HUB", "❌ Always blocked: $host")
                return false
            }
        }

        val isAuthDomain = commonAuthDomains.any { authDomain ->
            host.contains(authDomain)
        }

        if (isAuthDomain) {
            Log.d("AI_HUB", "✅ Allowed: Common auth domain - $host")
            return true
        }

        val allowedDomains = serviceDomains[serviceId]
        if (allowedDomains == null) {
            Log.d("AI_HUB", "❌ No domain rules for service: $serviceId")
            return false
        }

        val isAllowed = allowedDomains.any { domain ->
            host == domain || host.endsWith(".$domain")
        }
        if (isAllowed) {
            Log.d("AI_HUB", "✅ Allowed by $serviceId domains: $host")
            return true
        }

        Log.d("AI_HUB", "❌ Domain not allowed for $serviceId: $host")
        return false
    }
}