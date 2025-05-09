package org.bonygod.gymroutine.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Exercice (
    val name: String,
    val repetitions: Int,
    val sets: Int,
    val rest: Int
)