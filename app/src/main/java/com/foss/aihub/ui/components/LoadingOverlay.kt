package com.foss.aihub.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun LoadingOverlay(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    isRefreshing: Boolean = false,
    serviceName: String,
    accentColor: Color,
    progress: Int = 0
) {
    AnimatedVisibility(
        visible = isVisible, enter = fadeIn(), exit = fadeOut(), modifier = modifier
    ) {
        Surface(
            color = Color.Black.copy(alpha = 0.8f), modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(32.dp)
            ) {

                Card(
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    modifier = Modifier.size(80.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()
                    ) {
                        if (progress > 0) {

                            CircularProgressIndicator(
                                progress = { progress / 100f },
                                color = accentColor,
                                strokeWidth = 4.dp,
                                strokeCap = StrokeCap.Round
                            )
                            Text(
                                text = "${progress}%",
                                style = MaterialTheme.typography.labelSmall,
                                color = accentColor,
                                fontWeight = FontWeight.Medium
                            )
                        } else {

                            CircularProgressIndicator(
                                color = accentColor, strokeWidth = 4.dp, strokeCap = StrokeCap.Round
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = serviceName,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = if (isRefreshing) "Refreshing..." else "Loading...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )

                    if (progress > 0) {
                        Spacer(modifier = Modifier.height(16.dp))
                        LinearProgressIndicator(
                            progress = { progress / 100f },
                            color = accentColor,
                            trackColor = accentColor.copy(alpha = 0.2f),
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                        )
                    }
                }
            }
        }
    }
}