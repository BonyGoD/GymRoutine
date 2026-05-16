package dev.bonygod.gymroutine.di

import dev.bonygod.gymroutine.BuildConfig
import dev.bonygod.gymroutine.auth.ui.AuthViewModel
import dev.bonygod.gymroutine.core.navigation.Navigator
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule =
    module {
        single { Firebase.auth }
        single { Navigator() }
        single<String>(named("API_KEY")) { BuildConfig.FIREBASE_API_KEY }
        single<String>(named("CLIENT_ID")) { BuildConfig.CLIENT_ID }
        viewModel { AuthViewModel(get(), get()) }
    }

fun initKoin(config: KoinApplication.() -> Unit = {}) {
    startKoin {
        config()
        modules(appModule)
    }
}
