package org.bonygod.gymroutine.core.di

import org.bonygod.gymroutine.BuildConfig
import org.bonygod.gymroutine.data.localDb.CreateDatabase
import org.bonygod.gymroutine.data.localDb.RoomDb
import org.bonygod.gymroutine.data.network.AuthenticationService
import org.bonygod.gymroutine.data.repository.AuthRepository
import org.bonygod.gymroutine.data.repository.UserRepository
import org.bonygod.gymroutine.domain.DeleteUserUseCase
import org.bonygod.gymroutine.domain.GetUserUseCase
import org.bonygod.gymroutine.domain.InsertUserUseCase
import org.bonygod.gymroutine.domain.LoginUseCase
import org.bonygod.gymroutine.domain.SignUpUseCase
import org.bonygod.gymroutine.domain.UpdateUserUseCase
import org.bonygod.gymroutine.ui.view.viewModels.DialogViewModel
import org.bonygod.gymroutine.ui.view.viewModels.LoginViewModel
import org.bonygod.gymroutine.ui.view.viewModels.PrimeraPantallaViewModel
import org.bonygod.gymroutine.ui.view.viewModels.SharedViewModel
import org.bonygod.gymroutine.ui.view.viewModels.SignUpViewModel
import org.bonygod.gymroutine.ui.view.viewModels.UserViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val appModule = module {
    single { DialogViewModel() }
    single { SharedViewModel() }
    factory { LoginViewModel() }
    factory { SignUpViewModel() }
    factory { PrimeraPantallaViewModel() }
    single { LoginUseCase() }
    single { SignUpUseCase() }
    single(named("API_KEY")) { BuildConfig.API_KEY }
    single(named("CLIENT_ID")) { BuildConfig.CLIENT_ID }
    single <RoomDb> { CreateDatabase(get()).getDatabase() }
}

val dataModule = module {
    factoryOf(::AuthenticationService)
    factoryOf(::AuthRepository)
    singleOf(::UserRepository)
    singleOf(::InsertUserUseCase)
    singleOf(::UpdateUserUseCase)
    singleOf(::DeleteUserUseCase)
    singleOf(::GetUserUseCase)
}

val viewModelsModule = module {
    viewModelOf(::DialogViewModel)
    viewModelOf(::SharedViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::UserViewModel)
    viewModelOf(::PrimeraPantallaViewModel)
}

//expect val nativeModule : Module

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(viewModelsModule, appModule, dataModule)
    }
}