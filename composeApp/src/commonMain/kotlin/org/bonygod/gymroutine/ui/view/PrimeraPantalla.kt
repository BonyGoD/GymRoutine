package org.bonygod.gymroutine.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import org.bonygod.gymroutine.data.model.User
import org.bonygod.gymroutine.ui.navigation.LoadingScreen
import org.bonygod.gymroutine.ui.view.viewModels.PrimeraPantallaViewModel
import org.bonygod.gymroutine.ui.view.viewModels.UserViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PrimeraPantalla(
    primeraPantallaViewModel: PrimeraPantallaViewModel = koinViewModel(),
    navigateToLoginOrSignup: () -> Unit
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
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.padding(10.dp))
        Text("Bienvenido: ${user?.displayName}")
        Spacer(modifier = Modifier.padding(10.dp))
        Button(onClick = { primeraPantallaViewModel.logOut(navigateToLoginOrSignup)}) {
            Text("Cerrar sesión")
        }
    }
}