package com.darthleonard.dltunner.domain.repository

import com.darthleonard.dltunner.domain.model.Tuning

/**
 * Repository interface providing access to tuning presets.
 */
interface TunerRepository {
    fun getTunings(): List<Tuning>
    fun getDefaultTuning(): Tuning
}
