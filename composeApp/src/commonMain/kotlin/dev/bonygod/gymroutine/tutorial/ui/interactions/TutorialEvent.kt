package dev.bonygod.gymroutine.tutorial.ui.interactions

sealed class TutorialEvent {
    data object OnNext : TutorialEvent()
    data object OnSkip : TutorialEvent()
}
