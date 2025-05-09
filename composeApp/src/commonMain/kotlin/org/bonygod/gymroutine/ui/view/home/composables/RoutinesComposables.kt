package org.bonygod.gymroutine.ui.view.home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.bonygod.gymroutine.data.model.Exercice
import org.bonygod.gymroutine.data.model.Routine
import org.bonygod.gymroutine.ui.theme.CustomBlack
import org.bonygod.gymroutine.ui.theme.CustomGray
import org.bonygod.gymroutine.ui.theme.CustomWhite
import org.bonygod.gymroutine.ui.theme.CustomYellow

@Composable
fun RoutineTypeText(routine: Routine) {
    Text(
        text = routine.type,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp),
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun ExerciceTexts(exercice: Exercice) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp)
            .clip(CircleShape)
            .background(CustomGray)
            .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = exercice.name.truncate(),
            modifier = Modifier.weight(1f),
            maxLines = 1,
            color = CustomWhite
        )
        Text(
            text = exercice.sets.toString() + " S",
            modifier = Modifier.width(40.dp),
            color = CustomWhite
        )
        Text(
            text = exercice.repetitions.toString() + " R",
            modifier = Modifier.width(40.dp),
            color = CustomWhite
        )
        Text(
            text = exercice.rest.toString() + " Seg",
            modifier = Modifier.width(60.dp),
            color = CustomWhite
        )
    }
}

@Composable
fun AddRoutineButton(modifier: Modifier, onButtonClick: () -> Unit) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        FloatingActionButton(
            onClick = { onButtonClick() },
            modifier = Modifier.padding(16.dp),
            containerColor = CustomYellow,
            contentColor = CustomBlack,
            elevation = FloatingActionButtonDefaults.elevation(8.dp)
        ) {
            Icon(Icons.Rounded.Add, contentDescription = "Add")
        }
    }
}

private fun String.truncate(): String {
    val maxLength = 21
    return if (this.length > maxLength) {
        this.take(maxLength) + "..."
    } else {
        this
    }
}