package dev.bonygod.gymroutine.onboarding.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bonygod.gymroutine.core.ui.components.ProfileWheelColumn
import dev.bonygod.gymroutine.onboarding.ui.CompleteProfileViewModel
import dev.bonygod.gymroutine.onboarding.ui.interactions.CompleteProfileEffect
import dev.bonygod.gymroutine.onboarding.ui.interactions.CompleteProfileEvent
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.complete_profile_button_save
import gymroutine.composeapp.generated.resources.complete_profile_field_age
import gymroutine.composeapp.generated.resources.complete_profile_field_height
import gymroutine.composeapp.generated.resources.complete_profile_field_weight
import gymroutine.composeapp.generated.resources.complete_profile_screen_subtitle
import gymroutine.composeapp.generated.resources.complete_profile_screen_title
import gymroutine.composeapp.generated.resources.complete_profile_unit_cm
import gymroutine.composeapp.generated.resources.complete_profile_unit_kg
import gymroutine.composeapp.generated.resources.complete_profile_unit_years
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

private const val MIN_AGE = 18
private const val MAX_AGE = 100
private const val MIN_HEIGHT = 80
private const val MAX_HEIGHT = 300
private const val MIN_WEIGHT = 0
private const val MAX_WEIGHT = 300

@Composable
fun CompleteProfileScreen(
    userId: String,
    viewModel: CompleteProfileViewModel = koinViewModel(),
) {
    val colorScheme = MaterialTheme.colorScheme
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(userId) {
        viewModel.onEvent(CompleteProfileEvent.OnInit(userId))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is CompleteProfileEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    val ageValues = remember { (MIN_AGE..MAX_AGE).toList() }
    val heightValues = remember { (MIN_HEIGHT..MAX_HEIGHT).toList() }
    val weightValues = remember { (MIN_WEIGHT..MAX_WEIGHT).toList() }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background)
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 56.dp, bottom = 32.dp),
            ) {
                Text(
                    text = stringResource(Res.string.complete_profile_screen_title),
                    color = colorScheme.primary,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(Res.string.complete_profile_screen_subtitle),
                    color = colorScheme.onSurfaceVariant,
                    fontSize = 14.sp,
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(colorScheme.surface)
                    .border(1.dp, colorScheme.outline.copy(alpha = 0.15f), RoundedCornerShape(28.dp))
                    .padding(vertical = 24.dp, horizontal = 12.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    ProfileWheelColumn(
                        label = stringResource(Res.string.complete_profile_field_age),
                        unit = stringResource(Res.string.complete_profile_unit_years),
                        values = ageValues,
                        selectedValue = state.age,
                        onValueChange = { viewModel.onEvent(CompleteProfileEvent.OnAgeChange(it)) },
                    )
                    ProfileWheelColumn(
                        label = stringResource(Res.string.complete_profile_field_height),
                        unit = stringResource(Res.string.complete_profile_unit_cm),
                        values = heightValues,
                        selectedValue = state.height,
                        onValueChange = { viewModel.onEvent(CompleteProfileEvent.OnHeightChange(it)) },
                    )
                    ProfileWheelColumn(
                        label = stringResource(Res.string.complete_profile_field_weight),
                        unit = stringResource(Res.string.complete_profile_unit_kg),
                        values = weightValues,
                        selectedValue = state.weight,
                        onValueChange = { viewModel.onEvent(CompleteProfileEvent.OnWeightChange(it)) },
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
                    .height(64.dp)
                    .clip(CircleShape)
                    .background(if (state.isSaving) colorScheme.surfaceVariant else colorScheme.primary)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        enabled = !state.isSaving,
                    ) { viewModel.onEvent(CompleteProfileEvent.OnSaveClick) },
                contentAlignment = Alignment.Center,
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(color = colorScheme.primary)
                } else {
                    Text(
                        text = stringResource(Res.string.complete_profile_button_save),
                        color = colorScheme.onPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}
