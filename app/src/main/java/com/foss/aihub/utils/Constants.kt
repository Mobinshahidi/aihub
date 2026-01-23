package com.foss.aihub.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Chat
import androidx.compose.material.icons.rounded.Air
import androidx.compose.material.icons.rounded.AutoAwesomeMosaic
import androidx.compose.material.icons.rounded.Brush
import androidx.compose.material.icons.rounded.Cloud
import androidx.compose.material.icons.rounded.DataObject
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.DeveloperMode
import androidx.compose.material.icons.rounded.FlashOn
import androidx.compose.material.icons.rounded.Forum
import androidx.compose.material.icons.rounded.IntegrationInstructions
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.People
import androidx.compose.material.icons.rounded.PrivacyTip
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material.icons.rounded.Public
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
        "General Purpose",
        "Versatile conversation assistant",
        Color(0xFF10A37F) // Green
    ),
    AiService(
        "duck",
        "Duck AI",
        "https://duck.ai/",
        "Privacy First",
        "Anonymous AI conversations",
        Color(0xFF8B7355) // Brown
    ),
    AiService(
        "venice",
        "Venice",
        "https://venice.ai/chat",
        "Creative Arts",
        "Multimedia creative assistant",
        Color(0xFF9C27B0) // Purple
    ),
    AiService(
        "grok",
        "Grok",
        "https://grok.com/",
        "Entertainment",
        "Witty humorous companion",
        Color(0xFFE91E63) // Pink
    ),
    AiService(
        "lumo",
        "Lumo",
        "https://lumo.proton.me/",
        "Secure AI",
        "Encrypted privacy assistant",
        Color(0xFF2196F3) // Blue
    ),
    AiService(
        "deepseek",
        "Deepseek",
        "https://chat.deepseek.com/",
        "Development",
        "Code generation specialist",
        Color(0xFF00ACC1) // Cyan
    ),
    AiService(
        "gemini",
        "Gemini",
        "https://gemini.google.com/",
        "Multimodal",
        "Google ecosystem integration",
        Color(0xFF4285F4) // Blue
    ),
    AiService(
        "claude",
        "Claude",
        "https://claude.ai/chat",
        "Professional Writing",
        "Long-form content expert",
        Color(0xFF673AB7) // Purple
    ),
    AiService(
        "perplexity",
        "Perplexity",
        "https://www.perplexity.ai/",
        "Research",
        "Citation-based search engine",
        Color(0xFF00AB97) // Teal
    ),
    AiService(
        "qwen",
        "Qwen",
        "https://chat.qwen.ai/",
        "Multilingual",
        "100+ languages support",
        Color(0xFFFF6D00) // Orange
    ),
    AiService(
        "mistral",
        "Mistral",
        "https://chat.mistral.ai/",
        "Efficiency",
        "Fast reasoning model",
        Color(0xFF3F51B5) // Indigo
    ),
    AiService(
        "blackbox",
        "Blackbox",
        "https://app.blackbox.ai/",
        "Coding",
        "Code optimization assistant",
        Color(0xFFFF5722) // Orange
    ),
    AiService(
        "copilot",
        "Copilot",
        "https://copilot.microsoft.com/",
        "Productivity",
        "Microsoft 365 integration",
        Color(0xFF0078D4) // Blue
    ),
    AiService(
        "brave",
        "Brave",
        "https://search.brave.com/ask",
        "Search",
        "Privacy-focused searching",
        Color(0xFF198038) // Green
    ),
    AiService(
        "huggingface",
        "HuggingFace",
        "https://huggingface.co/chat",
        "Open Source",
        "Open model experimentation",
        Color(0xFFFFD600) // Yellow
    ),
    AiService(
        "meta",
        "Meta AI",
        "https://www.meta.ai/",
        "Social",
        "Social platform integration",
        Color(0xFF0081FB) // Blue
    ),
    AiService(
        "euria",
        "Euria",
        "https://euria.infomaniak.com/",
        "Privacy First",
        "Swiss AI assistant",
        Color(0xFF0D47A1) // Blue
    ),
    AiService(
        "zai",
        "Z.ai",
        "https://chat.z.ai/",
        "Conversational",
        "Social AI assistant",
        Color(0xFF8E24AA) // Purple
    ),
    AiService(
        "h2ogpte",
        "H2O GPTe",
        "https://h2ogpte.genai.h2o.ai/",
        "Data Science",
        "Enterprise AI platform",
        Color(0xFF00695C) // Teal
    ),
    AiService(
        "dola",
        "Dola",
        "https://www.dola.com/chat",
        "General",
        "AI conversation assistant",
        Color(0xFF009688) // Teal
    ),
    AiService(
        "khoj",
        "Khoj",
        "https://app.khoj.dev/?v=app",
        "Productivity",
        "Personal AI assistant",
        Color(0xFFFF9800) // Orange
    ),
)

val serviceIcons = mapOf(
    "chatgpt" to Icons.Rounded.FlashOn,
    "duck" to Icons.Rounded.PrivacyTip,
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
    "copilot" to Icons.Rounded.RocketLaunch,
    "brave" to Icons.Rounded.Public,
    "huggingface" to Icons.Rounded.Cloud,
    "meta" to Icons.Rounded.People,
    "euria" to Icons.Rounded.Language,
    "zai" to Icons.Rounded.Forum,
    "h2ogpte" to Icons.Rounded.DataObject,
    "dola" to Icons.AutoMirrored.Rounded.Chat,
    "khoj" to Icons.Rounded.Psychology
)