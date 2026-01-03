package com.foss.aihub.ui.screens.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.foss.aihub.models.LinkData
import com.foss.aihub.models.LinkType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MD3LinkOptionsDialog(
    linkData: LinkData,
    onDismiss: () -> Unit,
    onOpenLinkInExternalBrowser: (String) -> Unit,
    onCopyLink: () -> Unit,
    onShareLink: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss, properties = DialogProperties(
            usePlatformDefaultWidth = false, decorFitsSystemWindows = true
        )
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Link",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(top = 24.dp)
                            .align(Alignment.Center)
                    )

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 8.dp, end = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }


                Surface(
                    onClick = onCopyLink,
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.12f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = when (linkData.type) {
                                        LinkType.IMAGE -> Icons.Default.Image
                                        LinkType.EMAIL -> Icons.Default.Email
                                        LinkType.PHONE -> Icons.Default.Phone
                                        else -> Icons.Default.Link
                                    },
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = linkData.title,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = linkData.url,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                HorizontalDivider(
                    thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant
                )


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    LinkActionButton(
                        icon = Icons.AutoMirrored.Filled.OpenInNew,
                        label = "Open in browser",
                        description = "Open in external browser",
                        onClick = { onOpenLinkInExternalBrowser(linkData.url) })

                    LinkActionButton(
                        icon = Icons.Default.ContentCopy,
                        label = "Copy link",
                        description = "Copy to clipboard",
                        onClick = onCopyLink
                    )

                    LinkActionButton(
                        icon = Icons.Default.Share,
                        label = "Share link",
                        description = "Share via other apps",
                        onClick = onShareLink
                    )

                    if (linkData.type == LinkType.IMAGE) {
                        LinkActionButton(
                            icon = Icons.Default.Download,
                            label = "Download image",
                            description = "Save to device",
                            onClick = onShareLink
                        )
                    }
                }


                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun LinkActionButton(
    icon: ImageVector, label: String, description: String, onClick: () -> Unit
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = MaterialTheme.shapes.large,
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon, contentDescription = null, modifier = Modifier.size(22.dp)
            )

            Column(
                modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}