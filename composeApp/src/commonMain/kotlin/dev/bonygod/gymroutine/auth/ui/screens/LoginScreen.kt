package dev.bonygod.gymroutine.auth.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import dev.bonygod.gymroutine.auth.ui.AuthViewModel
import dev.bonygod.gymroutine.auth.ui.components.SocialDivider
import dev.bonygod.gymroutine.auth.ui.interactions.AuthEffect
import dev.bonygod.gymroutine.auth.ui.interactions.AuthEvent
import dev.bonygod.gymroutine.getPlatform
import dev.bonygod.signin.kmp.ui.AppleSignin
import dev.bonygod.signin.kmp.ui.GoogleSignin
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.applelogo
import gymroutine.composeapp.generated.resources.common_email_label
import gymroutine.composeapp.generated.resources.common_password_label
import gymroutine.composeapp.generated.resources.google_icon
import gymroutine.composeapp.generated.resources.login_screen_apple_signin
import gymroutine.composeapp.generated.resources.login_screen_forgot_password
import gymroutine.composeapp.generated.resources.login_screen_google_access
import gymroutine.composeapp.generated.resources.login_screen_login_button
import gymroutine.composeapp.generated.resources.login_screen_no_account
import gymroutine.composeapp.generated.resources.login_screen_or_login_with
import gymroutine.composeapp.generated.resources.login_screen_register_link
import gymroutine.composeapp.generated.resources.login_screen_subtitle
import gymroutine.composeapp.generated.resources.login_screen_title
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(viewModel: AuthViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is AuthEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
                else -> Unit
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(stringResource(Res.string.login_screen_title), style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(8.dp))
            Text(stringResource(Res.string.login_screen_subtitle), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(32.dp))

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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus(force = true)
                    keyboardController?.hide()
                    viewModel.onEvent(AuthEvent.OnSignInClick)
                }),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            Spacer(Modifier.height(8.dp))

            TextButton(
                onClick = { viewModel.onEvent(AuthEvent.OnNavigateToForgotPassword) },
                modifier = Modifier.align(Alignment.End),
            ) { Text(stringResource(Res.string.login_screen_forgot_password)) }

            Spacer(Modifier.height(16.dp))

            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                Button(onClick = {
                    focusManager.clearFocus(force = true)
                    keyboardController?.hide()
                    viewModel.onEvent(AuthEvent.OnSignInClick)
                }, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(Res.string.login_screen_login_button))
                }

                SocialDivider(stringResource(Res.string.login_screen_or_login_with))

                GoogleSignin(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 18.dp)
                        .border(1.dp, Color.White, RoundedCornerShape(30.dp))
                        .clip(RoundedCornerShape(30.dp))
                        .height(50.dp),
                    text = stringResource(Res.string.login_screen_google_access),
                    textColor = Color.White,
                    icon = painterResource(Res.drawable.google_icon),
                    onSuccess = { displayName, uid, email, _ ->
                        viewModel.onEvent(AuthEvent.OnGoogleSignInSuccess(uid, displayName, email))
                    },
                    onError = { errorMessage ->
                        viewModel.onEvent(AuthEvent.OnGoogleSignInError(errorMessage))
                    },
                )

                if (getPlatform().name.contains("Android").not()) {
                    AppleSignin(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                            .height(50.dp),
                        text = stringResource(Res.string.login_screen_apple_signin),
                        containerColor = Color.Black,
                        contentColor = Color.White,
                        icon = painterResource(Res.drawable.applelogo),
                        textColor = Color.White,
                        onSuccess = { _, uid, email, _ ->
                            val name = email.substringBefore("@")
                            viewModel.onEvent(AuthEvent.OnGoogleSignInSuccess(uid, name, email))
                        },
                        onError = { errorMessage ->
                            viewModel.onEvent(AuthEvent.OnGoogleSignInError(errorMessage))
                        },
                    )
                }

                Spacer(Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(stringResource(Res.string.login_screen_no_account), style = MaterialTheme.typography.bodyMedium)
                    TextButton(onClick = { viewModel.onEvent(AuthEvent.OnNavigateToRegister) }) {
                        Text(stringResource(Res.string.login_screen_register_link))
                    }
                }
            }
        }
    }
}
