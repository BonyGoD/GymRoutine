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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
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
import org.bonygod.gymroutine.ui.view.viewModels.ForgotPasswordViewModel
import org.jetbrains.compose.resources.stringResource

@Composable
fun ForgotPassword(
    auth: FirebaseAuth,
    dialogViewModel: DialogViewModel,
    forgotPasswordViewModel: ForgotPasswordViewModel = viewModel(),
    onBack: () -> Unit
) {

    val email by forgotPasswordViewModel.email.collectAsState()
    val showDialog by dialogViewModel.showDialog.collectAsState()

    dialogViewModel.setErrorMessageDialog(
        stringResource(Res.string.forgot_password_error_message_dialog)
    )

    forgotPasswordViewModel.setTitlesOkDialog(
        stringResource(Res.string.loginOrSignup_dialog_title),
        stringResource(Res.string.loginOrSignup_dialog_subtitle)
    )

    val focusManager = LocalFocusManager.current

    if (showDialog) {
        CustomDialog(dialogViewModel, onDismiss = { dialogViewModel.onShowDialogChange(false) })
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
            onValueChange = { email ->
                forgotPasswordViewModel.onEmailChange(email)
            })

        Spacer(modifier = Modifier.padding(15.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .clip(shape = RoundedCornerShape(30.dp))
                .height(50.dp),
            onClick = {
                forgotPasswordViewModel.resetEmail(auth, dialogViewModel, onBack)
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