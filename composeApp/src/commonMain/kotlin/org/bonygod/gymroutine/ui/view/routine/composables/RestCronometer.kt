package org.bonygod.gymroutine.ui.view.routine.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.bonygod.gymroutine.ui.theme.CustomBlack
import org.bonygod.gymroutine.ui.theme.CustomYellow

@Composable
fun RestCronometer(
    isExpanded: Boolean,
    restSecs: Int,
) {
    var count by remember { mutableStateOf(restSecs) }
    var isTimerActive by remember { mutableStateOf(false) }

    LaunchedEffect(isTimerActive) {
        if (isTimerActive) {
            while (count > 0) {
                delay(1000)
                count--
            }
        }
    }
    AnimatedVisibility(
        visible = isExpanded,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        Column(
            horizontalAlignment = CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .padding(vertical = 20.dp, horizontal = 20.dp)
                    .height(150.dp)
                    .width(150.dp)
                    .clip(CircleShape)
                    .background(CustomBlack)
                    .clickable {
                        isTimerActive = true
                    },
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "$count seg",
                        fontSize = 30.sp,
                        color = CustomYellow,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                    Icon(
                        Icons.Rounded.PlayArrow,
                        contentDescription = "Play",
                        tint = CustomBlack,
                        modifier = Modifier
                            .size(35.dp)
                            .clip(CircleShape)
                            .background(CustomYellow)
                    )
                }
            }
            Text(
                modifier = Modifier.padding(bottom = 20.dp),
                text = "Descanso",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = CustomBlack,
            )
        }

    }
}
