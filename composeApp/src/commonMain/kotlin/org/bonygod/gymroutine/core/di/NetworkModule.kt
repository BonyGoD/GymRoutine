package org.bonygod.gymroutine.core.di

import org.bonygod.gymroutine.data.network.AuthenticationService
import org.bonygod.gymroutine.data.repositories.AuthRepository
import org.bonygod.gymroutine.domain.LoginUseCase
import org.bonygod.gymroutine.domain.SignUpUseCase
import org.bonygod.gymroutine.ui.view.viewModels.DialogViewModel
import org.bonygod.gymroutine.ui.view.viewModels.LoginViewModel
import org.bonygod.gymroutine.ui.view.viewModels.SharedViewModel
import org.bonygod.gymroutine.ui.view.viewModels.SignUpViewModel
import org.koin.core.module.dsl.*
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val appModule = module {
    single { DialogViewModel() }
    single { SharedViewModel() }
    single { LoginViewModel() }
    single { SignUpViewModel() }
    single { LoginUseCase() }
    single { SignUpUseCase() }
}

val dataModule = module {
    factoryOf(::AuthenticationService)
    factoryOf(::AuthRepository)
}

val viewModelsModule = module {
    viewModelOf(::DialogViewModel)
    viewModelOf(::SharedViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignUpViewModel)
}

//expect val nativeModule : Module

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(viewModelsModule, appModule, dataModule)
    }
}