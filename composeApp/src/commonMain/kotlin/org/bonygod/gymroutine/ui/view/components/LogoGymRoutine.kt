package org.bonygod.gymroutine.ui.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.gymroutine_icon
import org.jetbrains.compose.resources.painterResource

@Composable
fun LogoGymRoutine(size: Dp) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
    Image(
        painter = painterResource(Res.drawable.gymroutine_icon),
        contentDescription = "Logo",
        modifier = Modifier.size(size)
    )
}
}