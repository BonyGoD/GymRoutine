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
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.common_accept
import gymroutine.composeapp.generated.resources.common_back_description
import gymroutine.composeapp.generated.resources.common_email_label
import gymroutine.composeapp.generated.resources.forgot_password_screen_dialog_message
import gymroutine.composeapp.generated.resources.forgot_password_screen_dialog_title
import gymroutine.composeapp.generated.resources.forgot_password_screen_send_button
import gymroutine.composeapp.generated.resources.forgot_password_screen_subtitle
import gymroutine.composeapp.generated.resources.forgot_password_screen_title
import gymroutine.composeapp.generated.resources.forgot_password_screen_topbar_title
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.stringResource
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
                TextButton(onClick = { viewModel.onEvent(AuthEvent.DismissDialog) }) {
                    Text(stringResource(Res.string.common_accept))
                }
            },
            title = { Text(stringResource(Res.string.forgot_password_screen_dialog_title)) },
            text = { Text(stringResource(Res.string.forgot_password_screen_dialog_message)) },
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.forgot_password_screen_topbar_title)) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onEvent(AuthEvent.OnBackClick) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(Res.string.common_back_description))
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
            Text(stringResource(Res.string.forgot_password_screen_title), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            Spacer(Modifier.height(8.dp))
            Text(stringResource(Res.string.forgot_password_screen_subtitle), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(32.dp))

            OutlinedTextField(
                value = state.getUserData().email,
                onValueChange = { viewModel.onEvent(AuthEvent.OnEmailChange(it)) },
                label = { Text(stringResource(Res.string.common_email_label)) },
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
                ) { Text(stringResource(Res.string.forgot_password_screen_send_button)) }
            }
        }
    }
}
