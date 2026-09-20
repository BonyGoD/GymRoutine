package dev.bonygod.gymroutine.tutorial.ui.interactions

import dev.bonygod.gymroutine.tutorial.ui.model.TutorialStep

data class TutorialState(
    val isVisible: Boolean = false,
    val stepIndex: Int = 0,
) {
    val step: TutorialStep get() = TutorialStep.entries[stepIndex]
    val isLastStep: Boolean get() = stepIndex == TutorialStep.entries.lastIndex

    fun show() = copy(isVisible = true, stepIndex = 0)
    fun next() = copy(stepIndex = (stepIndex + 1).coerceAtMost(TutorialStep.entries.lastIndex))
    fun hide() = copy(isVisible = false)
}
