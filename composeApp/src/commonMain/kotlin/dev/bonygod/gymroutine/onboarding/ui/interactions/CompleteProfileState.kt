package dev.bonygod.gymroutine.onboarding.ui.interactions

data class CompleteProfileState(
    val age: Int = 25,
    val height: Int = 170,
    val weight: Int = 70,
    val isSaving: Boolean = false,
) {
    fun setAge(value: Int) = copy(age = value)

    fun setHeight(value: Int) = copy(height = value)

    fun setWeight(value: Int) = copy(weight = value)

    fun showSaving(saving: Boolean) = copy(isSaving = saving)
}
