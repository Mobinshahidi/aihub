package com.foss.aihub.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.foss.aihub.models.AiService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiHubAppBar(
    selectedService: AiService,
    onMenuClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp),
                clip = false
            ),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        shape = RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            IconButton(
                onClick = onMenuClick, modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = "Open drawer",
                    tint = MaterialTheme.colorScheme.primary
                )
            }


            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AnimatedContent(
                    targetState = selectedService, transitionSpec = {
                        (fadeIn() + slideInHorizontally { -it }).togetherWith(fadeOut() + slideOutHorizontally { it })
                    }, label = "serviceTitle"
                ) { service ->
                    Text(
                        text = service.name,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                AnimatedContent(
                    targetState = selectedService,
                    transitionSpec = { fadeIn().togetherWith(fadeOut()) },
                    label = "serviceCategory"
                ) { service ->
                    Text(
                        text = service.category,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }


            IconButton(
                onClick = onSettingsClick, modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = "Settings",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Md3TopAppBar(
    title: String,
    onBack: (() -> Unit)? = null,
    scrollBehavior: TopAppBarScrollBehavior? = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
) {
    TopAppBar(
        title = {
        Text(
            text = title, style = MaterialTheme.typography.titleLarge
        )
    }, navigationIcon = {
        if (onBack != null) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
    }, colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.surface,
        scrolledContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
        actionIconContentColor = MaterialTheme.colorScheme.onSurface
    ), scrollBehavior = scrollBehavior
    )
}