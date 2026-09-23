package dev.bonygod.gymroutine.tutorial.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bonygod.gymroutine.tutorial.ui.interactions.TutorialEvent
import dev.bonygod.gymroutine.tutorial.ui.interactions.TutorialState
import dev.bonygod.gymroutine.tutorial.ui.model.TutorialStep
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.tutorial_next
import gymroutine.composeapp.generated.resources.tutorial_skip
import gymroutine.composeapp.generated.resources.tutorial_create_routine
import org.jetbrains.compose.resources.stringResource

@Composable
fun TutorialOverlay(state: TutorialState, onEvent: (TutorialEvent) -> Unit) {
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.25f))
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        awaitPointerEvent().changes.forEach { it.consume() }
                    }
                }
            },
        contentAlignment = Alignment.BottomCenter,
    ) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp, bottom = 96.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(colorScheme.surface)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Crossfade(targetState = state.step, label = "tutorial_step") { step ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(colorScheme.primary.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            step.icon,
                            contentDescription = null,
                            tint = colorScheme.primary,
                            modifier = Modifier.size(32.dp),
                        )
                    }
                    Spacer(Modifier.height(20.dp))
                    Text(
                        text = stringResource(step.title),
                        color = colorScheme.onSurface,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = stringResource(step.description),
                        color = colorScheme.onSurfaceVariant,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TutorialStep.entries.forEach { step ->
                    val isCurrent = step == state.step
                    Box(
                        modifier = Modifier
                            .height(8.dp)
                            .width(if (isCurrent) 24.dp else 8.dp)
                            .clip(CircleShape)
                            .background(
                                if (isCurrent) colorScheme.primary else colorScheme.outline.copy(alpha = 0.3f),
                            ),
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButton(onClick = { onEvent(TutorialEvent.OnSkip) }) {
                    Text(stringResource(Res.string.tutorial_skip))
                }
                Button(
                    onClick = { onEvent(TutorialEvent.OnNext) },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.primary,
                    ),
                ) {
                    val buttonText = if (state.isLastStep) {
                        Res.string.tutorial_create_routine
                    } else {
                        Res.string.tutorial_next
                    }
                    Text(
                        text = stringResource(buttonText),
                        color = colorScheme.onPrimary,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}
