package dev.bonygod.gymroutine.di

import dev.bonygod.gymroutine.BuildConfig
import dev.bonygod.gymroutine.auth.data.datasource.AuthRemoteDataSource
import dev.bonygod.gymroutine.auth.data.datasource.AuthRemoteDataSourceImpl
import dev.bonygod.gymroutine.auth.data.repository.AuthRepositoryImpl
import dev.bonygod.gymroutine.auth.domain.repository.AuthRepository
import dev.bonygod.gymroutine.auth.domain.usecase.LoginUseCase
import dev.bonygod.gymroutine.auth.domain.usecase.LoginWithSocialProviderUseCase
import dev.bonygod.gymroutine.auth.domain.usecase.LogoutUseCase
import dev.bonygod.gymroutine.auth.domain.usecase.RegisterUseCase
import dev.bonygod.gymroutine.auth.domain.usecase.SendPasswordResetUseCase
import dev.bonygod.gymroutine.auth.ui.AuthViewModel
import dev.bonygod.gymroutine.core.navigation.Navigator
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    // Firebase
    single { Firebase.auth }
    single { Firebase.firestore }

    // Navigation
    single { Navigator() }

    // Config
    single<String>(named("API_KEY")) { BuildConfig.FIREBASE_API_KEY }
    single<String>(named("CLIENT_ID")) { BuildConfig.CLIENT_ID }

    // Auth data layer
    single<AuthRemoteDataSource> { AuthRemoteDataSourceImpl(get(), get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }

    // Auth use cases
    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }
    factory { SendPasswordResetUseCase(get()) }
    factory { LogoutUseCase(get()) }
    factory { LoginWithSocialProviderUseCase(get()) }

    // ViewModels
    viewModel { AuthViewModel(get(), get(), get(), get(), get()) }
}

fun initKoin(config: KoinApplication.() -> Unit = {}) {
    startKoin {
        config()
        modules(appModule)
    }
}
