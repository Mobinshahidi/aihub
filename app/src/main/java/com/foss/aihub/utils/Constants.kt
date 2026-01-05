package com.foss.aihub.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Air
import androidx.compose.material.icons.rounded.AutoAwesomeMosaic
import androidx.compose.material.icons.rounded.Brush
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.DeveloperMode
import androidx.compose.material.icons.rounded.FlashOn
import androidx.compose.material.icons.rounded.IntegrationInstructions
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.PrivacyTip
import androidx.compose.material.icons.rounded.RocketLaunch
import androidx.compose.material.icons.rounded.SentimentVerySatisfied
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material.icons.rounded.TravelExplore
import androidx.compose.ui.graphics.Color
import com.foss.aihub.models.AiService

const val USER_AGENT =
    "Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/142.0.0.0 Mobile Safari/537.36"

val aiServices = listOf(
    AiService(
        "chatgpt",
        "ChatGPT",
        "https://chatgpt.com/",
        "General",
        "OpenAI's conversational AI",
        Color(0xFF10A37F)
    ),
    AiService(
        "duckai", "Duck AI", "https://duck.ai/", "Privacy", "Privacy-focused AI", Color(0xFF8B4513)
    ),
    AiService(
        "venice",
        "Venice",
        "https://venice.ai/chat",
        "Multimodal",
        "Creative AI assistant",
        Color(0xFF9C27B0)
    ),
    AiService(
        "grok", "Grok", "https://grok.com/", "Fun", "With a sense of humor", Color(0xFFFF4081)
    ),
    AiService(
        "lumo",
        "Lumo",
        "https://lumo.proton.me/",
        "Privacy",
        "Proton's secure AI",
        Color(0xFF2196F3)
    ),
    AiService(
        "deepseek",
        "Deepseek",
        "https://chat.deepseek.com/",
        "Coding",
        "AI for developers",
        Color(0xFF00ACC1)
    ),
    AiService(
        "gemini",
        "Gemini",
        "https://gemini.google.com/",
        "General",
        "Google's AI assistant",
        Color(0xFFEA4335)
    ),
    AiService(
        "claude",
        "Claude",
        "https://claude.ai/chat",
        "Writing",
        "Anthropic's assistant",
        Color(0xFF7B1FA2)
    ),
    AiService(
        "perplexity",
        "Perplexity",
        "https://www.perplexity.ai/",
        "Research",
        "AI search engine",
        Color(0xFF00AB97)
    ),
    AiService(
        "qwen", "Qwen", "https://chat.qwen.ai/", "Multilingual", "Alibaba's AI", Color(0xFFFF9800)
    ),
    AiService(
        "mistral",
        "Mistral",
        "https://chat.mistral.ai/",
        "Efficient",
        "French AI model",
        Color(0xFF3F51B5)
    ),
    AiService(
        "blackbox",
        "Blackbox",
        "https://app.blackbox.ai/",
        "Coding",
        "AI code assistant",
        Color(0xBFFF0000)
    ),
    AiService(
        "copilot",
        "Copilot",
        "https://copilot.microsoft.com/",
        "General",
        "Microsoft's AI assistant",
        Color(0xFF4293B3)
    ),
    AiService(
        "brave",
        "Brave",
        "https://search.brave.com/ask",
        "Search",
        "Brave's search engine",
        Color(0xFFB2CC25)
    ),
)

val serviceIcons = mapOf(
    "chatgpt" to Icons.Rounded.FlashOn,
    "duckai" to Icons.Rounded.PrivacyTip,
    "venice" to Icons.Rounded.Brush,
    "grok" to Icons.Rounded.SentimentVerySatisfied,
    "lumo" to Icons.Rounded.Shield,
    "deepseek" to Icons.Rounded.DeveloperMode,
    "gemini" to Icons.Rounded.AutoAwesomeMosaic,
    "claude" to Icons.Rounded.Description,
    "perplexity" to Icons.Rounded.TravelExplore,
    "qwen" to Icons.Rounded.Language,
    "mistral" to Icons.Rounded.Air,
    "blackbox" to Icons.Rounded.IntegrationInstructions,
    "copilot" to Icons.Rounded.RocketLaunch
)

val serviceDomains = mapOf(
    // ChatGPT
    "chatgpt" to listOf(
        "chatgpt.com",
        "openai.com",
        "fileserviceuploadsperm.blob.core.windows.net",
        "cdn.oaistatic.com",
        "oaiusercontent.com",

        // Auth
        "cdn.auth0.com",
        "auth.openai.com",
        "auth-cdn.oaistatic.com"
    ),

    // Duck AI
    "duckai" to listOf(
        "duck.ai", "duckduckgo.com"
    ),

    // Venice
    "venice" to listOf(
        "venice.ai"
    ),

    // Grok
    "grok" to listOf(
        "grok.com",

        // Auth
        "accounts.x.ai"
    ),

    // Lumo
    "lumo" to listOf(
        "lumo.proton.me",

        // Auth
        "account.proton.me"
    ),

    // Deepseek
    "deepseek" to listOf(
        "chat.deepseek.com", "cdn.deepseek.com", "static.deepseek.com"
    ),

    // Gemini
    "gemini" to listOf(
        "gemini.google.com",
        "fonts.gstatic.com",
        "www.gstatic.com",
    ),

    // Claude
    "claude" to listOf(
        "claude.ai"
    ),

    // Perplexity
    "perplexity" to listOf(
        "www.perplexity.ai", "pplx-next-static-public.perplexity.ai"
    ),

    // Qwen
    "qwen" to listOf(
        "chat.qwen.ai",
        "qwen.ai",
        "alicdn.com",
        "cdnjs.cloudflare.com",
        "assets.alicdn.com",
        "img.alicdn.com",
        "at.alicdn.com",
        "d.alicdn.com",
        "o.alicdn.com",
        "g.alicdn.com",
        "aplus.qwen.ai",
        "aliyuncs.com",
        "tdum.alibaba.com"
    ),

    // Mistral
    "mistral" to listOf(
        "chat.mistral.ai", "mistral.ai", "api.mistral.ai", "console.mistral.ai", "mistralcdn.net",

        // Auth
        "v2.auth.mistral.ai",
    ),

    // Blackbox
    "blackbox" to listOf(
        "app.blackbox.ai", "js.stripe.com", "m.stripe.network"
    ),

    // Copilot
    "copilot" to listOf(
        "copilot.microsoft.com"
    ),

    // Brave
    "brave" to listOf(
        "cdn.search.brave.com", "search.brave.com"
    )
)

val alwaysBlockedDomains = mapOf(
    "chatgpt" to listOf(
        "ab.chatgpt.com"
    ),
    "duckai" to listOf(
        "improving.duckduckgo.com"
    ),
    "grok" to listOf(
        "www.google-analytics.com",
    ),
    "gemini" to listOf(
        "www.google-analytics.com", "www.googletagmanager.com"
    ),
    "perplexity" to listOf(
        "suggest.perplexity.ai"
    ),
)

val commonAuthDomains = listOf(
    "accounts.google.com", "appleid.apple.com", "login.live.com", "login.microsoftonline.com"
)

val TRACKING_PARAMS = setOf(
    "utm_source",
    "utm_medium",
    "utm_campaign",
    "utm_term",
    "utm_content",
    "fbclid",
    "gclid",
    "msclkid",
    "dclid",
    "zanpid",
    "ref",
    "source",
    "campaign",
    "medium",
    "content"
)