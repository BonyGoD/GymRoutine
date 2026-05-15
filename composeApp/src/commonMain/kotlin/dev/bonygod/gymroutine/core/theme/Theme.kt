package dev.bonygod.gymroutine.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Primary = Color(0xFFE53935)
val OnPrimary = Color(0xFFFFFFFF)
val PrimaryContainer = Color(0xFF7F0000)
val Background = Color(0xFF121212)
val Surface = Color(0xFF1E1E1E)
val SurfaceVariant = Color(0xFF2C2C2C)
val OnBackground = Color(0xFFE0E0E0)
val OnSurface = Color(0xFFE0E0E0)
val OnSurfaceVariant = Color(0xFFAAAAAA)

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    background = Background,
    surface = Surface,
    surfaceVariant = SurfaceVariant,
    onBackground = OnBackground,
    onSurface = OnSurface,
    onSurfaceVariant = OnSurfaceVariant,
)

@Composable
fun GymRoutineTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
