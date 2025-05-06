package org.bonygod.gymroutine.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Routine(
    val id: String ,
    val type: String,
    val repetitions: Int,
    val sets: Int,
    val rest: Int
)