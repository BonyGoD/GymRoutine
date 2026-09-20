package dev.bonygod.gymroutine.tutorial.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.bonygod.gymroutine.auth.domain.usecase.GetCurrentUserUseCase
import dev.bonygod.gymroutine.auth.domain.usecase.MarkTutorialSeenUseCase
import dev.bonygod.gymroutine.core.config.DEVELOPER_EMAIL
import dev.bonygod.gymroutine.core.navigation.BottomTab
import dev.bonygod.gymroutine.core.navigation.Navigator
import dev.bonygod.gymroutine.core.navigation.Routes
import dev.bonygod.gymroutine.tutorial.ui.interactions.TutorialEvent
import dev.bonygod.gymroutine.tutorial.ui.interactions.TutorialState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TutorialViewModel(
    private val navigator: Navigator,
    private val getCurrentUser: GetCurrentUserUseCase,
    private val markTutorialSeen: MarkTutorialSeenUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(TutorialState())
    val state: StateFlow<TutorialState> = _state

    private var userId: String = ""

    init {
        viewModelScope.launch {
            getCurrentUser().onSuccess { user ->
                if (user != null && (!user.tutorialSeen || user.email == DEVELOPER_EMAIL)) {
                    userId = user.uid
                    setState { show() }
                    navigator.currentTab.value = _state.value.step.tab
                }
            }
        }
    }

    fun onEvent(event: TutorialEvent) {
        when (event) {
            is TutorialEvent.OnNext -> onNext()
            is TutorialEvent.OnSkip -> finish(BottomTab.Home)
        }
    }

    private fun onNext() {
        if (_state.value.isLastStep) {
            finish(BottomTab.Routines)
            navigator.navigateTo(Routes.AddRoutine)
        } else {
            setState { next() }
            navigator.currentTab.value = _state.value.step.tab
        }
    }

    private fun finish(tab: BottomTab) {
        setState { hide() }
        navigator.currentTab.value = tab
        viewModelScope.launch { markTutorialSeen(userId) }
    }

    private fun setState(reducer: TutorialState.() -> TutorialState) {
        _state.value = _state.value.reducer()
    }
}
