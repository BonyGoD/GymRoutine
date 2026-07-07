package dev.bonygod.gymroutine.auth.domain.model

data class User(
    val uid: String,
    val name: String,
    val age: String,
    val weight: String,
    val height: String,
    val email: String,
)
