package dev.bonygod.gymroutine.auth.domain.usecase

import dev.bonygod.gymroutine.auth.domain.model.ExternalAuthCredential
import dev.bonygod.gymroutine.auth.domain.model.User
import dev.bonygod.gymroutine.auth.domain.repository.AuthRepository

/**
 * Handles sign-in via any external provider (Google, Apple, etc.).
 *
 * The SignInKMP library returns uid, displayName, email and an optional photoUrl
 * from its success callbacks. This use case normalises those values into an
 * [ExternalAuthCredential] and delegates to the repository, which will:
 *   - Return the existing [User] if the uid is already registered in Firestore.
 *   - Create and return a new [User] if this is the first time the account is seen.
 *
 * Adding support for a new provider in the future only requires calling this
 * use case with the values returned by that provider — no other changes needed.
 */
class LoginWithSocialProviderUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(
        uid: String,
        displayName: String,
        email: String,
        photoUrl: String? = null,
    ): Result<User> {
        val credential = ExternalAuthCredential(
            uid = uid,
            displayName = displayName,
            email = email,
            photoUrl = photoUrl,
        )
        return repository.loginWithExternalProvider(credential)
    }
}
