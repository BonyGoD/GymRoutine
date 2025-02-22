package org.bonygod.gymroutine.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.loginOrSignup_button_login
import gymroutine.composeapp.generated.resources.loginOrSignup_button_signup
import gymroutine.composeapp.generated.resources.loginOrSignup_second_subtitle
import gymroutine.composeapp.generated.resources.loginOrSignup_subtitle
import org.bonygod.gymroutine.ui.view.components.LogoGymRoutine
import org.jetbrains.compose.resources.stringResource

class LoginOrSignup : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current
        Column(
            modifier = Modifier.fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Yellow, Color.Black)
                    )
                ),
            Arrangement.Top, Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            LogoGymRoutine(size = 300.dp)

            Text(
                stringResource(Res.string.loginOrSignup_subtitle),
                fontWeight = FontWeight.Bold,
                fontSize = 35.sp
            )
            Text(
                stringResource(Res.string.loginOrSignup_second_subtitle),
                fontWeight = FontWeight.Bold,
                fontSize = 35.sp
            )
            Spacer(modifier = Modifier.height(100.dp))
        }
        Column(modifier =
            Modifier.fillMaxSize()
                .padding(bottom = 50.dp),
            Arrangement.Bottom, Alignment.CenterHorizontally) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(30.dp))
                    .clip(shape = RoundedCornerShape(30.dp))
                    .height(50.dp),
                onClick = {
                    navigator?.push(Login())
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Yellow,
                    contentColor = Color.Black
                )
            ) {
                Text(stringResource(Res.string.loginOrSignup_button_signup), fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(30.dp))
                    .clip(shape = RoundedCornerShape(30.dp))
                    .height(50.dp),
                onClick = {
                    navigator?.push(Register())
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                )
            ) {
                Text(stringResource(Res.string.loginOrSignup_button_login), fontWeight = FontWeight.Bold)
            }
        }
    }
}