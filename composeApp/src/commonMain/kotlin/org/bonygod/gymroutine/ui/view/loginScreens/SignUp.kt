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
import gymroutine.composeapp.generated.resources.error_email_exist_subtitle
import gymroutine.composeapp.generated.resources.error_email_exist_title
import gymroutine.composeapp.generated.resources.loginOrSignup_dialog_subtitle
import gymroutine.composeapp.generated.resources.loginOrSignup_dialog_title
import gymroutine.composeapp.generated.resources.login_spacer_login_google
import gymroutine.composeapp.generated.resources.register_button_signup
import gymroutine.composeapp.generated.resources.register_email
import gymroutine.composeapp.generated.resources.register_password
import gymroutine.composeapp.generated.resources.register_repeat_password
import gymroutine.composeapp.generated.resources.register_user
import org.bonygod.gymroutine.ui.utils.CheckPasswordsText
import org.bonygod.gymroutine.ui.view.components.CustomDialog
import org.bonygod.gymroutine.ui.view.components.CustomPasswordTextField
import org.bonygod.gymroutine.ui.view.components.CustomTextField
import org.bonygod.gymroutine.ui.view.components.LogoGymRoutine
import org.bonygod.gymroutine.ui.view.viewModels.SignUpViewModel
import org.bonygod.gymroutine.view.GoogleSignin
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignUp(
    signUpViewModel: SignUpViewModel = koinViewModel(),
    navigateToWellcome: () -> Unit
) {
    val dialogViewModel by signUpViewModel.dialogViewModel.collectAsState()
    val email by signUpViewModel.email.collectAsState()
    val user by signUpViewModel.user.collectAsState()
    val password by signUpViewModel.password.collectAsState()
    val passwordRepeat by signUpViewModel.passwordRepeat.collectAsState()
    val passwordVisible by signUpViewModel.passwordVisible.collectAsState()
    val passwordVisibleRepeat by signUpViewModel.passwordVisibleRepeat.collectAsState()
    val colorFirstTextField by signUpViewModel.colorFirstTextField.collectAsState()
    val colorSecondTextField by signUpViewModel.colorSecondTextField.collectAsState()
    val showDialog by dialogViewModel.showDialog.collectAsState()
    val buttonVisible by signUpViewModel.buttonVisible.collectAsState()

    signUpViewModel.setTitlesGenericErrorDialog(
        stringResource(Res.string.loginOrSignup_dialog_title),
        stringResource(Res.string.loginOrSignup_dialog_subtitle)
    )

    signUpViewModel.setTitleErrorEmailExists(
        stringResource(Res.string.error_email_exist_title),
        stringResource(Res.string.error_email_exist_subtitle)
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
        LogoGymRoutine(size = 200.dp)

        CustomTextField(
            value = email,
            title = stringResource(Res.string.register_email),
            checkEmail = true,
            onValueChange = { email ->
                signUpViewModel.onEmailChange(email)
                signUpViewModel.checkFields()
            },
            validEmail = { valid ->
                signUpViewModel.validEmail(valid)
            }
        )

        Spacer(modifier = Modifier.padding(5.dp))

        CustomTextField(
            value = user,
            title = stringResource(Res.string.register_user),
            checkEmail = false,
            onValueChange = { user ->
                signUpViewModel.onUserChange(user)
                signUpViewModel.checkFields()
            })

        Spacer(modifier = Modifier.padding(5.dp))

        CustomPasswordTextField(
            password = password,
            passwordVisible = passwordVisible,
            title = stringResource(Res.string.register_password),
            color = colorFirstTextField,
            onPasswordChange = {
                signUpViewModel.onPasswordChange(it)
                signUpViewModel.checkFields()
            },
            onPasswordVisibleChange = { passwordVisible ->
                signUpViewModel.onPasswordVisibleChange(passwordVisible)
            }
        )
        CheckPasswordsText(password, passwordRepeat)

        Spacer(modifier = Modifier.padding(5.dp))

        CustomPasswordTextField(
            password = passwordRepeat,
            passwordVisible = passwordVisibleRepeat,
            title = stringResource(Res.string.register_repeat_password),
            color = colorSecondTextField,
            onPasswordChange = { passwordRepeat ->
                signUpViewModel.onPasswordRepeatChange(passwordRepeat)
                signUpViewModel.checkFields()
            },
            onPasswordVisibleChange = { passwordVisibleRepeat ->
                signUpViewModel.onPasswordVisibleRepeatChange(passwordVisibleRepeat)
            }
        )
        CheckPasswordsText(password, passwordRepeat)

        Spacer(modifier = Modifier.padding(15.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .clip(shape = RoundedCornerShape(30.dp))
                .height(50.dp),
            onClick = {
                signUpViewModel.signUp(dialogViewModel, navigateToWellcome)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Yellow,
                contentColor = Color.Black
            ),
            enabled = buttonVisible
        ) {
            Text(
                stringResource(Res.string.register_button_signup),
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

        GoogleSignin(navigateToWellcome)
    }
}