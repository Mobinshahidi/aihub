package com.foss.aihub.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.SmartToy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.foss.aihub.models.AiService
import com.foss.aihub.models.WebViewState
import com.foss.aihub.utils.serviceIcons

@Composable
fun Md3ServiceCard(
    service: AiService,
    serviceColor: Color,
    isSelected: Boolean,
    state: WebViewState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                state == WebViewState.ERROR -> colorScheme.errorContainer.copy(alpha = 0.12f)
                isSelected -> serviceColor.copy(alpha = 0.10f)
                else -> colorScheme.surfaceContainerLowest
            }
        ),
        border = when {
            state == WebViewState.ERROR -> BorderStroke(1.dp, colorScheme.error.copy(alpha = 0.28f))
            isSelected -> BorderStroke(1.5.dp, serviceColor.copy(alpha = 0.30f))
            else -> null
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            CompactLeadingIconWithState(
                service = service,
                serviceColor = serviceColor,
                isSelected = isSelected,
                state = state
            )

            Column(
                modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = service.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        color = when {
                            state == WebViewState.ERROR -> colorScheme.error
                            isSelected -> serviceColor
                            else -> colorScheme.onSurface
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    CompactStatusIndicator(state = state, serviceColor = serviceColor)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (state != WebViewState.IDLE) {
                        Text(
                            text = when (state) {
                                WebViewState.LOADING -> "Loading..."
                                WebViewState.ERROR -> "Failed to load"
                                WebViewState.SUCCESS -> "Ready"
                                else -> ""
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = when (state) {
                                WebViewState.LOADING -> colorScheme.onSurfaceVariant
                                WebViewState.ERROR -> colorScheme.error
                                WebViewState.SUCCESS -> serviceColor
                                else -> colorScheme.onSurfaceVariant
                            },
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Surface(
                            color = serviceColor.copy(alpha = 0.09f),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(0.8.dp, serviceColor.copy(alpha = 0.25f)),
                            modifier = Modifier.height(22.dp)
                        ) {
                            Text(
                                text = service.category,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Medium,
                                color = serviceColor,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                            )
                        }

                        Text(
                            text = "•",
                            style = MaterialTheme.typography.bodySmall,
                            color = colorScheme.outline.copy(alpha = 0.5f)
                        )

                        Text(
                            text = service.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun CompactLeadingIconWithState(
    service: AiService, serviceColor: Color, isSelected: Boolean, state: WebViewState
) {
    Surface(
        shape = CircleShape, color = when {
            state == WebViewState.ERROR -> MaterialTheme.colorScheme.errorContainer
            isSelected -> serviceColor.copy(alpha = 0.14f)
            else -> Color.Transparent
        }, border = BorderStroke(
            width = if (isSelected || state == WebViewState.ERROR) 1.5.dp else 0.8.dp,
            color = when {
                state == WebViewState.ERROR -> MaterialTheme.colorScheme.error.copy(alpha = 0.35f)
                isSelected -> serviceColor.copy(alpha = 0.35f)
                else -> serviceColor.copy(alpha = 0.18f)
            }
        ), tonalElevation = if (isSelected) 6.dp else 0.dp, modifier = Modifier.size(48.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            when (state) {
                WebViewState.LOADING -> LoadingSpinner(
                    color = serviceColor, modifier = Modifier.size(24.dp)
                )

                WebViewState.ERROR -> Icon(
                    Icons.Rounded.Error,
                    contentDescription = "Error",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(24.dp)
                )

                else -> Icon(
                    imageVector = serviceIcons[service.id] ?: Icons.Rounded.SmartToy,
                    contentDescription = null,
                    tint = if (isSelected) serviceColor else serviceColor.copy(alpha = 0.8f),
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}

@Composable
private fun LoadingSpinner(
    color: Color, modifier: Modifier = Modifier
) {
    val rotation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        rotation.animateTo(
            targetValue = 360f, animationSpec = infiniteRepeatable(
                animation = tween(900, easing = androidx.compose.animation.core.LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    Icon(
        imageVector = Icons.Rounded.Refresh,
        contentDescription = "Loading",
        tint = color,
        modifier = modifier.rotate(rotation.value)
    )
}

@Composable
private fun CompactStatusIndicator(
    state: WebViewState, serviceColor: Color
) {
    Box(modifier = Modifier.size(24.dp), contentAlignment = Alignment.Center) {
        when (state) {
            WebViewState.SUCCESS -> Icon(
                Icons.Rounded.CheckCircle,
                contentDescription = "Loaded",
                tint = serviceColor,
                modifier = Modifier.size(20.dp)
            )

            WebViewState.ERROR -> Icon(
                Icons.Rounded.Error,
                contentDescription = "Error",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(18.dp)
            )

            WebViewState.LOADING -> CircularProgressIndicator(
                strokeWidth = 2.5.dp, color = serviceColor, modifier = Modifier.size(18.dp)
            )

            else -> {}
        }
    }
}