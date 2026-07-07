package dev.bonygod.gymroutine.profile.ui.interactions

data class ProfileState(
    val isLoading: Boolean = true,
    val userName: String = "",
    val userEmail: String = "",
    val totalWorkouts: Int = 0,
    val personalRecords: Int = 0,
    val streak: Int = 0,
) {
    fun setUser(name: String, email: String) = copy(userName = name, userEmail = email, isLoading = false)
    fun setStats(total: Int, records: Int, streak: Int) = copy(
        totalWorkouts = total,
        personalRecords = records,
        streak = streak,
    )
}
