package dev.bonygod.gymroutine.wear

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import dev.bonygod.gymroutine.wear.ui.WatchApp
import dev.bonygod.gymroutine.wear.ui.WatchViewModel
import dev.bonygod.gymroutine.wear.ui.interactions.WatchEvent
import dev.bonygod.gymroutine.wear.ui.theme.WatchTheme

class MainActivity : ComponentActivity() {

    private val viewModel: WatchViewModel by lazy { ViewModelProvider(this)[WatchViewModel::class.java] }
    private var hasResumedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state by viewModel.state.collectAsState()
            LaunchedEffect(state.rest != null) {
                if (state.rest != null) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                } else {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                }
            }
            WatchTheme {
                WatchApp(viewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (hasResumedOnce) {
            viewModel.onEvent(WatchEvent.OnRefresh)
        }
        hasResumedOnce = true
    }
}
