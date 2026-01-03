package com.foss.aihub.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.Block
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.DragIndicator
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Restore
import androidx.compose.material.icons.rounded.SmartToy
import androidx.compose.material.icons.rounded.TextIncrease
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material.icons.rounded.WebAsset
import androidx.compose.material.icons.rounded.ZoomIn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foss.aihub.ui.components.Md3TopAppBar
import com.foss.aihub.ui.webview.WebViewSecurity
import com.foss.aihub.utils.SettingsManager
import com.foss.aihub.utils.aiServices
import com.foss.aihub.utils.serviceIcons

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit, settingsManager: SettingsManager, onManageServicesClick: () -> Unit
) {
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

    val fontSizeOptions = listOf("x-small", "small", "medium", "large", "x-large")


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


    val orderedServices = remember(settings) {
        settings.serviceOrder.filter { it in settings.enabledServices }
            .mapNotNull { id -> aiServices.find { it.id == id } }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(), topBar = {
            Md3TopAppBar(
                title = "Settings", onBack = onBack
            )
        }, containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                SectionHeader(
                    title = "App Behavior",
                    icon = Icons.Rounded.Tune,
                    iconColor = MaterialTheme.colorScheme.primary
                )
            }

            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {

                        SettingItem(
                            title = "Load Last Opened AI",
                            description = "Open the last used AI when app starts",
                            icon = Icons.Rounded.Restore,
                            iconColor = MaterialTheme.colorScheme.primary,
                            trailingContent = {
                                Switch(
                                    checked = loadLastAi, onCheckedChange = { loadLastAi = it })
                            })

                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                        )


                        AnimatedVisibility(
                            visible = !loadLastAi,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Column {
                                SettingItem(
                                    title = "Default AI Assistant",
                                    description = "AI to open when app starts",
                                    icon = Icons.Rounded.Home,
                                    iconColor = MaterialTheme.colorScheme.primary,
                                    showTrailing = false
                                )


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
                                                    style = MaterialTheme.typography.labelMedium,
                                                    maxLines = 1
                                                )
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    serviceIcons[service.id]
                                                        ?: Icons.Rounded.SmartToy,
                                                    null,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            },
                                            colors = FilterChipDefaults.elevatedFilterChipColors(
                                                containerColor = MaterialTheme.colorScheme.surface,
                                                selectedContainerColor = service.accentColor,
                                                labelColor = MaterialTheme.colorScheme.onSurface,
                                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                                iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                                selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
                                            ),

                                            elevation = FilterChipDefaults.filterChipElevation(
                                                disabledElevation = 0.dp,
                                                pressedElevation = 3.dp,
                                                focusedElevation = 2.dp,
                                                hoveredElevation = 2.dp,
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

                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                        )


                        SettingItem(
                            title = "Manage AI Services",
                            description = "Enable/disable AI assistants",
                            icon = Icons.Rounded.Apps,
                            iconColor = MaterialTheme.colorScheme.primary,
                            onClick = onManageServicesClick,
                            trailingContent = {
                                Icon(
                                    Icons.Rounded.ChevronRight,
                                    contentDescription = "Go to manage services",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                            })
                    }
                }
            }


            item {
                SectionHeader(
                    title = "WebView Settings",
                    icon = Icons.Rounded.WebAsset,
                    iconColor = MaterialTheme.colorScheme.secondary
                )
            }

            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {

                        SettingItem(
                            title = "Zoom Controls",
                            description = "Enable pinch-to-zoom gestures",
                            icon = Icons.Rounded.ZoomIn,
                            iconColor = MaterialTheme.colorScheme.secondary,
                            trailingContent = {
                                Switch(
                                    checked = enableZoom, onCheckedChange = { enableZoom = it })
                            })

                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                        )


                        SettingItem(
                            title = "Font Size",
                            description = "Adjust text size in WebView",
                            icon = Icons.Rounded.TextIncrease,
                            iconColor = MaterialTheme.colorScheme.secondary,
                            onClick = { showFontSizeOptions = !showFontSizeOptions },
                            trailingContent = {
                                Icon(
                                    if (showFontSizeOptions) Icons.Rounded.ExpandLess
                                    else Icons.Rounded.ExpandMore,
                                    contentDescription = if (showFontSizeOptions) "Hide options" else "Show options",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            })


                        AnimatedVisibility(
                            visible = showFontSizeOptions,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                fontSizeOptions.forEachIndexed { index, option ->
                                    Row(modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {

                                            selectedFontSize = option
                                        }
                                        .padding(vertical = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(
                                            text = option.replaceFirstChar { it.uppercaseChar() },
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = if (selectedFontSize == option) MaterialTheme.colorScheme.primary
                                            else MaterialTheme.colorScheme.onSurfaceVariant
                                        )

                                        if (selectedFontSize == option) {
                                            Icon(
                                                Icons.Rounded.CheckCircle,
                                                contentDescription = "Selected",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }

                                    if (index < fontSizeOptions.size - 1) {
                                        HorizontalDivider(
                                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                                            thickness = 1.dp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            item {
                SectionHeader(
                    title = "Advanced",
                    icon = Icons.Rounded.Lock,
                    iconColor = MaterialTheme.colorScheme.tertiary
                )
            }

            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        SettingItem(
                            title = "Block Unnecessary Connections",
                            description = "Block ads, trackers, and analytics (recommended)",
                            icon = Icons.Rounded.Lock,
                            iconColor = MaterialTheme.colorScheme.tertiary,
                            trailingContent = {
                                Switch(
                                    checked = blockUnnecessaryConnections, onCheckedChange = {
                                        blockUnnecessaryConnections = it
                                        WebViewSecurity.isBlockingEnabled = it
                                    })
                            })
                    }
                }
            }



            item {
                SectionHeader(
                    title = "Quick Tips",
                    icon = Icons.Rounded.Lightbulb,
                    iconColor = MaterialTheme.colorScheme.primary
                )
            }

            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                        TipItem(
                            icon = Icons.Rounded.Refresh,
                            text = "Tap the AI card in the side drawer to quickly reload or refresh the current AI"
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        TipItem(
                            icon = Icons.Rounded.Block,
                            text = "If an AI isn't loading properly, try disabling \"Block Unnecessary Connections\" temporarily"
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        TipItem(
                            icon = Icons.Rounded.DragIndicator,
                            text = "You can reorder your AI assistants by long-pressing and dragging in \"Manage AI Services\""
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String, icon: ImageVector, iconColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(20.dp)
        )
        Text(
            title, style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            ), color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun SettingItem(
    title: String,
    description: String? = null,
    icon: ImageVector,
    iconColor: Color,
    onClick: (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    showTrailing: Boolean = true
) {
    val modifier = if (onClick != null) {
        Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    } else {
        Modifier.fillMaxWidth()
    }

    Row(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )


        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                title, style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                ), color = MaterialTheme.colorScheme.onSurface
            )
            if (description != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            }
        }


        if (showTrailing && trailingContent != null) {
            trailingContent()
        }
    }
}

@Composable
private fun TipItem(
    icon: ImageVector, text: String, modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {

        Surface(
            shape = CircleShape,
            tonalElevation = 2.dp,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(40.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(20.dp)
                )
            }
        }


        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
    }
}