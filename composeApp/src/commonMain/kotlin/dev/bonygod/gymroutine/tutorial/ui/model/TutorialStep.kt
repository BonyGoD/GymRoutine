package dev.bonygod.gymroutine.tutorial.ui.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timer
import androidx.compose.ui.graphics.vector.ImageVector
import dev.bonygod.gymroutine.core.navigation.BottomTab
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.tutorial_evolution_description
import gymroutine.composeapp.generated.resources.tutorial_evolution_title
import gymroutine.composeapp.generated.resources.tutorial_history_description
import gymroutine.composeapp.generated.resources.tutorial_history_title
import gymroutine.composeapp.generated.resources.tutorial_home_description
import gymroutine.composeapp.generated.resources.tutorial_home_title
import gymroutine.composeapp.generated.resources.tutorial_profile_description
import gymroutine.composeapp.generated.resources.tutorial_profile_title
import gymroutine.composeapp.generated.resources.tutorial_routines_description
import gymroutine.composeapp.generated.resources.tutorial_routines_title
import gymroutine.composeapp.generated.resources.tutorial_welcome_description
import gymroutine.composeapp.generated.resources.tutorial_welcome_title
import gymroutine.composeapp.generated.resources.tutorial_workout_description
import gymroutine.composeapp.generated.resources.tutorial_workout_title
import org.jetbrains.compose.resources.StringResource

enum class TutorialStep(
    val tab: BottomTab,
    val icon: ImageVector,
    val title: StringResource,
    val description: StringResource,
) {
    Welcome(
        BottomTab.Home,
        Icons.Default.FitnessCenter,
        Res.string.tutorial_welcome_title,
        Res.string.tutorial_welcome_description,
    ),
    Home(
        BottomTab.Home,
        Icons.Default.Home,
        Res.string.tutorial_home_title,
        Res.string.tutorial_home_description,
    ),
    Routines(
        BottomTab.Routines,
        Icons.AutoMirrored.Filled.ViewList,
        Res.string.tutorial_routines_title,
        Res.string.tutorial_routines_description,
    ),
    Workout(
        BottomTab.Home,
        Icons.Default.Timer,
        Res.string.tutorial_workout_title,
        Res.string.tutorial_workout_description,
    ),
    History(
        BottomTab.History,
        Icons.Default.History,
        Res.string.tutorial_history_title,
        Res.string.tutorial_history_description,
    ),
    Evolution(
        BottomTab.Evolution,
        Icons.AutoMirrored.Filled.TrendingUp,
        Res.string.tutorial_evolution_title,
        Res.string.tutorial_evolution_description,
    ),
    Profile(
        BottomTab.Profile,
        Icons.Default.Person,
        Res.string.tutorial_profile_title,
        Res.string.tutorial_profile_description,
    ),
}
