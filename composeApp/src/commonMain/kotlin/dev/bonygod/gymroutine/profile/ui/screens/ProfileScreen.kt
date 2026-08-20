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
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import dev.bonygod.gymroutine.core.ui.components.ProfileWheelColumn
import dev.bonygod.gymroutine.profile.ui.ProfileViewModel
import dev.bonygod.gymroutine.profile.ui.interactions.ProfileEffect
import dev.bonygod.gymroutine.profile.ui.interactions.ProfileEvent
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.common_edit_description
import gymroutine.composeapp.generated.resources.profile_screen_default_user_name
import gymroutine.composeapp.generated.resources.profile_screen_edit_data_title
import gymroutine.composeapp.generated.resources.profile_screen_field_age
import gymroutine.composeapp.generated.resources.profile_screen_field_height
import gymroutine.composeapp.generated.resources.profile_screen_field_weight
import gymroutine.composeapp.generated.resources.profile_screen_logout
import gymroutine.composeapp.generated.resources.profile_screen_personal_data_title
import gymroutine.composeapp.generated.resources.profile_screen_personal_records
import gymroutine.composeapp.generated.resources.profile_screen_save_button
import gymroutine.composeapp.generated.resources.profile_screen_streak_days
import gymroutine.composeapp.generated.resources.profile_screen_total_workouts
import gymroutine.composeapp.generated.resources.profile_screen_training_streak
import gymroutine.composeapp.generated.resources.profile_screen_unit_cm
import gymroutine.composeapp.generated.resources.profile_screen_unit_kg
import gymroutine.composeapp.generated.resources.profile_screen_unit_years
import gymroutine.composeapp.generated.resources.profile_screen_value_placeholder
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

// Mismos rangos que el onboarding (CompleteProfileScreen): edad 18-100, altura 80-300, peso 30-300.
private const val MIN_AGE = 18
private const val MAX_AGE = 100
private const val MIN_HEIGHT = 80
private const val MAX_HEIGHT = 300
private const val MIN_WEIGHT = 30
private const val MAX_WEIGHT = 300

/**
 * El bloque de diagnóstico solo se dibuja para esta cuenta, así que puede quedarse en el código y
 * viajar en las builds de release sin que ningún tester llegue a verlo.
 */
