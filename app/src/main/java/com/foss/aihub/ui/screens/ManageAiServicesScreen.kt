package com.foss.aihub.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material.icons.rounded.SmartToy
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.foss.aihub.models.AiService
import com.foss.aihub.ui.components.Md3TopAppBar
import com.foss.aihub.utils.SettingsManager
import com.foss.aihub.utils.aiServices
import com.foss.aihub.utils.serviceIcons
import java.util.Collections

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageAiServicesScreen(
    onBack: () -> Unit,
    enabledServices: Set<String>,
    defaultServiceId: String,
    loadLastAiEnabled: Boolean,
    onEnabledServicesChange: (Set<String>) -> Unit,
    settingsManager: SettingsManager
) {
    val settings by settingsManager.settingsFlow.collectAsState()
    val orderedServices = remember(settings) {
        settings.serviceOrder.mapNotNull { id -> aiServices.find { it.id == id } }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(), topBar = {
            Md3TopAppBar(
                title = "Manage AI Services", onBack = onBack
            )
        }, containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(orderedServices, key = { _, service -> service.id }) { index, service ->
                val isDefault = service.id == defaultServiceId
                val isOnlyEnabled = enabledServices.size == 1 && service.id in enabledServices
                val canDisable =
                    if (loadLastAiEnabled) !isOnlyEnabled else !isDefault && !isOnlyEnabled
                val isFirst = index == 0
                val isLast = index == orderedServices.lastIndex

                AiServiceItem(
                    service = service,
                    isEnabled = service.id in enabledServices,
                    canToggle = canDisable,
                    isDefault = isDefault,
                    loadLastAiEnabled = loadLastAiEnabled,
                    isFirst = isFirst,
                    isLast = isLast,
                    onToggle = { enabled ->
                        val newSet = enabledServices.toMutableSet().apply {
                            if (enabled) add(service.id) else remove(service.id)
                        }
                        onEnabledServicesChange(newSet)
                    },
                    onMoveUp = {
                        if (!isFirst) {
                            val newOrder = settings.serviceOrder.toMutableList().apply {
                                Collections.swap(this, index, index - 1)
                            }
                            settingsManager.updateSettings { it.serviceOrder = newOrder }
                        }
                    },
                    onMoveDown = {
                        if (!isLast) {
                            val newOrder = settings.serviceOrder.toMutableList().apply {
                                Collections.swap(this, index, index + 1)
                            }
                            settingsManager.updateSettings { it.serviceOrder = newOrder }
                        }
                    })
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = when {
                        loadLastAiEnabled -> "At least one AI assistant must remain enabled"
                        else -> "At least one AI assistant must remain enabled • The default cannot be disabled"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun AiServiceItem(
    service: AiService,
    isEnabled: Boolean,
    canToggle: Boolean,
    isDefault: Boolean,
    loadLastAiEnabled: Boolean,
    isFirst: Boolean,
    isLast: Boolean,
    onToggle: (Boolean) -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isEnabled) service.accentColor.copy(alpha = 0.08f)
            else MaterialTheme.colorScheme.surfaceContainer
        ),
        border = if (!isEnabled) BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant
        ) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CompactServiceIcon(service = service, isEnabled = isEnabled)

            Column(
                modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = service.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = if (isEnabled) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    if (isDefault && !loadLastAiEnabled) {
                        DefaultBadge()
                    }
                }

                Text(
                    text = service.category,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = if (isEnabled) 1f else 0.7f)
                )

                if (!canToggle && isEnabled) {
                    Text(
                        text = if (isDefault && !loadLastAiEnabled) "Default AI cannot be disabled"
                        else "Last enabled AI cannot be disabled",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            TrailingControls(
                isEnabled = isEnabled,
                canToggle = canToggle,
                isFirst = isFirst,
                isLast = isLast,
                onToggle = onToggle,
                onMoveUp = onMoveUp,
                onMoveDown = onMoveDown
            )
        }
    }
}

@Composable
private fun CompactServiceIcon(service: AiService, isEnabled: Boolean) {
    Surface(
        shape = CircleShape, color = if (isEnabled) {
            service.accentColor.copy(alpha = 0.12f)
        } else {
            MaterialTheme.colorScheme.surfaceContainer
        }, tonalElevation = 4.dp, modifier = Modifier.size(48.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = serviceIcons[service.id] ?: Icons.Rounded.SmartToy,
                contentDescription = null,
                tint = if (isEnabled) service.accentColor else service.accentColor.copy(alpha = 0.6f),
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
private fun DefaultBadge() {
    AssistChip(
        onClick = {}, label = {
        Text(
            "Default",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold
        )
    }, colors = AssistChipDefaults.assistChipColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        labelColor = MaterialTheme.colorScheme.onPrimaryContainer
    ), border = null
    )
}

@Composable
private fun TrailingControls(
    isEnabled: Boolean,
    canToggle: Boolean,
    isFirst: Boolean,
    isLast: Boolean,
    onToggle: (Boolean) -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Switch(
            checked = isEnabled,
            onCheckedChange = { if (canToggle || !isEnabled) onToggle(it) },
            enabled = canToggle || !isEnabled,
            colors = SwitchDefaults.colors(
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
            IconButton(
                onClick = onMoveUp, enabled = !isFirst, modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    Icons.Rounded.ArrowDropUp,
                    contentDescription = "Move up",
                    tint = if (!isFirst) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(
                onClick = onMoveDown, enabled = !isLast, modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    Icons.Rounded.ArrowDropDown,
                    contentDescription = "Move down",
                    tint = if (!isLast) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}