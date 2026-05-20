package dev.bonygod.gymroutine.core.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList

class Navigator {
    private val _backStack: SnapshotStateList<Any> = mutableStateListOf(Routes.Login)

    val backStack: SnapshotStateList<Any>
        get() = _backStack

    var currentTab = mutableStateOf(BottomTab.Home)

    fun navigateTo(route: Routes) {
        _backStack.add(route)
    }

    fun goBack() {
        if (_backStack.size > 1) {
            _backStack.removeLastOrNull()
        }
    }

    fun replaceTo(route: Routes) {
        if (_backStack.isNotEmpty()) _backStack.removeLastOrNull()
        _backStack.add(route)
    }

    fun clearAndNavigateTo(route: Routes) {
        _backStack.clear()
        _backStack.add(route)
    }
}
