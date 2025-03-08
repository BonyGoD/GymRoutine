package org.bonygod.gymroutine.ui.view.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.bonygod.gymroutine.ui.theme.CustomGray
import org.bonygod.gymroutine.ui.theme.CustomWhite
import org.bonygod.gymroutine.ui.theme.CustomYellow

@Composable
fun CustomSelectableButton(inisitalValue: Boolean, text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected && !inisitalValue) CustomYellow else CustomGray
        ),
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected && !inisitalValue) CustomGray else CustomWhite,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}