private const val DEVELOPER_EMAIL = "bonygod.dev@gmail.com"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    vmKey: String = "",
    viewModel: ProfileViewModel = koinViewModel(key = vmKey.ifBlank { null }),
) {
    val state by viewModel.state.collectAsState()
    val colorScheme = MaterialTheme.colorScheme
    val snackbarHostState = remember { SnackbarHostState() }
    val editSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

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
    val personalDataTitle = stringResource(Res.string.profile_screen_personal_data_title)
    val ageLabel = stringResource(Res.string.profile_screen_field_age)
    val heightLabel = stringResource(Res.string.profile_screen_field_height)
    val weightLabel = stringResource(Res.string.profile_screen_field_weight)
    val yearsUnit = stringResource(Res.string.profile_screen_unit_years)
    val cmUnit = stringResource(Res.string.profile_screen_unit_cm)
    val kgUnit = stringResource(Res.string.profile_screen_unit_kg)
    val valuePlaceholder = stringResource(Res.string.profile_screen_value_placeholder)
    val ageValue = if (state.age.isBlank()) valuePlaceholder else "${state.age} $yearsUnit"
    val heightValue = if (state.height.isBlank()) valuePlaceholder else "${state.height} $cmUnit"
    val weightValue = if (state.weight.isBlank()) valuePlaceholder else "${state.weight} $kgUnit"

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
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState, enabled = scrollState.maxValue > 0)
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

                Spacer(Modifier.height(32.dp))

                // ── Datos personales ─────────────────────────────────────────
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = personalDataTitle,
                            color = colorScheme.onSurface,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.32).sp,
                        )
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(colorScheme.surfaceVariant)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                ) { viewModel.onEvent(ProfileEvent.OnEditProfileData) },
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = stringResource(Res.string.common_edit_description),
                                tint = colorScheme.primary,
                                modifier = Modifier.size(16.dp),
                            )
                        }
                    }
                    StatRow(
                        icon = Icons.Default.Cake,
                        iconTint = colorScheme.primary,
                        label = ageLabel,
                        value = ageValue,
                    )
                    StatRow(
                        icon = Icons.Default.Height,
                        iconTint = colorScheme.primary,
                        label = heightLabel,
                        value = heightValue,
                    )
                    StatRow(
                        icon = Icons.Default.MonitorWeight,
                        iconTint = colorScheme.primary,
                        label = weightLabel,
                        value = weightValue,
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

                // ── Diagnóstico (solo cuenta de desarrollador) ────────────────
                if (state.userEmail == DEVELOPER_EMAIL) {
                    Spacer(Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CircleShape)
                            .background(colorScheme.surfaceVariant)
                            .border(1.dp, colorScheme.outline.copy(alpha = 0.2f), CircleShape)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                            ) {
                                // Se lanza sin capturar a propósito: tiene que llegar al handler de
                                // excepciones no capturadas para que Crashlytics lo registre como
                                // fatal. El reporte se sube al siguiente arranque, no al instante.
                                throw RuntimeException("Test Crash - verificación de Crashlytics")
                            }
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            Icon(
                                Icons.Default.BugReport,
                                contentDescription = null,
                                tint = colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp),
                            )
                            // Sin stringResource: no lo ve ningún usuario, y así no hay que
                            // arrastrar la cadena a values/, values-es/ y values-ca/.
                            Text(
                                "Forzar crash de prueba",
                                color = colorScheme.onSurfaceVariant,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }

                Spacer(Modifier.height(88.dp))
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

    if (state.isEditingProfileData) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.onEvent(ProfileEvent.OnDismissEditProfileData) },
            sheetState = editSheetState,
            containerColor = colorScheme.surface,
        ) {
            EditProfileDataSheet(
                age = state.editingAge,
                height = state.editingHeight,
                weight = state.editingWeight,
                isSaving = state.isSavingProfileData,
                onAgeChange = { viewModel.onEvent(ProfileEvent.OnAgeChange(it)) },
                onHeightChange = { viewModel.onEvent(ProfileEvent.OnHeightChange(it)) },
                onWeightChange = { viewModel.onEvent(ProfileEvent.OnWeightChange(it)) },
                onSaveClick = { viewModel.onEvent(ProfileEvent.OnSaveProfileData) },
            )
        }
    }
}

@Composable
private fun EditProfileDataSheet(
    age: Int,
    height: Int,
    weight: Int,
    isSaving: Boolean,
    onAgeChange: (Int) -> Unit,
    onHeightChange: (Int) -> Unit,
    onWeightChange: (Int) -> Unit,
    onSaveClick: () -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme
    val ageValues = remember { (MIN_AGE..MAX_AGE).toList() }
    val heightValues = remember { (MIN_HEIGHT..MAX_HEIGHT).toList() }
    val weightValues = remember { (MIN_WEIGHT..MAX_WEIGHT).toList() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(Res.string.profile_screen_edit_data_title),
            color = colorScheme.onSurface,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.4).sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            ProfileWheelColumn(
                label = stringResource(Res.string.profile_screen_field_age),
                unit = stringResource(Res.string.profile_screen_unit_years),
                values = ageValues,
                selectedValue = age,
                onValueChange = onAgeChange,
            )
            ProfileWheelColumn(
                label = stringResource(Res.string.profile_screen_field_height),
                unit = stringResource(Res.string.profile_screen_unit_cm),
                values = heightValues,
                selectedValue = height,
                onValueChange = onHeightChange,
            )
            ProfileWheelColumn(
                label = stringResource(Res.string.profile_screen_field_weight),
                unit = stringResource(Res.string.profile_screen_unit_kg),
                values = weightValues,
                selectedValue = weight,
                onValueChange = onWeightChange,
            )
        }

        Spacer(Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(CircleShape)
                .background(if (isSaving) colorScheme.surfaceVariant else colorScheme.primary)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    enabled = !isSaving,
                    onClick = onSaveClick,
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (isSaving) {
                CircularProgressIndicator(color = colorScheme.primary)
            } else {
                Text(
                    text = stringResource(Res.string.profile_screen_save_button),
                    color = colorScheme.onPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
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
