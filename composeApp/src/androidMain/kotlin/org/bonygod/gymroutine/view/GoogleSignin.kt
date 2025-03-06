package org.bonygod.gymroutine.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import org.bonygod.gymroutine.core.network.GoogleAuthHelper
import org.bonygod.gymroutine.ui.view.components.GoogleButton

@Composable
actual fun GoogleSignin(navigateToPrimeraPantalla: () -> Unit) {
    val context = LocalContext.current
    val googleAuthHelper = GoogleAuthHelper(context, CredentialManager.create(context))

    GoogleButton(
        googleAuthHelper,
        navigateToPrimeraPantalla
    )
}