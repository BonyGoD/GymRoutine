package dev.bonygod.gymroutine.routines.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class RoutineDto(
    @Transient val id: String = "",
    val name: String = "",
    val exercises: List<ExerciseDto> = emptyList(),
)
