package dev.bonygod.gymroutine.routines.domain.model

data class Routine(
    val id: String,
    val name: String,
    val days: String,
    val exercises: List<Exercise>,
)
