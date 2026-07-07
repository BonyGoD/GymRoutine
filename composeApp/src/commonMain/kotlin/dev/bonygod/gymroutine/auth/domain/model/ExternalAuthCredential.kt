package dev.bonygod.gymroutine.auth.domain.model

/**
 * Normalized credential produced by any external auth provider (Google, Apple, etc.).
 * Adapt the provider-specific result to this model before calling the repository.
 */
data class ExternalAuthCredential(
    val uid: String,
    val email: String,
    val displayName: String,
    val photoUrl: String? = null,
)
