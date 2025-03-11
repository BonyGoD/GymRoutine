package org.bonygod.gymroutine.ui.view.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.google_icon
import gymroutine.composeapp.generated.resources.login_button_text_google
import kotlinx.coroutines.launch
import org.bonygod.gymroutine.core.network.GoogleAuthHelper
import org.bonygod.gymroutine.ui.utils.createUserDb
import org.bonygod.gymroutine.ui.view.viewModels.UserViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GoogleButton(googleAuthHelper: GoogleAuthHelper, navigateToWellcome: () -> Unit) {

    val error = rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val userViewModel = koinViewModel<UserViewModel>()

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(30.dp))
            .clip(shape = RoundedCornerShape(30.dp))
            .height(50.dp),
        onClick = {
            scope.launch {
                googleAuthHelper.signInWithGoogle(
                    onSuccess = { userName, userUid, tokenId, mail, photo ->
                        val userDb = createUserDb(userUid, userName, mail, tokenId)
                        userViewModel.insertUser(userDb)
                        navigateToWellcome()
                    },
                    onError = { errorMsg ->
                        error.value = errorMsg
                    })
            }
        },
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