package org.bonygod.gymroutine.ui.utils

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.register_password_not_match
import gymroutine.composeapp.generated.resources.register_password_not_minimum_large
import org.jetbrains.compose.resources.stringResource

@Composable
fun CheckPasswordsText(password: String, passwordRepeat: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
    if (password != passwordRepeat) {
        Text(
            text = stringResource(Res.string.register_password_not_match),
            modifier = Modifier.align(Alignment.Start).padding(horizontal = 25.dp),
            color = Color.Red,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }
    if (password.length < 6 && passwordRepeat.isNotEmpty()) {
        Text(
            text = stringResource(Res.string.register_password_not_minimum_large),
            modifier = Modifier.align(Alignment.Start).padding(horizontal = 20.dp),
            color = Color.Red,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }
        }
}