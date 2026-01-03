package com.foss.aihub.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorOverlay(
    modifier: Modifier = Modifier,
    errorType: ErrorType,
    errorCode: Int = -1,
    errorMessage: String? = null,
    serviceName: String,
    accentColor: Color,
    onRetry: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLowest, modifier = modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {

            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(60.dp)
                            .padding(bottom = 16.dp)
                    ) {
                        Icon(
                            imageVector = errorType.icon,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(32.dp)
                        )
                    }


                    Text(
                        text = errorType.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))


                    Text(
                        text = errorType.getDescription(serviceName, errorCode, errorMessage),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.2,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp)
                    )


                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Button(
                            onClick = onRetry,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = accentColor
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Retry", style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }


                    if (errorMessage != null && errorMessage.isNotBlank()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                            ), shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                            ) {
                                Text(
                                    text = "Details",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Text(
                                    text = errorMessage.take(100) + if (errorMessage.length > 100) "..." else "",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }
            }


            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Service: $serviceName • Code: $errorCode",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}

enum class ErrorType(
    val icon: ImageVector, val title: String, val description: (String, Int, String?) -> String
) {
    NETWORK_ERROR(
        icon = Icons.Outlined.WifiOff, title = "No Connection", description = { serviceName, _, _ ->
            "Can't reach $serviceName. Check your internet connection."
        }),
    HTTP_ERROR(
        icon = Icons.Outlined.ErrorOutline,
        title = "Server Error",
        description = { serviceName, errorCode, _ ->
            "$serviceName returned error $errorCode"
        }),
    SSL_ERROR(
        icon = Icons.Outlined.ErrorOutline,
        title = "Security Issue",
        description = { serviceName, _, _ ->
            "Can't securely connect to $serviceName"
        }),
    UNKNOWN_ERROR(
        icon = Icons.Outlined.ErrorOutline,
        title = "Failed to Load",
        description = { serviceName, _, message ->
            "Couldn't load $serviceName. ${message?.take(80) ?: "Please try again."}"
        });

    fun getDescription(serviceName: String, errorCode: Int, errorMessage: String?): String {
        return description(serviceName, errorCode, errorMessage)
    }

    companion object {
        fun fromErrorCode(errorCode: Int): ErrorType {
            return when (errorCode) {
                -2, in 400..599 -> HTTP_ERROR
                -3 -> SSL_ERROR
                in listOf(-2, -4, -6, -7, -8, -10) -> NETWORK_ERROR
                else -> UNKNOWN_ERROR
            }
        }
    }
}