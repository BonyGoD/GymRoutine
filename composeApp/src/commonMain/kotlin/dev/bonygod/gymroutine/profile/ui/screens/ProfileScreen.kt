package dev.bonygod.gymroutine.profile.ui.screens

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bonygod.gymroutine.core.theme.GoldIcon
import dev.bonygod.gymroutine.core.theme.OrangeIcon
import dev.bonygod.gymroutine.profile.ui.ProfileViewModel
import dev.bonygod.gymroutine.profile.ui.interactions.ProfileEffect
import dev.bonygod.gymroutine.profile.ui.interactions.ProfileEvent
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.profile_screen_default_user_name
import gymroutine.composeapp.generated.resources.profile_screen_logout
import gymroutine.composeapp.generated.resources.profile_screen_personal_records
import gymroutine.composeapp.generated.resources.profile_screen_streak_days
import gymroutine.composeapp.generated.resources.profile_screen_total_workouts
import gymroutine.composeapp.generated.resources.profile_screen_training_streak
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    vmKey: String = "",
    viewModel: ProfileViewModel = koinViewModel(key = vmKey.ifBlank { null }),
) {
    val state by viewModel.state.collectAsState()
    val colorScheme = MaterialTheme.colorScheme
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ProfileEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    val defaultUserName = stringResource(Res.string.profile_screen_default_user_name)
    val totalWorkoutsLabel = stringResource(Res.string.profile_screen_total_workouts)
    val personalRecordsLabel = stringResource(Res.string.profile_screen_personal_records)
    val trainingStreakLabel = stringResource(Res.string.profile_screen_training_streak)
    val streakDaysText = stringResource(Res.string.profile_screen_streak_days, state.streak)
    val logoutText = stringResource(Res.string.profile_screen_logout)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background),
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = colorScheme.primary,
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(96.dp))

                // ── Avatar ────────────────────────────────────────────────────
                Box(
                    modifier = Modifier
                        .size(84.dp)
                        .clip(CircleShape)
                        .background(colorScheme.surfaceVariant)
                        .border(1.dp, colorScheme.outline.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    if (state.userName.isNotBlank()) {
                        Text(
                            text = state.userName.first().uppercase(),
                            color = colorScheme.primary,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    } else {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = colorScheme.primary,
                            modifier = Modifier.size(40.dp),
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // ── Nombre y email ────────────────────────────────────────────
                Text(
                    text = state.userName.ifBlank { defaultUserName },
                    color = colorScheme.onSurface,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.48).sp,
                )
                if (state.userEmail.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = state.userEmail,
                        color = colorScheme.onSurfaceVariant,
                        fontSize = 14.sp,
                    )
                }

                Spacer(Modifier.height(32.dp))

                // ── Stats ─────────────────────────────────────────────────────
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    StatRow(
                        icon = Icons.Default.FitnessCenter,
                        iconTint = colorScheme.primary,
                        label = totalWorkoutsLabel,
                        value = state.totalWorkouts.toString(),
                    )
                    StatRow(
                        icon = Icons.Default.Star,
                        iconTint = GoldIcon,
                        label = personalRecordsLabel,
                        value = state.personalRecords.toString(),
                    )
                    StatRow(
                        icon = Icons.Default.LocalFireDepartment,
                        iconTint = OrangeIcon,
                        label = trainingStreakLabel,
                        value = streakDaysText,
                    )
                }

                Spacer(Modifier.height(40.dp))

                // ── Cerrar sesión ─────────────────────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CircleShape)
                        .background(colorScheme.surfaceVariant)
                        .border(1.dp, colorScheme.outline.copy(alpha = 0.2f), CircleShape)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) { viewModel.onEvent(ProfileEvent.OnLogout) }
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Icon(
                            Icons.Default.Logout,
                            contentDescription = null,
                            tint = Color(0xFFEF5350),
                            modifier = Modifier.size(18.dp),
                        )
                        Text(
                            logoutText,
                            color = Color(0xFFEF5350),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }

                Spacer(Modifier.height(120.dp))
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
        ) { data ->
            Snackbar(
                snackbarData = data,
                containerColor = colorScheme.surfaceVariant,
                contentColor = colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun StatRow(
    icon: ImageVector,
    iconTint: Color,
    label: String,
    value: String,
) {
    val colorScheme = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(colorScheme.surface)
            .border(1.dp, colorScheme.outline.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(iconTint.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
        }
        Text(
            text = label,
            color = colorScheme.onSurfaceVariant,
            fontSize = 15.sp,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = value,
            color = colorScheme.onSurface,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.36).sp,
        )
    }
}
