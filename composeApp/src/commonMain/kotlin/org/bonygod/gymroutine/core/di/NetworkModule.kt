package org.bonygod.gymroutine.core.di

import org.bonygod.gymroutine.ui.view.viewModels.DialogViewModel
import org.bonygod.gymroutine.ui.view.viewModels.SharedViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val appModule = module {
    single { DialogViewModel() }
    single { SharedViewModel() }
}

val dataModule = module {
    //factoryOf(::Repository)
}

val viewModelsModule = module {
    viewModelOf(::DialogViewModel)
    viewModelOf(::SharedViewModel)
}

//expect val nativeModule : Module

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(viewModelsModule, appModule)
    }
}