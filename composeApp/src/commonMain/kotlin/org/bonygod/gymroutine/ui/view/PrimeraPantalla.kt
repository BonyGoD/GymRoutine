package org.bonygod.gymroutine.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.gitlive.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.bonygod.gymroutine.ui.view.viewModels.SignUpViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PrimeraPantalla(auth: FirebaseAuth, scope: CoroutineScope, user: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Usuario logueado")
        Spacer(modifier = Modifier.padding(10.dp))
        Text(user)
        Button(onClick = {
            scope.launch {
                auth.signOut()
            }
        }) {
            Text("Cerrar sesión")
        }
    }
}