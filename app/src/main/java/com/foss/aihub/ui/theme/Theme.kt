package com.foss.aihub.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun AiHubTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }

        darkTheme -> darkColorScheme(
            primary = Color(0xFFD0BCFF),
            secondary = Color(0xFFCCC2DC),
            tertiary = Color(0xFFEFB8C8),
            surface = Color(0xFF1C1B1F),
            surfaceVariant = Color(0xFF49454F),
            background = Color(0xFF121212),
            onPrimary = Color(0xFF381E72),
            onSecondary = Color(0xFF332D41),
            onTertiary = Color(0xFF492532),
            onBackground = Color(0xFFE6E1E5),
            onSurface = Color(0xFFE6E1E5),
        )

        else -> lightColorScheme(
            primary = Color(0xFF6750A4),
            secondary = Color(0xFF625B71),
            tertiary = Color(0xFF7D5260),
            surface = Color(0xFFFFFBFE),
            surfaceVariant = Color(0xFFE7E0EC),
            background = Color(0xFFFFFBFE),
            onPrimary = Color.White,
            onSecondary = Color.White,
            onTertiary = Color.White,
            onBackground = Color(0xFF1C1B1F),
            onSurface = Color(0xFF1C1B1F),
        )
    }

    MaterialTheme(
        colorScheme = colorScheme, typography = Typography(
            displayLarge = TextStyle(
                fontWeight = FontWeight.Light,
                fontSize = 57.sp,
                lineHeight = 64.sp,
                letterSpacing = (-0.25).sp
            ),
            titleLarge = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.sp
            ),
            bodyLarge = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp
            ),
        ), shapes = MaterialTheme.shapes.copy(
            small = RoundedCornerShape(8.dp),
            medium = RoundedCornerShape(16.dp),
            large = RoundedCornerShape(24.dp),
            extraLarge = RoundedCornerShape(32.dp)
        ), content = content
    )
}