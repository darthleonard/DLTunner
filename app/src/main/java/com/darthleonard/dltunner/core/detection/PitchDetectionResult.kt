package com.darthleonard.dltunner.core.detection

/**
 * Result of pitch detection execution.
 *
 * @property frequency Detected fundamental frequency in Hertz.
 * @property confidence Detection confidence score from 0.0 (no confidence) to 1.0 (highest confidence).
 * @property isPitchDetected True if a valid pitch was detected above signal noise threshold.
 */
data class PitchDetectionResult(
    val frequency: Double,
    val confidence: Double,
    val isPitchDetected: Boolean
) {
    companion object {
        val NO_PITCH = PitchDetectionResult(
            frequency = 0.0,
            confidence = 0.0,
            isPitchDetected = false
        )
    }
}
