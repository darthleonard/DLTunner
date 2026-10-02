package com.darthleonard.dltunner.domain.usecase

import com.darthleonard.dltunner.core.detection.PitchDetectionResult
import com.darthleonard.dltunner.domain.model.GuitarString
import com.darthleonard.dltunner.domain.model.Tuning
import kotlin.math.abs

/**
 * Identifies the target guitar string based on detected frequency, confidence, and current tuning.
 * Implements hysteresis and noise rejection to maintain visual stability.
 */
class DetectStringUseCase(
    private val calculateCentsUseCase: CalculateCentsUseCase = CalculateCentsUseCase(),
    private val minConfidenceThreshold: Double = 0.40,
    private val maxDeviationCents: Double = 220.0,
    private val hysteresisMarginCents: Double = 60.0
) {

    /**
     * Finds the matching string for the detected pitch.
     *
     * @param result Detection result containing frequency and confidence.
     * @param tuning Currently active tuning configuration.
     * @param previousString The string detected in the previous frame, if any.
     * @return The identified [GuitarString] or null if no confident match was found.
     */
    operator fun invoke(
        result: PitchDetectionResult,
        tuning: Tuning,
        previousString: GuitarString? = null
    ): GuitarString? {
        if (!result.isPitchDetected || result.confidence < minConfidenceThreshold || result.frequency <= 0.0) {
            return null
        }

        val frequency = result.frequency

        // Find closest string in the current tuning based on absolute cents deviation
        var closestString: GuitarString? = null
        var minCentsDeviation = Double.MAX_VALUE

        for (string in tuning.strings) {
            val centsDev = abs(calculateCentsUseCase(frequency, string.targetFrequency))
            if (centsDev < minCentsDeviation) {
                minCentsDeviation = centsDev
                closestString = string
            }
        }

        if (closestString == null || minCentsDeviation > maxDeviationCents) {
            return null
        }

        // Apply hysteresis: if previous string is still within range, stay with it to avoid flicker
        if (previousString != null && tuning.strings.contains(previousString)) {
            val previousCentsDev = abs(calculateCentsUseCase(frequency, previousString.targetFrequency))
            if (previousCentsDev <= minCentsDeviation + hysteresisMarginCents && previousCentsDev <= maxDeviationCents) {
                return previousString
            }
        }

        return closestString
    }
}
