package com.foss.aihub.utils

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.net.toUri

fun extractLinkTitle(url: String): String {
    if (url.isEmpty()) return "Link"
    return try {
        url.substringAfterLast("/").substringBefore("?").substringBefore("#")
            .replace(Regex("[_-]"), " ").replace(Regex("\\.[a-zA-Z]{2,4}$"), "").trim()
            .takeIf { it.isNotEmpty() && it != "null" }?.split(" ")?.joinToString(" ") { word ->
                word.replaceFirstChar { it.uppercase() }
            } ?: "Link"
    } catch (_: Exception) {
        "Link"
    }
}

fun copyLinkToClipboard(context: Context, url: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("URL", url)
    clipboard.setPrimaryClip(clip)
}

fun shareLink(context: Context, url: String, title: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, url)
        putExtra(Intent.EXTRA_SUBJECT, title)
    }
    context.startActivity(
        Intent.createChooser(shareIntent, "Share link").apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })
}

fun cleanTrackingParams(context: Context, url: String): String {
    return try {
        val uri = url.toUri()
        val builder = uri.buildUpon()
        builder.clearQuery()
        val settings = SettingsManager(context)

        uri.queryParameterNames.forEach { param ->
            if (!settings.getTrackingParams().contains(param.lowercase())) {
                val values = uri.getQueryParameters(param)
                values.forEach { value ->
                    builder.appendQueryParameter(param, value)
                }
            }
        }
        builder.build().toString()
    } catch (_: Exception) {
        url
    }
}

fun openInExternalBrowser(
    context: Context, url: String
) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
        Log.d("AI_HUB", "Opening in external browser: $url")
    } catch (e: ActivityNotFoundException) {
        Log.e("AI_HUB", "No browser app found to handle URL: $url", e)
    } catch (e: Exception) {
        Log.e("AI_HUB", "Error opening URL in browser: $url", e)
    }
}