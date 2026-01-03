package com.foss.aihub.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foss.aihub.R
import com.foss.aihub.models.AiService
import com.foss.aihub.models.WebViewState
import com.foss.aihub.ui.screens.dialogs.AboutDialog
import com.foss.aihub.utils.aiServices

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerContent(
    selectedService: AiService,
    onServiceSelected: (AiService) -> Unit,
    onServiceReload: (AiService) -> Unit,
    webViewStates: Map<String, WebViewState>,
    enabledServices: Set<String>,
    serviceOrder: List<String>,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    val orderedEnabledServices = remember(enabledServices, serviceOrder) {
        serviceOrder.filter { it in enabledServices }
            .mapNotNull { id -> aiServices.find { it.id == id } }
    }

    var showAboutDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .fillMaxWidth(0.86f)
            .clip(RoundedCornerShape(topEnd = 28.dp, bottomEnd = 28.dp))
            .shadow(12.dp, RoundedCornerShape(topEnd = 28.dp, bottomEnd = 28.dp)),
        color = colorScheme.surfaceContainerLowest,
        tonalElevation = 2.dp,
        border = BorderStroke(
            width = 0.5.dp, color = colorScheme.outline.copy(alpha = 0.15f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                colorScheme.primary.copy(alpha = 0.08f), Color.Transparent
                            )
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = WindowInsets.statusBars.asPaddingValues()
                                .calculateTopPadding() + 28.dp, start = 28.dp, end = 28.dp
                        )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = colorScheme.primary,
                            tonalElevation = 6.dp,
                            modifier = Modifier
                                .size(60.dp)
                                .shadow(8.dp, CircleShape)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_launcher_foreground),
                                    contentDescription = "AI Hub Logo",
                                    tint = colorScheme.onPrimary,
                                    modifier = Modifier.size(60.dp)
                                )
                            }
                        }

                        Column {
                            Text(
                                "AI Hub", style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold, letterSpacing = (-0.5).sp
                                ), color = colorScheme.onSurface
                            )
                            Text(
                                "Your AI Companion Collection",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = colorScheme.onSurfaceVariant,
                                letterSpacing = 0.2.sp
                            )
                        }
                    }
                }
            }

            Text(
                "AI Assistants", style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ), color = colorScheme.onSurface, modifier = Modifier.padding(
                    top = 20.dp, start = 28.dp, end = 28.dp, bottom = 12.dp
                )
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                items(orderedEnabledServices) { service ->
                    val state = webViewStates[service.id] ?: WebViewState.IDLE
                    Md3ServiceCard(
                        service = service,
                        serviceColor = service.accentColor,
                        isSelected = selectedService.id == service.id,
                        state = state,
                        onClick = {
                            if (selectedService.id == service.id) {
                                onServiceReload(service)
                            } else {
                                onServiceSelected(service)
                            }
                        })
                }
            }

            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = colorScheme.surfaceContainerHigh
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = colorScheme.primaryContainer,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    "${orderedEnabledServices.size}",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = colorScheme.onPrimaryContainer
                                )
                            }
                        }

                        Column {
                            Text(
                                "AI Assistants", style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.SemiBold
                                ), color = colorScheme.onSurface
                            )
                            Text(
                                "Ready to assist",
                                style = MaterialTheme.typography.labelSmall,
                                color = colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    FilledTonalButton(
                        onClick = { showAboutDialog = true },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.Rounded.Info,
                                contentDescription = "About",
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                "About", style = MaterialTheme.typography.labelMedium, maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAboutDialog) {
        AboutDialog { showAboutDialog = false }
    }
}