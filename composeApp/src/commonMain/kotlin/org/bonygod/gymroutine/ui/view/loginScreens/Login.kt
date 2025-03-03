package org.bonygod.gymroutine.ui.view.loginScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.custom_dialog_title
import gymroutine.composeapp.generated.resources.exclamation
import gymroutine.composeapp.generated.resources.login_button_text_login
import gymroutine.composeapp.generated.resources.login_email
import gymroutine.composeapp.generated.resources.login_forgot_password
import gymroutine.composeapp.generated.resources.login_password
import gymroutine.composeapp.generated.resources.login_spacer_login_google
import org.bonygod.gymroutine.ui.view.components.CustomDialog
import org.bonygod.gymroutine.ui.view.components.CustomPasswordTextField
import org.bonygod.gymroutine.ui.view.components.CustomTextField
import org.bonygod.gymroutine.ui.view.components.LogoGymRoutine
import org.bonygod.gymroutine.ui.view.viewModels.LoginViewModel
import org.bonygod.gymroutine.view.GoogleSignin
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun Login(
    loginViewModel: LoginViewModel = koinViewModel(),
    navigateToPrimeraPantalla: (String) -> Unit,
    navigateToForgotScreen: () -> Unit
) {

    val dialogViewModel by loginViewModel.dialogViewModel.collectAsState()
    val passwordVisible by loginViewModel.passwordVisible.collectAsState()
    val email by loginViewModel.email.collectAsState()
    val password by loginViewModel.password.collectAsState()
    val showDialog by dialogViewModel.showDialog.collectAsState()

    dialogViewModel.setCustomDialog(
        customDialogTitle = stringResource(Res.string.custom_dialog_title),
        customDialogSubtitle = "",
        icon = Res.drawable.exclamation,
        iconColor = Color.Red
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
            checkEmail = false,
            onValueChange = { email ->
                loginViewModel.onEmailChange(email)
            })

        Spacer(modifier = Modifier.padding(5.dp))

        CustomPasswordTextField(
            password = password,
            passwordVisible = passwordVisible,
            title = stringResource(Res.string.login_password),
            color = Color.Black,
            onPasswordChange = { pass ->
                loginViewModel.onPasswordChange(pass)
            },
            onPasswordVisibleChange = { passVisible ->
                loginViewModel.onPasswordVisibleChange(passVisible)
            },
        )

        Text(
            stringResource(Res.string.login_forgot_password),
            modifier = Modifier
                .align(Alignment.End)
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .clickable { loginViewModel.onForgotPasswordClick(navigateToForgotScreen) },
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.padding(15.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .clip(shape = RoundedCornerShape(30.dp))
                .height(50.dp),
            onClick = {
                try {
                    loginViewModel.signIn(navigateToPrimeraPantalla)
                }catch (e: Exception){
                    dialogViewModel.onShowDialogChange(true)
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Yellow,
                contentColor = Color.Black
            )
        ) {
            Text(
                stringResource(Res.string.login_button_text_login),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f).height(1.dp).background(Color.White))
            Text(
                stringResource(Res.string.login_spacer_login_google),
                modifier = Modifier.padding(horizontal = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.weight(1f).height(1.dp).background(Color.White))
        }

        GoogleSignin(navigateToPrimeraPantalla)
    }
}