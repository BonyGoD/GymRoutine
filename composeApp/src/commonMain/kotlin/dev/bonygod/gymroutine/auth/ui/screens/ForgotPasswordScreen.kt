package dev.bonygod.gymroutine.auth.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.bonygod.gymroutine.auth.ui.AuthViewModel
import dev.bonygod.gymroutine.auth.ui.interactions.AuthEffect
import dev.bonygod.gymroutine.auth.ui.interactions.AuthEvent
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(viewModel: AuthViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is AuthEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
                else -> Unit
            }
        }
    }

    if (state.showDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(AuthEvent.DismissDialog) },
            confirmButton = {
                TextButton(onClick = { viewModel.onEvent(AuthEvent.DismissDialog) }) { Text("Aceptar") }
            },
            title = { Text("Correo enviado") },
            text = { Text("Revisa tu bandeja para el enlace de restablecimiento.") },
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Restablecer contrasena") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onEvent(AuthEvent.OnBackClick) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atras")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text("Has olvidado tu contrasena?", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            Spacer(Modifier.height(8.dp))
            Text("Escribe tu correo y te enviaremos un enlace.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(32.dp))

            OutlinedTextField(
                value = state.getUserData().email,
                onValueChange = { viewModel.onEvent(AuthEvent.OnEmailChange(it)) },
                label = { Text("Correo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            Spacer(Modifier.height(24.dp))

            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { viewModel.onEvent(AuthEvent.OnResetPassword(state.getUserData().email)) },
                    modifier = Modifier.fillMaxWidth(),
                ) { Text("Enviar enlace") }
            }
        }
    }
}
