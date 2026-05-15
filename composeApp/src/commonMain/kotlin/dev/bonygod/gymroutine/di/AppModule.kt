package dev.bonygod.gymroutine.di

import dev.bonygod.gymroutine.auth.ui.AuthViewModel
import dev.bonygod.gymroutine.core.navigation.Navigator
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { Firebase.auth }
    single { Navigator() }
    viewModel { AuthViewModel(get(), get()) }
}

fun initKoin(config: KoinApplication.() -> Unit = {}) {
    startKoin {
        config()
        modules(appModule)
    }
}
