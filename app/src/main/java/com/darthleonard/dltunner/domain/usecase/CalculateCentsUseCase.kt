package com.darthleonard.dltunner.domain.usecase

import kotlin.math.log2

/**
 * Calculates logarithmic cents deviation between detected frequency and target frequency.
 *
 * Formula: cents = 1200 * log2(detectedFrequency / targetFrequency)
 */
class CalculateCentsUseCase {

    /**
     * Executes the cents calculation.
     *
     * @param detectedFrequency The frequency in Hz detected from audio input.
     * @param targetFrequency The target note frequency in Hz.
     * @return Cents deviation. Negative values indicate flat (too low), positive indicate sharp (too high).
     */
    operator fun invoke(detectedFrequency: Double, targetFrequency: Double): Double {
        if (detectedFrequency <= 0.0 || targetFrequency <= 0.0) {
            return 0.0
        }
        return 1200.0 * log2(detectedFrequency / targetFrequency)
    }
}
