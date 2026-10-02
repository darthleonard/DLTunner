package com.darthleonard.dltunner.domain.usecase

import com.darthleonard.dltunner.domain.model.Tuning
import com.darthleonard.dltunner.domain.repository.TunerRepository

/**
 * Use case to retrieve available tuning presets.
 */
class GetTuningsUseCase(
    private val repository: TunerRepository
) {
    fun getTunings(): List<Tuning> = repository.getTunings()
    fun getDefaultTuning(): Tuning = repository.getDefaultTuning()
}
