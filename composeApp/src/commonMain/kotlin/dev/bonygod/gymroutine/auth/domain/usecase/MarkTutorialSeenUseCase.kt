package dev.bonygod.gymroutine.auth.domain.usecase

import dev.bonygod.gymroutine.auth.domain.repository.AuthRepository

class MarkTutorialSeenUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(uid: String): Result<Unit> = repository.markTutorialSeen(uid = uid)
}
