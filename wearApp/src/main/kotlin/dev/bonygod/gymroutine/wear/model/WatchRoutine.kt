package dev.bonygod.gymroutine.wear.model

data class WatchRoutine(
    val id: String,
    val name: String,
    val days: List<String>,
    val exercises: List<WatchExercise>,
    val lastCompletedOn: String? = null,
)
