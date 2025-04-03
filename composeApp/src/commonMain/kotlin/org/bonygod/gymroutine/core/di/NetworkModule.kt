package org.bonygod.gymroutine.core.di

import org.bonygod.gymroutine.BuildConfig
import org.bonygod.gymroutine.data.localDb.CreateDatabase
import org.bonygod.gymroutine.data.localDb.RoomDb
import org.bonygod.gymroutine.data.network.AuthenticationService
import org.bonygod.gymroutine.data.network.UserDataService
import org.bonygod.gymroutine.data.repository.AuthRepository
import org.bonygod.gymroutine.data.repository.UserDataRepository
import org.bonygod.gymroutine.data.repository.UserRepository
import org.bonygod.gymroutine.domain.DeleteUserDaoUseCase
import org.bonygod.gymroutine.domain.ForgotPasswordUseCase
import org.bonygod.gymroutine.domain.GetUserDaoUseCase
import org.bonygod.gymroutine.domain.GetUserUseCase
import org.bonygod.gymroutine.domain.InsertUserDaoUseCase
import org.bonygod.gymroutine.domain.LoginUseCase
import org.bonygod.gymroutine.domain.SaveUserDataUseCase
import org.bonygod.gymroutine.domain.SignUpUseCase
import org.bonygod.gymroutine.domain.UpdateUserDaoUseCase
import org.bonygod.gymroutine.ui.view.viewModels.DialogViewModel
import org.bonygod.gymroutine.ui.view.viewModels.ForgotPasswordViewModel
import org.bonygod.gymroutine.ui.view.viewModels.LoginViewModel
import org.bonygod.gymroutine.ui.view.viewModels.SharedViewModel
import org.bonygod.gymroutine.ui.view.viewModels.SignUpViewModel
import org.bonygod.gymroutine.ui.view.viewModels.UserProfileViewModel
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
    single { LoginUseCase() }
    single { SignUpUseCase() }
    single { GetUserUseCase() }
    factory { ForgotPasswordUseCase() }
    factory { SaveUserDataUseCase() }
    factory { ForgotPasswordViewModel() }
    factory { UserProfileViewModel() }
    single(named("API_KEY")) { BuildConfig.API_KEY }
    single(named("CLIENT_ID")) { BuildConfig.CLIENT_ID }
    single <RoomDb> { CreateDatabase(get()).getDatabase() }
}

val dataModule = module {
    factoryOf(::AuthenticationService)
    factoryOf(::UserDataService)
    factoryOf(::AuthRepository)
    singleOf(::UserRepository)
    singleOf(::UserDataRepository)
    singleOf(::InsertUserDaoUseCase)
    singleOf(::UpdateUserDaoUseCase)
    singleOf(::DeleteUserDaoUseCase)
    singleOf(::GetUserDaoUseCase)
    singleOf(::GetUserUseCase)
    singleOf(::ForgotPasswordUseCase)
}

val viewModelsModule = module {
    viewModelOf(::DialogViewModel)
    viewModelOf(::SharedViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::UserViewModel)
    viewModelOf(::ForgotPasswordViewModel)
    viewModelOf(::UserProfileViewModel)
}

//expect val nativeModule : Module

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(viewModelsModule, appModule, dataModule)
    }
}