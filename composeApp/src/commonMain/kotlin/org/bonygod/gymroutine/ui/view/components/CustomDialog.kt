package org.bonygod.gymroutine.ui.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.custom_dialog_button_text
import org.bonygod.gymroutine.ui.theme.CustomBlack
import org.bonygod.gymroutine.ui.theme.CustomGray
import org.bonygod.gymroutine.ui.view.viewModels.DialogViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CustomDialog(dialogViewModel: DialogViewModel, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(
                modifier = Modifier.fillMaxWidth().height(250.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(dialogViewModel.icon.value),
                    contentDescription = "Error",
                    tint = dialogViewModel.iconColor.value,
                    modifier = Modifier.size(70.dp)
                )
                Spacer(modifier = Modifier.weight(0.3f))
                Text(
                    text = dialogViewModel.customDialogTitle.value,
                    fontSize = 16.sp,
                    color = CustomBlack,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.weight(0.3f))
                Text(
                    text = dialogViewModel.customDialogSubtitle.value,
                    fontSize = 15.sp,
                    color = CustomGray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.weight(0.7f))
                Button(
                    onClick = {
                        onDismiss()
                    },
                    modifier = Modifier.padding(bottom = 20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                ) {
                    Text(text = stringResource(Res.string.custom_dialog_button_text))
                }
            }
        }
    }
}