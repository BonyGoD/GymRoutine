package dev.bonygod.gymroutine.auth.domain.model

data class User(
    val uid: String,
    val name: String,
    val age: String,
    val weight: String,
    val height: String,
    val email: String,
)

/**
 * El perfil está completo cuando edad, peso y altura tienen valor. Sin ellos la app no puede
 * estimar calorías ni contextualizar la evolución, así que se piden antes de entrar a la home.
 */
fun User.hasCompleteProfile(): Boolean = age.isNotBlank() && weight.isNotBlank() && height.isNotBlank()
