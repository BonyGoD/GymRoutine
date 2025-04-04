package org.bonygod.gymroutine.ui.view.routine.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.bonygod.gymroutine.ui.theme.CustomBlack
import org.bonygod.gymroutine.ui.theme.CustomWhite
import org.bonygod.gymroutine.ui.theme.CustomYellow

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BodyExercise(exercise: String, sets: String) {
    Text(
        text = "Extension de triceps con polea",
        modifier = Modifier.padding(10.dp),
        color = CustomBlack,
        fontSize = 20.sp
    )
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Serie 1",
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp),
            color = CustomBlack
        )
    }
}

@Composable
fun ExerciceTexts() {
    val maxLength = 21
    val text = "Extension de triceps con polea"
    val truncatedText = text.take(maxLength) + "..."
    Row {
        Text(text = truncatedText, modifier = Modifier.weight(1f), maxLines = 1, color = CustomWhite)
        Text(text = "4 S", modifier = Modifier.width(40.dp), color = CustomWhite)
        Text(text = "10 R", modifier = Modifier.width(40.dp), color = CustomWhite)
        Text(text = "60 Seg", modifier = Modifier.width(60.dp), color = CustomWhite)
    }
}