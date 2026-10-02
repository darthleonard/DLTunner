package com.darthleonard.dltunner.domain.usecase

import com.darthleonard.dltunner.domain.model.TunerState
import kotlin.math.abs

/**
 * Determines the [TunerState] based on cents deviation and tolerance threshold.
 *
 * @property inTuneToleranceCents Tolerance margin in cents for considering pitch "In Tune" (default 3.0).
 */
class DetermineTunerStateUseCase(
    private val inTuneToleranceCents: Double = 3.0
) {

    /**
     * Computes the tuning state.
     *
     * @param cents Deviation in cents, or null if no valid note was detected.
     * @param isSignalValid Whether the input signal is active and reliable.
     * @return [TunerState] state classification.
     */
    operator fun invoke(cents: Double?, isSignalValid: Boolean): TunerState {
        if (!isSignalValid || cents == null) {
            return TunerState.NO_SIGNAL
        }

        return when {
            abs(cents) <= inTuneToleranceCents -> TunerState.IN_TUNE
            cents < -inTuneToleranceCents -> TunerState.TOO_LOW
            else -> TunerState.TOO_HIGH
        }
    }
}
