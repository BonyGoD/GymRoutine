package dev.bonygod.gymroutine.core.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import dev.bonygod.crashlytics.kmp.core.CrashlyticsKMP

class Navigator {
    private val _backStack: SnapshotStateList<Any> = mutableStateListOf(Routes.Login)

    val backStack: SnapshotStateList<Any>
        get() = _backStack

    var currentTab = mutableStateOf(BottomTab.Home)

    fun navigateTo(route: Routes) {
        logNavigation(route)
        _backStack.add(route)
    }

    fun goBack() {
        if (_backStack.size > 1) {
            _backStack.removeLastOrNull()
            CrashlyticsKMP.reporter.log("Navigate back to ${_backStack.lastOrNull()?.let { it::class.simpleName }}")
        }
    }

    fun replaceTo(route: Routes) {
        if (_backStack.isNotEmpty()) _backStack.removeLastOrNull()
        logNavigation(route)
        _backStack.add(route)
    }

    fun clearAndNavigateTo(route: Routes) {
        _backStack.clear()
        logNavigation(route)
        _backStack.add(route)
        if (route is Routes.Login || route is Routes.Main) {
            currentTab.value = BottomTab.Home
        }
    }

    /**
     * Deja una miga en Crashlytics con la pantalla a la que se navega. Solo el nombre de la ruta:
     * algunas, como [Routes.Workout], llevan datos del usuario en sus parámetros (el nombre de la
     * rutina) que no deben acabar en un log.
     */
    private fun logNavigation(route: Routes) {
        CrashlyticsKMP.reporter.log("Navigate to ${route::class.simpleName}")
    }
}
