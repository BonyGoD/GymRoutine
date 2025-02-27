package org.bonygod.gymroutine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.login_spacer_login_google
import gymroutine.composeapp.generated.resources.register_button_signup
import gymroutine.composeapp.generated.resources.register_email
import gymroutine.composeapp.generated.resources.register_password
import gymroutine.composeapp.generated.resources.register_repeat_password
import gymroutine.composeapp.generated.resources.register_user
import kotlinx.coroutines.launch
import org.bonygod.gymroutine.ui.utils.CheckPasswordsText
import org.bonygod.gymroutine.ui.view.App
import org.bonygod.gymroutine.ui.view.components.CustomPasswordTextField
import org.bonygod.gymroutine.ui.view.components.CustomTextField
import org.bonygod.gymroutine.ui.view.components.GoogleButton
import org.bonygod.gymroutine.ui.view.components.LogoGymRoutine
import org.bonygod.gymroutine.ui.view.viewModels.SignUpViewModel
import org.jetbrains.compose.resources.stringResource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }

    }
}

@Preview(showSystemUi = true)
@Composable
fun AppAndroidPreview(signUpViewModel: SignUpViewModel = viewModel()) {
    App()
}