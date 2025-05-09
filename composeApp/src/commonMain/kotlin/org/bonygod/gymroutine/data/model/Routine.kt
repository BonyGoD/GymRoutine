package org.bonygod.gymroutine.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Routine(
    val id: String,
    val type: String,
    val day: String,
    val exercises: List<Exercice>,
)