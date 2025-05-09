package org.bonygod.gymroutine.ui.view.home.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.bonygod.gymroutine.ui.theme.CustomLightGray
import org.bonygod.gymroutine.ui.view.home.composables.AddRoutineButton
import org.bonygod.gymroutine.ui.view.home.composables.ExerciceTexts
import org.bonygod.gymroutine.ui.view.home.composables.RoutineTypeText
import org.bonygod.gymroutine.ui.view.viewModels.UserProfileViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RoutinesScreen(
    modifier: Modifier,
    userProfileViewModel: UserProfileViewModel = koinViewModel(),
) {
    val userData by userProfileViewModel.userData.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxSize().background(CustomLightGray),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        items(userData?.routines ?: emptyList()) { routine ->
            RoutineTypeText(routine)
            routine.exercises.forEach { exercice ->
                ExerciceTexts(exercice)
                Spacer(modifier = Modifier.padding(5.dp))
            }
        }
    }
    AddRoutineButton(modifier) {
        TODO("navigate to add routine screen")
    }
}