package org.bonygod.gymroutine.ui.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.bonygod.gymroutine.ui.theme.CustomBlack
import org.bonygod.gymroutine.ui.theme.CustomYellow

@Composable
fun LoadingScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize().background(CustomBlack)
    ) {
        CircularProgressIndicator(color = CustomYellow)
    }
}