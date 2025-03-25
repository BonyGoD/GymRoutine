package org.bonygod.gymroutine.ui.view.userProfileScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.wellcome_button
import gymroutine.composeapp.generated.resources.wellcome_subtitle
import gymroutine.composeapp.generated.resources.wellcome_title
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import org.bonygod.gymroutine.data.model.User
import org.bonygod.gymroutine.ui.theme.CustomBlack
import org.bonygod.gymroutine.ui.theme.CustomYellow
import org.bonygod.gymroutine.ui.view.components.LoadingScreen
import org.bonygod.gymroutine.ui.view.viewModels.UserViewModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun Wellcome(
    navigateToUserProfile: () -> Unit
) {

    val userViewModel = koinViewModel<UserViewModel>()
    var user by remember { mutableStateOf<User?>(null) }
    var showScreen by remember { mutableStateOf<Boolean>(false) }

    LaunchedEffect(Unit) {
        delay(500)
        user = userViewModel.getUser().first()
        showScreen = true
    }

    if (!showScreen) {
        LoadingScreen()
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(CustomYellow, CustomBlack)
                )
            ),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(Res.string.wellcome_title),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            text = user?.displayName ?: "",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Text(
            text = stringResource(Res.string.wellcome_subtitle),
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.padding(20.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .height(50.dp),
            onClick = {
                navigateToUserProfile()
            },
            colors = ButtonDefaults.buttonColors(
                CustomBlack
            )
        ) {
            Text(
                text = stringResource(Res.string.wellcome_button),
                color = CustomYellow,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

        }
        Spacer(modifier = Modifier.padding(10.dp))
    }
}