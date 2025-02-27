package org.bonygod.gymroutine.ui.view.loginScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.gitlive.firebase.auth.FirebaseAuth
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.exclamation
import gymroutine.composeapp.generated.resources.forgot_password_button
import gymroutine.composeapp.generated.resources.forgot_password_error_message_dialog
import gymroutine.composeapp.generated.resources.loginOrSignup_dialog_subtitle
import gymroutine.composeapp.generated.resources.loginOrSignup_dialog_title
import gymroutine.composeapp.generated.resources.login_email
import gymroutine.composeapp.generated.resources.ok_icon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.bonygod.gymroutine.ui.view.components.CustomDialog
import org.bonygod.gymroutine.ui.view.components.CustomTextField
import org.bonygod.gymroutine.ui.view.components.LogoGymRoutine
import org.bonygod.gymroutine.ui.view.viewModels.DialogViewModel
import org.jetbrains.compose.resources.stringResource

@Composable
fun ForgotPassword(dialogViewModel: DialogViewModel, auth: FirebaseAuth, scope: CoroutineScope, onBack: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val titleDialogOK = stringResource(Res.string.loginOrSignup_dialog_title)
    val subtitleDialogOK = stringResource(Res.string.loginOrSignup_dialog_subtitle)
    val errorMessageDialog = stringResource(Res.string.forgot_password_error_message_dialog)
    val focusManager = LocalFocusManager.current

    if (showDialog) {
        CustomDialog(dialogViewModel, onDismiss = { showDialog = false })
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Yellow, Color.Black)
                )
            )
            .clickable {
                focusManager.clearFocus()
            },
        Arrangement.Top, Alignment.CenterHorizontally
    ) {
        LogoGymRoutine(size = 300.dp)

        CustomTextField(
            value = email,
            title = stringResource(Res.string.login_email),
            checkEmail = true,
            onValueChange = { email = it })

        Spacer(modifier = Modifier.padding(15.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .clip(shape = RoundedCornerShape(30.dp))
                .height(50.dp),
            onClick = {
                scope.launch {
                    try {
                        auth.sendPasswordResetEmail(email)
                        dialogViewModel.setCustomDialog(
                            titleDialogOK,
                            subtitleDialogOK,
                            Res.drawable.ok_icon,
                            Color.Green
                        )
                        onBack()
                    } catch (e: Exception) {
                        dialogViewModel.setCustomDialog(
                            errorMessageDialog,
                            null,
                            Res.drawable.exclamation,
                            Color.Red)
                        showDialog = true
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Yellow,
                contentColor = Color.Black
            )
        ) {
            Text(
                stringResource(Res.string.forgot_password_button),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}