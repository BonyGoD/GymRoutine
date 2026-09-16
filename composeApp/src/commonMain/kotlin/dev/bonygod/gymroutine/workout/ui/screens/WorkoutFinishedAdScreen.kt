package dev.bonygod.gymroutine.workout.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bonygod.admob.kmp.ui.InterstitialAdScreen
import dev.bonygod.gymroutine.core.navigation.Navigator
import org.koin.compose.koinInject

@Composable
fun WorkoutFinishedAdScreen(navigator: Navigator = koinInject()) {
    InterstitialAdScreen(
        onFinished = { navigator.goBack() },
        loading = { WorkoutFinishedAdLoading() },
    )
}

@Composable
private fun WorkoutFinishedAdLoading() {
    val colorScheme = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = colorScheme.primary)
    }
}
