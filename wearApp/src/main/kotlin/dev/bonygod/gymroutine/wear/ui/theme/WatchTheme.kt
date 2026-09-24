package dev.bonygod.gymroutine.wear.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material3.ColorScheme
import androidx.wear.compose.material3.MaterialTheme

private val WatchColorScheme = ColorScheme(
    primary = Color(0xFFE53935),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF7F1D1D),
    onPrimaryContainer = Color.White,
)

@Composable
fun WatchTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = WatchColorScheme, content = content)
}
