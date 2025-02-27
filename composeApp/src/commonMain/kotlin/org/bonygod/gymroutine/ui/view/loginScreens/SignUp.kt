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
import gymroutine.composeapp.generated.resources.login_spacer_login_google
import gymroutine.composeapp.generated.resources.register_button_signup
import gymroutine.composeapp.generated.resources.register_email
import gymroutine.composeapp.generated.resources.register_password
import gymroutine.composeapp.generated.resources.register_repeat_password
import gymroutine.composeapp.generated.resources.register_user
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.bonygod.gymroutine.ui.utils.CheckPasswordsText
import org.bonygod.gymroutine.ui.view.components.CustomPasswordTextField
import org.bonygod.gymroutine.ui.view.components.CustomTextField
import org.bonygod.gymroutine.ui.view.components.GoogleButton
import org.bonygod.gymroutine.ui.view.components.LogoGymRoutine
import org.bonygod.gymroutine.ui.view.viewModels.SignUpViewModel
import org.jetbrains.compose.resources.stringResource

@Composable
fun SignUp(auth: FirebaseAuth, scope: CoroutineScope, signUpViewModel: SignUpViewModel = viewModel()) {
    val focusManager = LocalFocusManager.current
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
            value = signUpViewModel.email,
            title = stringResource(Res.string.register_email),
            checkEmail = true,
            onValueChange = { signUpViewModel.email = it })

        Spacer(modifier = Modifier.padding(5.dp))

        CustomTextField(
            value = signUpViewModel.user,
            title = stringResource(Res.string.register_user),
            checkEmail = false,
            onValueChange = { signUpViewModel.user = it })

        Spacer(modifier = Modifier.padding(5.dp))

        CustomPasswordTextField(
            password = signUpViewModel.password,
            passwordVisible = signUpViewModel.passwordVisible,
            title = stringResource(Res.string.register_password),
            color = signUpViewModel.colorFirstTextField,
            onPasswordChange = { signUpViewModel.onPasswordChange(it) },
            onPasswordVisibleChange = { signUpViewModel.passwordVisible = it }
        )
        CheckPasswordsText(signUpViewModel.password, signUpViewModel.passwordRepeat)

        Spacer(modifier = Modifier.padding(5.dp))

        CustomPasswordTextField(
            password = signUpViewModel.passwordRepeat,
            passwordVisible = signUpViewModel.passwordVisibleRepeat,
            title = stringResource(Res.string.register_repeat_password),
            color = signUpViewModel.colorSecondTextField,
            onPasswordChange = { signUpViewModel.onPasswordRepeatChange(it) },
            onPasswordVisibleChange = { signUpViewModel.passwordVisibleRepeat = it }
        )
        CheckPasswordsText(signUpViewModel.password, signUpViewModel.passwordRepeat)

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
                        auth.createUserWithEmailAndPassword(signUpViewModel.email, signUpViewModel.password)
                    } catch (e: Exception) {
                        // Handle exception
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Yellow,
                contentColor = Color.Black
            )
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

        GoogleButton()
    }
}