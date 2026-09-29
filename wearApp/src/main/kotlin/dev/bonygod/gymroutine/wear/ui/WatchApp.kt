package dev.bonygod.gymroutine.wear.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.ConfirmationDialogDefaults
import androidx.wear.compose.material3.FailureConfirmationDialog
import androidx.wear.compose.material3.SuccessConfirmationDialog
import androidx.wear.compose.material3.confirmationDialogCurvedText
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import dev.bonygod.gymroutine.wear.R
import dev.bonygod.gymroutine.wear.ui.interactions.WatchEffect
import dev.bonygod.gymroutine.wear.ui.screens.ExerciseScreen
import dev.bonygod.gymroutine.wear.ui.screens.RoutinesScreen
import dev.bonygod.gymroutine.wear.ui.screens.WorkoutScreen

private const val ROUTE_ROUTINES = "routines"
private const val ROUTE_WORKOUT = "workout"
private const val ROUTE_EXERCISE = "exercise/{index}"

@Composable
fun WatchApp(viewModel: WatchViewModel) {
    val state by viewModel.state.collectAsState()
    val navController = rememberSwipeDismissableNavController()

    var showSaved by remember { mutableStateOf(false) }
    var showSaveFailed by remember { mutableStateOf(false) }
    val savedText = stringResource(R.string.watch_saved)
    val saveFailedText = stringResource(R.string.watch_save_failed)
    val curvedTextStyle = ConfirmationDialogDefaults.curvedTextStyle

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is WatchEffect.ExerciseCompleted -> {
                    val route = navController.currentBackStackEntry?.destination?.route
                    if (route?.startsWith("exercise/") == true) {
                        navController.popBackStack()
                    }
                }
                WatchEffect.WorkoutSaved -> {
                    navController.navigate(ROUTE_ROUTINES) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                    showSaved = true
                }
                WatchEffect.SaveFailed -> {
                    showSaveFailed = true
                }
            }
        }
    }

    AppScaffold {
        SwipeDismissableNavHost(navController = navController, startDestination = ROUTE_ROUTINES) {
            composable(ROUTE_ROUTINES) {
                RoutinesScreen(
                    state = state,
                    onEvent = viewModel::onEvent,
                    onNavigateToWorkout = { navController.navigate(ROUTE_WORKOUT) },
                )
            }
            composable(ROUTE_WORKOUT) {
                WorkoutScreen(
                    state = state,
                    onEvent = viewModel::onEvent,
                    onNavigateToExercise = { index -> navController.navigate("exercise/$index") },
                    onNavigateToRoutines = {
                        navController.navigate(ROUTE_ROUTINES) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    },
                )
            }
            composable(ROUTE_EXERCISE) { backStackEntry ->
                val index = backStackEntry.arguments?.getString("index")?.toIntOrNull()
                if (index == null) {
                    LaunchedEffect(Unit) { navController.popBackStack() }
                } else {
                    ExerciseScreen(state = state, index = index, onEvent = viewModel::onEvent)
                }
            }
        }
    }

    SuccessConfirmationDialog(
        visible = showSaved,
        onDismissRequest = { showSaved = false },
        curvedText = { confirmationDialogCurvedText(savedText, curvedTextStyle) },
    )

    FailureConfirmationDialog(
        visible = showSaveFailed,
        onDismissRequest = { showSaveFailed = false },
        curvedText = { confirmationDialogCurvedText(saveFailedText, curvedTextStyle) },
    )
}
