package org.bonygod.gymroutine.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.bonygod.gymroutine.ui.view.viewModels.UserViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PrimeraPantalla(scope: CoroutineScope, navigateToLoginOrSignup: () -> Unit) {

    val userViewModel = koinViewModel<UserViewModel>()

    val user by userViewModel.getUser().collectAsStateWithLifecycle(initialValue = null)

    var updatedUser by remember { mutableStateOf(user) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Usuario logueado")
        Spacer(modifier = Modifier.padding(10.dp))
        Button(onClick = {
            scope.launch {
                updatedUser = user?.copy(token = "")
                userViewModel.updateUser(updatedUser!!)
                navigateToLoginOrSignup()
            }
        }) {
            Text("Cerrar sesión")
        }
    }
}