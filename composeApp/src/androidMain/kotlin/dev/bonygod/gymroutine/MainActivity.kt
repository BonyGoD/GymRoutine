package dev.bonygod.gymroutine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import dev.bonygod.crashlytics.kmp.core.CrashlyticsConfig
import dev.bonygod.crashlytics.kmp.core.CrashlyticsKMP
import dev.bonygod.crashlytics.kmp.core.CrashlyticsKeys
import dev.bonygod.gymroutine.watch.WearSync
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Firebase.initialize(this)
        // CrashlyticsKMP.initialize() necesita Firebase ya inicializado. initPlatform() ya se llamó
        // en GymRoutineApp, así que isDebugBuild de AndroidPlatform tiene su appContext.
        val platform = getPlatform()
        CrashlyticsKMP.initialize(
            CrashlyticsConfig(
                isDebugBuild = platform.isDebugBuild,
                // La app aún no está publicada y se prueba en debug: sin esto la librería desactiva
                // la recolección y no llegaría nada a la consola. Poner a false al publicar.
                collectionEnabledInDebug = true,
                defaultCustomKeys = mapOf(CrashlyticsKeys.PLATFORM to platform.name),
            ),
        )
        setContent {
            App()
        }
        lifecycleScope.launch { WearSync(applicationContext).drainResults() }
    }
}
