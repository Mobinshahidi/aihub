package com.foss.aihub.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.CloudSync
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.DragIndicator
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Memory
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.SmartToy
import androidx.compose.material.icons.outlined.TextIncrease
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.WebAsset
import androidx.compose.material.icons.outlined.ZoomIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foss.aihub.ui.components.Md3TopAppBar
import com.foss.aihub.ui.webview.WebViewSecurity
import com.foss.aihub.utils.DomainConfigUpdater
import com.foss.aihub.utils.SettingsManager
import com.foss.aihub.utils.aiServices
import com.foss.aihub.utils.serviceIcons
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    settingsManager: SettingsManager,
    onManageServicesClick: () -> Unit,
    onClearCache: () -> Unit,
    onClearData: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var blockUnnecessaryConnections by remember {
        mutableStateOf(WebViewSecurity.isBlockingEnabled)
    }

    val settings by settingsManager.settingsFlow.collectAsState()

    var showFontSizeOptions by remember { mutableStateOf(false) }
    var loadLastAi by remember { mutableStateOf(settings.loadLastOpenedAI) }
    var selectedFontSize by remember { mutableStateOf(settings.fontSize) }
    var defaultServiceId by remember { mutableStateOf(settings.defaultServiceId) }
    var enabledServices by remember { mutableStateOf(settings.enabledServices) }
    var enableZoom by remember { mutableStateOf(settings.enableZoom) }
    var limitSimultaneousAIs by remember { mutableStateOf(settings.maxKeepAlive != Int.MAX_VALUE) }
    var maxKeepAlive by remember {
        mutableIntStateOf(
            if (settings.maxKeepAlive == Int.MAX_VALUE) 5 else settings.maxKeepAlive
        )
    }

    val fontSizeOptions = listOf("x-small", "small", "medium", "large", "x-large")

    var showClearCacheDialog by remember { mutableStateOf(false) }
    var showClearDataDialog by remember { mutableStateOf(false) }

    var isUpdating by remember { mutableStateOf(false) }

    var configVersion by remember {
        mutableStateOf(settingsManager.getDomainConfigVersion() ?: "Not loaded")
    }

    val refreshVersion = {
        configVersion = settingsManager.getDomainConfigVersion() ?: "Not loaded"
    }

    LaunchedEffect(loadLastAi) {
        settingsManager.updateSettings { it.loadLastOpenedAI = loadLastAi }
    }

    LaunchedEffect(defaultServiceId) {
        settingsManager.updateSettings { it.defaultServiceId = defaultServiceId }
    }

    LaunchedEffect(enabledServices) {
        settingsManager.updateSettings { it.enabledServices = enabledServices }
        if (defaultServiceId !in enabledServices && enabledServices.isNotEmpty()) {
            defaultServiceId = enabledServices.first()
        }
    }

    LaunchedEffect(selectedFontSize) {
        settingsManager.updateSettings { it.fontSize = selectedFontSize }
    }

    LaunchedEffect(enableZoom) {
        settingsManager.updateSettings { it.enableZoom = enableZoom }
    }

    LaunchedEffect(limitSimultaneousAIs, maxKeepAlive) {
        val value = if (limitSimultaneousAIs) maxKeepAlive else Int.MAX_VALUE
        settingsManager.updateSettings { it.maxKeepAlive = value }
    }

    val orderedServices = remember(settings) {
        settings.serviceOrder.filter { it in settings.enabledServices }
            .mapNotNull { id -> aiServices.find { it.id == id } }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(), topBar = {
        Md3TopAppBar(
            title = "Settings", onBack = onBack
        )
    }, snackbarHost = {
        SnackbarHost(hostState = snackbarHostState) { data ->
            ModernSnackbar(snackbarData = data)
        }
    }, containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                SettingSectionHeader(
                    title = "App Behavior",
                    icon = Icons.Outlined.Tune,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            item {
                ModernCard {
                    Column {
                        ListItem(headlineContent = {
                            Text(
                                "Load last opened AI",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }, supportingContent = {
                            Text("Reopen the last used assistant on launch")
                        }, leadingContent = {
                            IconBox(
                                icon = Icons.Outlined.Restore,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }, trailingContent = {
                            Switch(
                                checked = loadLastAi,
                                onCheckedChange = { loadLastAi = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                                    checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        })

                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

                        AnimatedVisibility(
                            visible = !loadLastAi,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Column {
                                ListItem(headlineContent = {
                                    Text(
                                        "Default AI Assistant",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                }, supportingContent = {
                                    Text("Shown when app starts")
                                }, leadingContent = {
                                    IconBox(
                                        icon = Icons.Outlined.Home,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                })

                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    orderedServices.forEach { service ->
                                        ElevatedFilterChip(
                                            selected = defaultServiceId == service.id,
                                            onClick = { defaultServiceId = service.id },
                                            label = {
                                                Text(
                                                    service.name,
                                                    style = MaterialTheme.typography.labelLarge,
                                                    maxLines = 1
                                                )
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    serviceIcons[service.id]
                                                        ?: Icons.Outlined.SmartToy,
                                                    null,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            },
                                            colors = FilterChipDefaults.elevatedFilterChipColors(
                                                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                                                selectedContainerColor = service.accentColor,
                                                labelColor = MaterialTheme.colorScheme.onSurface,
                                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                                iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                                selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        )
                                    }

                                    if (orderedServices.isEmpty()) {
                                        Text(
                                            "No AI services enabled",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(8.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

                        ListItem(headlineContent = {
                            Text(
                                "Manage AI Services",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }, supportingContent = {
                            Text("Enable, disable & reorder assistants")
                        }, leadingContent = {
                            IconBox(
                                icon = Icons.Outlined.Apps,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }, trailingContent = {
                            Icon(
                                Icons.Outlined.ChevronRight,
                                contentDescription = "Go to manage services",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }, modifier = Modifier.clickable { onManageServicesClick() })
                    }
                }
            }

            item {
                SettingSectionHeader(
                    title = "Performance",
                    icon = Icons.Outlined.Memory,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            item {
                ModernCard {
                    Column {
                        ListItem(headlineContent = {
                            Text(
                                "Limit simultaneous AIs",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }, supportingContent = {
                            Text("Reduces memory usage")
                        }, leadingContent = {
                            IconBox(
                                icon = Icons.Outlined.Layers,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }, trailingContent = {
                            Switch(
                                checked = limitSimultaneousAIs,
                                onCheckedChange = { limitSimultaneousAIs = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = MaterialTheme.colorScheme.secondary,
                                    checkedTrackColor = MaterialTheme.colorScheme.secondaryContainer
                                )
                            )
                        })

                        AnimatedVisibility(
                            visible = limitSimultaneousAIs,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "Max loaded AIs",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        "$maxKeepAlive",
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(Modifier.height(8.dp))

                                Slider(
                                    value = (maxKeepAlive - 1).toFloat(),
                                    onValueChange = {
                                        maxKeepAlive = (it.roundToInt() + 1).coerceIn(1, 5)
                                    },
                                    valueRange = 0f..4f,
                                    steps = 3,
                                    colors = SliderDefaults.colors(
                                        thumbColor = MaterialTheme.colorScheme.secondary,
                                        activeTrackColor = MaterialTheme.colorScheme.secondary,
                                        inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
                                    )
                                )

                                Spacer(Modifier.height(4.dp))

                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    (1..5).forEach { value ->
                                        Text(
                                            text = "$value",
                                            color = if (maxKeepAlive == value) MaterialTheme.colorScheme.secondary
                                            else MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontWeight = if (maxKeepAlive == value) FontWeight.Bold else FontWeight.Normal,
                                            modifier = Modifier.clickable { maxKeepAlive = value })
                                    }
                                }

                                Spacer(Modifier.height(12.dp))

                                Text(
                                    text = when (maxKeepAlive) {
                                        1 -> "Minimal memory – reloads often"
                                        2 -> "Good balance"
                                        3 -> "Recommended"
                                        4 -> "Smooth switching"
                                        5 -> "Most convenient"
                                        else -> ""
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            item {
                SettingSectionHeader(
                    title = "WebView",
                    icon = Icons.Outlined.WebAsset,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            item {
                ModernCard {
                    Column {
                        ListItem(headlineContent = {
                            Text(
                                "Pinch to zoom",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }, leadingContent = {
                            IconBox(
                                icon = Icons.Outlined.ZoomIn,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }, trailingContent = {
                            Switch(
                                checked = enableZoom,
                                onCheckedChange = { enableZoom = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = MaterialTheme.colorScheme.secondary,
                                    checkedTrackColor = MaterialTheme.colorScheme.secondaryContainer
                                )
                            )
                        })

                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

                        ListItem(headlineContent = {
                            Text(
                                "Font size",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }, leadingContent = {
                            IconBox(
                                icon = Icons.Outlined.TextIncrease,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }, trailingContent = {
                            Icon(
                                if (showFontSizeOptions) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                                null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }, modifier = Modifier.clickable {
                            showFontSizeOptions = !showFontSizeOptions
                        })

                        AnimatedVisibility(
                            visible = showFontSizeOptions,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Column(
                                modifier = Modifier.padding(
                                    start = 72.dp, end = 16.dp, bottom = 12.dp
                                )
                            ) {
                                fontSizeOptions.forEach { option ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { selectedFontSize = option }
                                            .padding(vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = option.replaceFirstChar { it.uppercase() },
                                            modifier = Modifier.weight(1f),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        if (selectedFontSize == option) {
                                            Icon(
                                                Icons.Outlined.CheckCircle,
                                                null,
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                SettingSectionHeader(
                    title = "Storage",
                    icon = Icons.Outlined.DeleteSweep,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            item {
                ModernCard {
                    Column {
                        ListItem(headlineContent = {
                            Text(
                                "Clear cache",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }, supportingContent = {
                            Text("Removes temporary files")
                        }, leadingContent = {
                            IconBox(
                                icon = Icons.Outlined.Delete,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }, trailingContent = {
                            Icon(Icons.Outlined.ChevronRight, null)
                        }, modifier = Modifier.clickable { showClearCacheDialog = true })

                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

                        ListItem(headlineContent = {
                            Text(
                                "Clear all data",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.error
                            )
                        }, supportingContent = {
                            Text("Resets everything – use carefully")
                        }, leadingContent = {
                            IconBox(
                                icon = Icons.Outlined.DeleteSweep,
                                color = MaterialTheme.colorScheme.error
                            )
                        }, trailingContent = {
                            Icon(
                                Icons.Outlined.ChevronRight,
                                null,
                                tint = MaterialTheme.colorScheme.error
                            )
                        }, modifier = Modifier.clickable { showClearDataDialog = true })
                    }
                }
            }

            item {
                SettingSectionHeader(
                    title = "Security",
                    icon = Icons.Outlined.Security,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            item {
                ModernCard {
                    ListItem(headlineContent = {
                        Text(
                            "Block trackers & ads",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }, supportingContent = {
                        Text("Recommended for privacy")
                    }, leadingContent = {
                        IconBox(
                            icon = Icons.Outlined.Block, color = MaterialTheme.colorScheme.tertiary
                        )
                    }, trailingContent = {
                        Switch(
                            checked = blockUnnecessaryConnections, onCheckedChange = {
                                blockUnnecessaryConnections = it
                                WebViewSecurity.isBlockingEnabled = it
                            }, colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.tertiary,
                                checkedTrackColor = MaterialTheme.colorScheme.tertiaryContainer
                            )
                        )
                    })
                }
            }

            item {
                SettingSectionHeader(
                    title = "Domain Security",
                    icon = Icons.Outlined.CloudSync,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            item {
                ModernCard {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    "Rules version",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    configVersion,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Button(
                                onClick = {
                                    scope.launch {
                                        isUpdating = true
                                        try {
                                            val msg = DomainConfigUpdater.updateIfNeeded(context)
                                            snackbarHostState.showSnackbar(
                                                message = msg,
                                                actionLabel = null,
                                                withDismissAction = true,
                                                duration = SnackbarDuration.Short
                                            )
                                        } catch (e: Exception) {
                                            snackbarHostState.showSnackbar(
                                                message = "Failed to update: ${e.message ?: "No internet connection"}",
                                                withDismissAction = true,
                                                duration = SnackbarDuration.Long
                                            )
                                        } finally {
                                            refreshVersion()
                                            isUpdating = false
                                        }
                                    }
                                }, enabled = !isUpdating, colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.tertiary,
                                    contentColor = MaterialTheme.colorScheme.onTertiary
                                )
                            ) {
                                if (isUpdating) {
                                    CircularProgressIndicator(
                                        Modifier.size(18.dp), strokeWidth = 2.dp
                                    )
                                    Spacer(Modifier.width(12.dp))
                                }
                                Text(if (isUpdating) "Updating…" else "Check update")
                            }
                        }
                    }
                }
            }

            item {
                SettingSectionHeader(
                    title = "Tips",
                    icon = Icons.Outlined.Lightbulb,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            item {
                ModernCard(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.Refresh,
                                null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.width(16.dp))
                            Text(
                                "Tap AI card in drawer → quick reload",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.Block,
                                null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.width(16.dp))
                            Text(
                                "Disable ad-blocker temporarily if site won't load",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.DragIndicator,
                                null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.width(16.dp))
                            Text(
                                "Long-press & drag to reorder AIs in Manage section",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.Memory,
                                null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.width(16.dp))
                            Text(
                                "Limit loaded AIs on low-RAM devices",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(24.dp)) }
        }
    }

    if (showClearCacheDialog) {
        AlertDialog(
            onDismissRequest = { showClearCacheDialog = false },
            title = { Text("Clear Cache") },
            text = { Text("Removes temporary web files.\nMay log you out of sites.") },
            confirmButton = {
                TextButton(onClick = {
                    onClearCache()
                    showClearCacheDialog = false
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Cache cleared successfully",
                            actionLabel = null,
                            withDismissAction = true,
                            duration = SnackbarDuration.Short
                        )
                    }
                }) {
                    Text("Clear", color = MaterialTheme.colorScheme.primary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearCacheDialog = false }) { Text("Cancel") }
            })
    }

    if (showClearDataDialog) {
        AlertDialog(
            onDismissRequest = { showClearDataDialog = false },
            title = { Text("Clear All Data", color = MaterialTheme.colorScheme.error) },
            text = {
                Text(
                    "Deletes cache, cookies, history, logins…\n" + "This cannot be undone."
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    onClearData()
                    showClearDataDialog = false
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "All data cleared successfully",
                            actionLabel = null,
                            withDismissAction = true,
                            duration = SnackbarDuration.Short
                        )
                    }
                }) {
                    Text("Clear Everything", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDataDialog = false }) { Text("Cancel") }
            })
    }
}

@Composable
private fun SettingSectionHeader(
    title: String, icon: ImageVector, color: androidx.compose.ui.graphics.Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = color.copy(alpha = 0.12f),
            modifier = Modifier
                .size(36.dp)
                .padding(6.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    icon, null, tint = color, modifier = Modifier.size(18.dp)
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Text(
            title,
            style = MaterialTheme.typography.titleMedium,
            color = color,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.3.sp
        )
    }
}

@Composable
private fun IconBox(
    icon: ImageVector, color: androidx.compose.ui.graphics.Color
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            icon, null, tint = color, modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun ModernCard(
    containerColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                spotColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
            ), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(
            containerColor = containerColor
        ), elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(content = content)
    }
}

@Composable
fun ModernSnackbar(
    snackbarData: androidx.compose.material3.SnackbarData, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.inverseSurface,
            contentColor = MaterialTheme.colorScheme.inverseOnSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = snackbarData.visuals.message,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )

            snackbarData.visuals.actionLabel?.let { actionLabel ->
                TextButton(
                    onClick = { snackbarData.performAction() },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.inverseOnSurface
                    ),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        actionLabel,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}