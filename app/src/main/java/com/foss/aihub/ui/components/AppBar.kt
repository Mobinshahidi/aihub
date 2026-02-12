package com.foss.aihub.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.foss.aihub.models.AiService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiHubAppBar(
selectedService: AiService,
onMenuClick: () -> Unit,
onSettingsClick: () -> Unit,
modifier: Modifier = Modifier,
titleOverride: String? = null
) {
val title = titleOverride ?: selectedService.name
TopAppBar(
modifier = modifier, title = {
AnimatedContent(
targetState = title, transitionSpec = {
fadeIn(animationSpec = tween(220, delayMillis = 90)) togetherWith fadeOut(
animationSpec = tween(90)
)
}, label = "service title"
) { currentTitle ->
Text(
text = currentTitle,
style = MaterialTheme.typography.titleLarge,
fontWeight = FontWeight.Medium,
maxLines = 1,
overflow = TextOverflow.Ellipsis
)
}
}, navigationIcon = {
IconButton(onClick = onMenuClick) {
Icon(Icons.Rounded.Menu, "Menu")
}
}, actions = {
IconButton(onClick = onSettingsClick) {
Icon(Icons.Rounded.Settings, "Settings")
}
}, colors = TopAppBarDefaults.topAppBarColors(
containerColor = MaterialTheme.colorScheme.surface,
titleContentColor = MaterialTheme.colorScheme.onSurface,
navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
actionIconContentColor = MaterialTheme.colorScheme.onSurface
)
)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Md3TopAppBar(
title: String,
onBack: (() -> Unit)? = null,
scrollBehavior: TopAppBarScrollBehavior? = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
) {
TopAppBar(
title = {
Text(
text = title, style = MaterialTheme.typography.titleLarge
)
}, navigationIcon = {
if (onBack != null) {
IconButton(onClick = onBack) {
Icon(
imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
contentDescription = "Back"
)
}
}
}, colors = TopAppBarDefaults.topAppBarColors(
containerColor = MaterialTheme.colorScheme.surface,
scrolledContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
titleContentColor = MaterialTheme.colorScheme.onSurface,
navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
actionIconContentColor = MaterialTheme.colorScheme.onSurface
), scrollBehavior = scrollBehavior
)
}
