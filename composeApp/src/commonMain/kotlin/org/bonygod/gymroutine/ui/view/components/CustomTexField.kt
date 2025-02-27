package org.bonygod.gymroutine.ui.view.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomTextField(
    value: String,
    title: String,
    checkEmail: Boolean,
    onValueChange: (String) -> Unit
) {
    val borderColor = remember { mutableStateOf(Color.Black) }
    val isValidEmail = remember { mutableStateOf(true) }

    Column {
        Text(
            title,
            modifier = Modifier.align(Alignment.Start).padding(horizontal = 20.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.padding(3.dp))
        TextField(
            value = value,
            onValueChange = {
                onValueChange(it)
                if (checkEmail) {
                    isValidEmail.value = checkValidEmail(it)
                    borderColor.value = if (checkValidEmail(it)) Color.Black else Color.Red
                }
            },
            modifier = Modifier
                .align(Alignment.Start)
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .clip(shape = RoundedCornerShape(30.dp))
                .border(width = 1.dp, color = borderColor.value, shape = RoundedCornerShape(30.dp))
                .height(50.dp),
            textStyle = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        )
        if (!isValidEmail.value && checkEmail) {
            Text(
                "Invalid email",
                modifier = Modifier.align(Alignment.Start).padding(horizontal = 20.dp),
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
    }
}

fun checkValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
    return emailRegex.matches(email)
}