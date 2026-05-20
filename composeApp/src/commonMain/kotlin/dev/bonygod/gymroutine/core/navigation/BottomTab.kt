package dev.bonygod.gymroutine.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomTab(val label: String, val icon: ImageVector) {
    Home("Inicio", Icons.Default.Home),
    Routines("Rutinas", Icons.AutoMirrored.Filled.ViewList),
    History("Historial", Icons.Default.History),
    Profile("Perfil", Icons.Default.Person),
}
