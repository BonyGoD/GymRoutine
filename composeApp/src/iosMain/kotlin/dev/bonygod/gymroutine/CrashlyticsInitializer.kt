package dev.bonygod.gymroutine

import dev.bonygod.crashlytics.kmp.core.CrashlyticsConfig
import dev.bonygod.crashlytics.kmp.core.CrashlyticsKMP
import dev.bonygod.crashlytics.kmp.core.CrashlyticsKeys

/**
 * Inicializa CrashlyticsKMP en iOS. Pensada para llamarse desde Swift, en
 * `AppDelegate.application(_:didFinishLaunchingWithOptions:)`, justo después de
 * `FirebaseApp.configure()` — `CrashlyticsKMP.initialize()` necesita Firebase ya inicializado.
 *
 * A propósito no se llama "initXxx": el exportador de Kotlin/Native antepone "do" a los nombres
 * que empiezan por "init" seguido de mayúscula (así `initKoin()` se convirtió en `doInitKoin()`
 * en `iOSApp.swift`), y aquí interesa un nombre predecible desde Swift.
 */
fun configureCrashlytics() {
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
}
