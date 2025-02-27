package org.bonygod.gymroutine.ui.view.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.visibility
import gymroutine.composeapp.generated.resources.visibilityoff
import org.bonygod.gymroutine.ui.utils.BiggerPasswordVisualTransformation
import org.jetbrains.compose.resources.painterResource

@Composable
fun CustomPasswordTextField(
    password: String,
    passwordVisible: Boolean,
    title: String,
    color: Color,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibleChange: (Boolean) -> Unit
) {
    Column {
        Text(
            title,
            modifier = Modifier.align(Alignment.Start).padding(horizontal = 20.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.padding(3.dp))
        TextField(
            value = password,
            onValueChange = {
                onPasswordChange(it)
            },
            modifier = Modifier
                .align(Alignment.Start)
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .clip(shape = RoundedCornerShape(30.dp))
                .border(width = 1.dp, color = color, shape = RoundedCornerShape(30.dp))
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

                IconButton(onClick = { onPasswordVisibleChange(!passwordVisible) }) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = image,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            },
        )
    }
}