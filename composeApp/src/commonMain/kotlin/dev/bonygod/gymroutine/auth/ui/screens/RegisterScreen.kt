package dev.bonygod.gymroutine.auth.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import dev.bonygod.gymroutine.auth.ui.AuthViewModel
import dev.bonygod.gymroutine.auth.ui.components.SocialDivider
import dev.bonygod.gymroutine.auth.ui.interactions.AuthEffect
import dev.bonygod.gymroutine.auth.ui.interactions.AuthEvent
import dev.bonygod.signin.kmp.ui.GoogleSignin
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.common_back_description
import gymroutine.composeapp.generated.resources.common_email_label
import gymroutine.composeapp.generated.resources.common_name_label
import gymroutine.composeapp.generated.resources.common_password_label
import gymroutine.composeapp.generated.resources.google_icon
import gymroutine.composeapp.generated.resources.register_screen_confirm_password_label
import gymroutine.composeapp.generated.resources.register_screen_google_register
import gymroutine.composeapp.generated.resources.register_screen_or_register_with
import gymroutine.composeapp.generated.resources.register_screen_register_button
import gymroutine.composeapp.generated.resources.register_screen_title
import gymroutine.composeapp.generated.resources.register_screen_topbar_title
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(viewModel: AuthViewModel = koinViewModel()) {
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

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.register_screen_topbar_title)) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onEvent(AuthEvent.OnBackClick) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(Res.string.common_back_description))
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(stringResource(Res.string.register_screen_title), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(32.dp))

            OutlinedTextField(
                value = state.getUserData().userName,
                onValueChange = { viewModel.onEvent(AuthEvent.OnUserNameChange(it)) },
                label = { Text(stringResource(Res.string.common_name_label)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = state.getUserData().email,
                onValueChange = { viewModel.onEvent(AuthEvent.OnEmailChange(it)) },
                label = { Text(stringResource(Res.string.common_email_label)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = state.getUserData().password,
                onValueChange = { viewModel.onEvent(AuthEvent.OnPasswordChange(it)) },
                label = { Text(stringResource(Res.string.common_password_label)) },
                visualTransformation = if (state.eyePasswordOpen) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { viewModel.onEvent(AuthEvent.OnEyePasswordClick) }) {
                        Icon(if (state.eyePasswordOpen) Icons.Default.VisibilityOff else Icons.Default.Visibility, null)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = state.getUserData().confirmPassword,
                onValueChange = { viewModel.onEvent(AuthEvent.OnConfirmPasswordChange(it)) },
                label = { Text(stringResource(Res.string.register_screen_confirm_password_label)) },
                visualTransformation = if (state.eyeConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { viewModel.onEvent(AuthEvent.OnEyeConfirmPasswordClick) }) {
                        Icon(if (state.eyeConfirmPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility, null)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            Spacer(Modifier.height(24.dp))

            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                Button(onClick = { viewModel.onEvent(AuthEvent.OnRegisterClick) }, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(Res.string.register_screen_register_button))
                }

                SocialDivider(stringResource(Res.string.register_screen_or_register_with))

                GoogleSignin(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 18.dp)
                        .border(1.dp, Color.White, RoundedCornerShape(30.dp))
                        .clip(RoundedCornerShape(30.dp))
                        .height(50.dp),
                    text = stringResource(Res.string.register_screen_google_register),
                    textColor = Color.White,
                    icon = painterResource(Res.drawable.google_icon),
                    onSuccess = { displayName, uid, email, _ ->
                        viewModel.onEvent(AuthEvent.OnGoogleSignInSuccess(uid, displayName, email))
                    },
                    onError = { errorMessage ->
                        viewModel.onEvent(AuthEvent.OnGoogleSignInError(errorMessage))
                    },
                )
            }
        }
    }
}
