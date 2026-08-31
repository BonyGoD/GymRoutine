package dev.bonygod.gymroutine.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Columna con etiqueta + [WheelPicker] + unidad, usada tanto en el onboarding
 * (`CompleteProfileScreen`) como en el editor de perfil (`ProfileScreen`) para
 * elegir edad, altura y peso con el mismo carrete.
 */
@Composable
fun ProfileWheelColumn(
    label: String,
    unit: String,
    values: List<Int>,
    selectedValue: Int,
    onValueChange: (Int) -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = label,
            color = colorScheme.onSurfaceVariant,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp,
            textAlign = TextAlign.Center,
        )
        WheelPicker(
            values = values,
            selectedValue = selectedValue,
            onValueChange = onValueChange,
            modifier = Modifier.width(80.dp),
        )
        Text(
            text = unit,
            color = colorScheme.onSurfaceVariant,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
        )
    }
}
