package org.bonygod.gymroutine.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.google_icon
import gymroutine.composeapp.generated.resources.gymroutine_icon
import gymroutine.composeapp.generated.resources.login_button_text_google
import gymroutine.composeapp.generated.resources.login_button_text_login
import gymroutine.composeapp.generated.resources.login_email
import gymroutine.composeapp.generated.resources.login_forgot_password
import gymroutine.composeapp.generated.resources.login_password
import gymroutine.composeapp.generated.resources.login_spacer_login_google
import gymroutine.composeapp.generated.resources.visibility
import gymroutine.composeapp.generated.resources.visibilityoff
import org.bonygod.gymroutine.ui.utils.BiggerPasswordVisualTransformation
import org.jetbrains.compose.resources.painterResource
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
            Image(
                painter = painterResource(Res.drawable.gymroutine_icon),
                contentDescription = "Logo",
                modifier = Modifier.size(300.dp)
            )
            Text(
                stringResource(Res.string.login_email),
                modifier = Modifier.align(Alignment.Start).padding(horizontal = 20.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.padding(3.dp))
            TextField(
                value = email,
                onValueChange = {
                    email = it
                },
                modifier = Modifier
                    .align(Alignment.Start)
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .clip(shape = RoundedCornerShape(30.dp))
                    .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(30.dp))
                    .height(50.dp),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Text(
                stringResource(Res.string.login_password),
                modifier = Modifier.align(Alignment.Start).padding(horizontal = 20.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.padding(3.dp))
            TextField(
                value = password,
                onValueChange = {
                    password = it
                },
                modifier = Modifier
                    .align(Alignment.Start)
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .clip(shape = RoundedCornerShape(30.dp))
                    .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(30.dp))
                    .height(50.dp),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                visualTransformation = if (passwordVisible) VisualTransformation.None else BiggerPasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible)
                        painterResource(Res.drawable.visibility)
                    else painterResource(Res.drawable.visibilityoff)

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = image,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                },
            )
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
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(30.dp))
                    .clip(shape = RoundedCornerShape(30.dp))
                    .height(50.dp),
                onClick = { /*TODO: Ir al registro de Google*/ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                )
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.google_icon),
                            contentDescription = "Google",
                            modifier = Modifier.size(24.dp),
                            tint = Color.Unspecified
                        )
                    }
                    Text(
                        stringResource(Res.string.login_button_text_google),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}