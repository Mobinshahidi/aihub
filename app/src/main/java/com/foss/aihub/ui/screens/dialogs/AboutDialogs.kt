package com.foss.aihub.ui.screens.dialogs

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AboutDialog(onDismiss: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    var isVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        isVisible = true
    }

    Dialog(
        onDismissRequest = {
            isVisible = false
            scope.launch {
                delay(300)
                onDismiss()
            }
            onDismiss()
        }, properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn() + scaleIn(initialScale = 0.8f),
            exit = fadeOut() + scaleOut(targetScale = 1.2f)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(28.dp))
                    .shadow(16.dp, RoundedCornerShape(28.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                colorScheme.surface, colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            )
                        )
                    ), tonalElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    AnimatedContent(targetState = "About AI Hub") { title ->
                        Text(
                            title, style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold, color = colorScheme.primary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))


                    Surface(
                        color = colorScheme.surfaceContainerLow,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "The Story Behind AI Hub",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "As a FOSS enthusiast, I avoid proprietary AI apps and use these services through their PWAs in the browser instead. But constantly switching between tabs and interfaces became frustrating. So I built AI Hub — a simple, open-source app that brings all these AI assistants together in one clean place, keeping the FOSS spirit while making switching effortless",
                                style = MaterialTheme.typography.bodyMedium,
                                color = colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))


                    Surface(
                        color = colorScheme.primaryContainer.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Smart Work > Hard Work",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "I've used AI to build some parts of this app. Smart work is better than hard work — modern AI tools helped me accelerate development, generate code ideas, and refine features faster.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))


                    ElevatedButton(
                        onClick = { uriHandler.openUri("https://github.com/SilentCoderHere") },
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = colorScheme.secondaryContainer,
                            contentColor = colorScheme.onSecondaryContainer
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .animateContentSize()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Rounded.Code,
                                contentDescription = "GitHub",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "View on GitHub", style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))


                    TextButton(
                        onClick = {
                            isVisible = false
                            scope.launch {
                                delay(300)
                                onDismiss()
                            }
                        }, modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Close", color = colorScheme.primary)
                    }
                }
            }
        }
    }
}