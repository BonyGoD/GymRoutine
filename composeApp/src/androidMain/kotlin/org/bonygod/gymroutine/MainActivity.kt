package org.bonygod.gymroutine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import org.bonygod.gymroutine.ui.view.App
import org.bonygod.gymroutine.ui.view.viewModels.SignUpViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }

    }
}

@Preview(showSystemUi = true)
@Composable
fun AppAndroidPreview(signUpViewModel: SignUpViewModel = viewModel()) {
    App()
}