package com.darthleonard.dltunner.data.repository

import com.darthleonard.dltunner.data.tuning.TuningPresets
import com.darthleonard.dltunner.domain.model.Tuning
import com.darthleonard.dltunner.domain.repository.TunerRepository

/**
 * Default implementation of [TunerRepository].
 */
class TunerRepositoryImpl : TunerRepository {
    override fun getTunings(): List<Tuning> = TuningPresets.ALL_TUNINGS
    override fun getDefaultTuning(): Tuning = TuningPresets.STANDARD
}
