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
    val typography = MaterialTheme.typography

    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                state == WebViewState.ERROR -> serviceColor.copy(alpha = 0.04f)
                isSelected -> serviceColor.copy(alpha = 0.08f)
                else -> colorScheme.surfaceContainer
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = when {
            state == WebViewState.ERROR -> BorderStroke(
                1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.3f)
            )

            isSelected -> BorderStroke(1.dp, serviceColor.copy(alpha = 0.24f))
            else -> null
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CompactLeadingIconWithState(
                service = service,
                serviceColor = serviceColor,
                isSelected = isSelected,
                state = state
            )

            Column(
                modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = service.name,
                        style = typography.titleSmall,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = when {
                            state == WebViewState.ERROR -> MaterialTheme.colorScheme.error
                            isSelected -> serviceColor
                            else -> colorScheme.onSurface
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    CompactStatusIndicator(
                        state = state, serviceColor = serviceColor
                    )
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
                            style = typography.labelSmall,
                            color = when (state) {
                                WebViewState.LOADING -> colorScheme.onSurfaceVariant
                                WebViewState.ERROR -> MaterialTheme.colorScheme.error
                                WebViewState.SUCCESS -> serviceColor
                                else -> colorScheme.onSurfaceVariant
                            },
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Surface(
                            color = serviceColor.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(50),
                            border = BorderStroke(0.5.dp, serviceColor.copy(alpha = 0.24f)),
                            modifier = Modifier.height(18.dp)
                        ) {
                            Text(
                                text = service.category,
                                style = typography.labelSmall,
                                fontWeight = FontWeight.Medium,
                                color = serviceColor,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }

                        Text(
                            text = "•",
                            style = typography.bodySmall,
                            color = colorScheme.outline.copy(alpha = 0.6f)
                        )

                        Text(
                            text = service.description,
                            style = typography.bodySmall,
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
    service: AiService,
    serviceColor: Color,
    isSelected: Boolean,
    state: WebViewState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(36.dp), contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = CircleShape, color = when {
                state == WebViewState.ERROR -> MaterialTheme.colorScheme.errorContainer
                isSelected -> serviceColor.copy(alpha = 0.12f)
                else -> Color.Transparent
            }, border = BorderStroke(
                width = when {
                    state == WebViewState.ERROR -> 1.5.dp
                    isSelected -> 1.5.dp
                    else -> 0.5.dp
                }, color = when {
                    state == WebViewState.ERROR -> MaterialTheme.colorScheme.error.copy(alpha = 0.3f)
                    isSelected -> serviceColor.copy(alpha = 0.32f)
                    else -> serviceColor.copy(alpha = 0.12f)
                }
            ), modifier = Modifier.size(32.dp)
        ) {
            when (state) {
                WebViewState.LOADING -> LoadingSpinner(
                    color = serviceColor, modifier = Modifier.size(18.dp)
                )

                WebViewState.ERROR -> Icon(
                    imageVector = Icons.Rounded.Error,
                    contentDescription = "Error",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(18.dp)
                )

                else -> Icon(
                    imageVector = serviceIcons[service.id] ?: Icons.Rounded.SmartToy,
                    contentDescription = null,
                    tint = if (isSelected) serviceColor else serviceColor.copy(alpha = 0.74f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun LoadingSpinner(
    color: Color, modifier: Modifier = Modifier
) {
    val animatedRotation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animatedRotation.animateTo(
            targetValue = 360f, animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1000, easing = androidx.compose.animation.core.LinearEasing
                ), repeatMode = RepeatMode.Restart
            )
        )
    }

    Icon(
        imageVector = Icons.Rounded.Refresh,
        contentDescription = "Loading",
        tint = color,
        modifier = modifier.rotate(animatedRotation.value)
    )
}

@Composable
private fun CompactStatusIndicator(
    state: WebViewState, serviceColor: Color, modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(20.dp), contentAlignment = Alignment.Center
    ) {
        when (state) {
            WebViewState.SUCCESS -> Icon(
                imageVector = Icons.Rounded.CheckCircle,
                contentDescription = "Service loaded",
                tint = serviceColor,
                modifier = Modifier.size(16.dp)
            )

            WebViewState.ERROR -> Icon(
                imageVector = Icons.Rounded.Error,
                contentDescription = "Error",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(14.dp)
            )

            WebViewState.LOADING -> Box(
                modifier = Modifier.size(12.dp), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    strokeWidth = 2.dp, color = serviceColor, modifier = Modifier.size(10.dp)
                )
            }

            else -> {
                // Nothing
            }
        }
    }
}