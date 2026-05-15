package org.bonygod.gymroutine.screens.detail

import androidx.lifecycle.ViewModel
import org.bonygod.gymroutine.data.MuseumObject
import org.bonygod.gymroutine.data.MuseumRepository
import kotlinx.coroutines.flow.Flow

class DetailViewModel(private val museumRepository: MuseumRepository) : ViewModel() {
    fun getObject(objectId: Int): Flow<MuseumObject?> =
        museumRepository.getObjectById(objectId)
}
