package dev.bonygod.gymroutine.di

import dev.bonygod.gymroutine.BuildConfig
import dev.bonygod.gymroutine.auth.data.datasource.AuthRemoteDataSource
import dev.bonygod.gymroutine.auth.data.datasource.AuthRemoteDataSourceImpl
import dev.bonygod.gymroutine.auth.data.repository.AuthRepositoryImpl
import dev.bonygod.gymroutine.auth.domain.repository.AuthRepository
import dev.bonygod.gymroutine.auth.domain.usecase.GetCurrentUserUseCase
import dev.bonygod.gymroutine.auth.domain.usecase.LoginUseCase
import dev.bonygod.gymroutine.auth.domain.usecase.LoginWithSocialProviderUseCase
import dev.bonygod.gymroutine.auth.domain.usecase.LogoutUseCase
import dev.bonygod.gymroutine.auth.domain.usecase.RegisterUseCase
import dev.bonygod.gymroutine.auth.domain.usecase.SendPasswordResetUseCase
import dev.bonygod.gymroutine.auth.ui.AuthViewModel
import dev.bonygod.gymroutine.core.navigation.Navigator
import dev.bonygod.gymroutine.history.ui.HistoryViewModel
import dev.bonygod.gymroutine.home.ui.HomeViewModel
import dev.bonygod.gymroutine.profile.ui.ProfileViewModel
import dev.bonygod.gymroutine.routines.data.datasource.RoutineRemoteDataSource
import dev.bonygod.gymroutine.routines.data.datasource.RoutineRemoteDataSourceImpl
import dev.bonygod.gymroutine.routines.data.repository.RoutineRepositoryImpl
import dev.bonygod.gymroutine.routines.domain.repository.RoutineRepository
import dev.bonygod.gymroutine.routines.domain.usecase.CreateRoutineUseCase
import dev.bonygod.gymroutine.routines.domain.usecase.DeleteRoutineUseCase
import dev.bonygod.gymroutine.routines.domain.usecase.GetRoutinesUseCase
import dev.bonygod.gymroutine.routines.domain.usecase.ObserveRoutinesUseCase
import dev.bonygod.gymroutine.routines.domain.usecase.UpdateRoutineUseCase
import dev.bonygod.gymroutine.routines.ui.RoutinesViewModel
import dev.bonygod.gymroutine.workout.data.datasource.WorkoutLogRemoteDataSource
import dev.bonygod.gymroutine.workout.data.datasource.WorkoutLogRemoteDataSourceImpl
import dev.bonygod.gymroutine.workout.data.repository.WorkoutLogRepositoryImpl
import dev.bonygod.gymroutine.workout.domain.repository.WorkoutLogRepository
import dev.bonygod.gymroutine.workout.domain.usecase.LogWorkoutUseCase
import dev.bonygod.gymroutine.workout.domain.usecase.ObserveWorkoutLogsUseCase
import dev.bonygod.gymroutine.workout.ui.WorkoutViewModel
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
    factory { GetCurrentUserUseCase(get()) }

    // Routines data layer
    single<RoutineRemoteDataSource> { RoutineRemoteDataSourceImpl(get()) }
    single<RoutineRepository> { RoutineRepositoryImpl(get()) }

    // Routines use cases
    factory { GetRoutinesUseCase(get()) }
    factory { ObserveRoutinesUseCase(get()) }
    factory { CreateRoutineUseCase(get()) }
    factory { UpdateRoutineUseCase(get()) }
    factory { DeleteRoutineUseCase(get()) }

    // Workout logs data layer
    single<WorkoutLogRemoteDataSource> { WorkoutLogRemoteDataSourceImpl(get()) }
    single<WorkoutLogRepository> { WorkoutLogRepositoryImpl(get()) }

    // Workout logs use cases
    factory { LogWorkoutUseCase(get()) }
    factory { ObserveWorkoutLogsUseCase(get()) }

    // ViewModels
    viewModel { AuthViewModel(get(), get(), get(), get(), get()) }
    viewModel { HomeViewModel(get(), get(), get(), get()) }
    viewModel { HistoryViewModel(get(), get()) }
    viewModel { ProfileViewModel(get(), get(), get(), get(), get()) }
    viewModel { RoutinesViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { WorkoutViewModel(get(), get(), get(), get(), get()) }
}

fun initKoin(config: KoinApplication.() -> Unit = {}) {
    startKoin {
        config()
        modules(appModule)
    }
}
