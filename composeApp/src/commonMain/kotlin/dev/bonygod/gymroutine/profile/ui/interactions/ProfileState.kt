package dev.bonygod.gymroutine.profile.ui.interactions

/**
 * Los mismos valores por defecto que el onboarding (`CompleteProfileState`): si un usuario
 * anterior a la 0.0.10 no tiene edad/altura/peso guardados, la edición debe arrancar en 25/170/70.
 */
private const val DEFAULT_AGE = 25
private const val DEFAULT_HEIGHT = 170
private const val DEFAULT_WEIGHT = 70

data class ProfileState(
    val isLoading: Boolean = true,
    val userName: String = "",
    val userEmail: String = "",
    val totalWorkouts: Int = 0,
    val personalRecords: Int = 0,
    val streak: Int = 0,
    val age: String = "",
    val weight: String = "",
    val height: String = "",
    val isEditingProfileData: Boolean = false,
    val isSavingProfileData: Boolean = false,
    val editingAge: Int = DEFAULT_AGE,
    val editingHeight: Int = DEFAULT_HEIGHT,
    val editingWeight: Int = DEFAULT_WEIGHT,
) {
    fun setUser(name: String, email: String) = copy(userName = name, userEmail = email, isLoading = false)
    fun setStats(total: Int, records: Int, streak: Int) = copy(
        totalWorkouts = total,
        personalRecords = records,
        streak = streak,
    )

    fun setProfileData(age: String, weight: String, height: String) = copy(
        age = age,
        weight = weight,
        height = height,
    )

    fun startEditingProfileData() = copy(
        isEditingProfileData = true,
        editingAge = age.toIntOrNull() ?: DEFAULT_AGE,
        editingHeight = height.toIntOrNull() ?: DEFAULT_HEIGHT,
        editingWeight = weight.toIntOrNull() ?: DEFAULT_WEIGHT,
    )

    fun dismissEditingProfileData() = copy(isEditingProfileData = false, isSavingProfileData = false)

    fun setEditingAge(value: Int) = copy(editingAge = value)

    fun setEditingHeight(value: Int) = copy(editingHeight = value)

    fun setEditingWeight(value: Int) = copy(editingWeight = value)

    fun setSavingProfileData(saving: Boolean) = copy(isSavingProfileData = saving)
}
