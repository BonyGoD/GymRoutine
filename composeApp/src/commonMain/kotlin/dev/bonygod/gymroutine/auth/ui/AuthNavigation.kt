package dev.bonygod.gymroutine.auth.ui

import dev.bonygod.gymroutine.auth.domain.model.User
import dev.bonygod.gymroutine.auth.domain.model.hasCompleteProfile
import dev.bonygod.gymroutine.core.navigation.Navigator
import dev.bonygod.gymroutine.core.navigation.Routes

internal fun Navigator.navigateAfterAuth(user: User) {
    if (user.hasCompleteProfile()) {
        clearAndNavigateTo(Routes.Main(user.uid))
    } else {
        clearAndNavigateTo(Routes.CompleteProfile(user.uid))
    }
}
