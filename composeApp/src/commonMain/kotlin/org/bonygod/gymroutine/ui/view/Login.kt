package org.bonygod.gymroutine.ui.view

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.login_button_text_login
import gymroutine.composeapp.generated.resources.login_email
import gymroutine.composeapp.generated.resources.login_forgot_password
import gymroutine.composeapp.generated.resources.login_password
import gymroutine.composeapp.generated.resources.login_spacer_login_google
import org.bonygod.gymroutine.ui.view.components.CustomTextField
import org.bonygod.gymroutine.ui.view.components.GoogleButton
import org.bonygod.gymroutine.ui.view.components.LogoGymRoutine
import org.bonygod.gymroutine.ui.view.components.PasswordTextField
import org.jetbrains.compose.resources.stringResource

class Login : Screen {
    @Composable
    override fun Content() {
        var passwordVisible by remember { mutableStateOf(false) }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Column(
            modifier = Modifier.fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Yellow, Color.Black)
                    )
                ),
            Arrangement.Top, Alignment.CenterHorizontally
        ) {
            LogoGymRoutine(size = 300.dp)

            CustomTextField(email, stringResource(Res.string.login_email), onValueChange = { email = it })

            Spacer(modifier = Modifier.padding(5.dp))

            PasswordTextField(password, passwordVisible, stringResource(Res.string.login_password),
                onPasswordChange = { password = it },
                onPasswordVisibleChange = { passwordVisible = it })

            Text(
                stringResource(Res.string.login_forgot_password),
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    .clickable {
                        /*TODO: Click en Olvidé mi contraseña*/
                    },
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
                onClick = { /*TODO: Ir a la pantalla de inicio*/ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Yellow,
                    contentColor = Color.Black
                )
            ) {
                Text(stringResource(Res.string.login_button_text_login), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f).height(1.dp).background(Color.White))
                Text(stringResource(Res.string.login_spacer_login_google), modifier = Modifier.padding(horizontal = 8.dp), fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                Spacer(modifier = Modifier.weight(1f).height(1.dp).background(Color.White))
            }

            GoogleButton()
        }
    }
}