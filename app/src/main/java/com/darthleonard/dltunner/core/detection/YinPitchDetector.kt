package com.darthleonard.dltunner.core.detection

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

/**
 * Robust pitch detection algorithm based on the YIN method.
 * Optimized for monophonic audio signals such as guitar strings.
 *
 * @property threshold Primary YIN absolute difference threshold (default 0.15).
 * @property minFrequency Minimum frequency in Hz to search for (default 60.0 Hz).
 * @property maxFrequency Maximum frequency in Hz to search for (default 500.0 Hz).
 * @property noiseFloor Rms energy threshold below which signal is considered silence.
 */
class YinPitchDetector(
    private val threshold: Double = 0.15,
    private val minFrequency: Double = 60.0,
    private val maxFrequency: Double = 500.0,
    private val noiseFloor: Double = 0.01,
) : PitchDetector {

    override suspend fun detect(samples: FloatArray, sampleRate: Int): PitchDetectionResult {
        if (samples.size < 512) {
            return PitchDetectionResult.NO_PITCH
        }

        // 1. RMS Energy check for noise gate
        var sumSquares = 0.0
        for (sample in samples) {
            sumSquares += sample * sample
        }
        val rms = sqrt(sumSquares / samples.size)
        if (rms < noiseFloor) {
            return PitchDetectionResult.NO_PITCH
        }

        // Calculate lag bounds
        val tauMin = max(2, (sampleRate / maxFrequency).toInt())
        val tauMax = min(samples.size / 2, (sampleRate / minFrequency).toInt())

        if (tauMin >= tauMax) {
            return PitchDetectionResult.NO_PITCH
        }

        // 2. Difference function d(tau)
        val windowSize = samples.size - tauMax
        val yinBuffer = DoubleArray(tauMax + 1)

        for (tau in 1..tauMax) {
            var deltaSum = 0.0
            for (i in 0 until windowSize) {
                val diff = (samples[i] - samples[i + tau]).toDouble()
                deltaSum += diff * diff
            }
            yinBuffer[tau] = deltaSum
        }

        // 3. Cumulative mean normalized difference function (CMNDF)
        yinBuffer[0] = 1.0
        var runningSum = 0.0
        for (tau in 1..tauMax) {
            runningSum += yinBuffer[tau]
            yinBuffer[tau] = if (runningSum > 0) (yinBuffer[tau] * tau) / runningSum else 1.0
        }

        // 4. Absolute thresholding
        var selectedTau = -1
        for (tau in tauMin..tauMax) {
            if (yinBuffer[tau] < threshold) {
                selectedTau = tau
                // Find the local minimum
                while ((selectedTau + 1 <= tauMax) && (yinBuffer[selectedTau + 1] < yinBuffer[selectedTau])) {
                    selectedTau++
                }
                break
            }
        }

        // Fallback to global minimum if no lag fell below threshold
        if (selectedTau == -1) {
            var minVal = Double.MAX_VALUE
            for (tau in tauMin..tauMax) {
                if (yinBuffer[tau] < minVal) {
                    minVal = yinBuffer[tau]
                    selectedTau = tau
                }
            }
            // Reject if confidence is too low
            if (minVal > 0.45) {
                return PitchDetectionResult.NO_PITCH
            }
        }

        if (selectedTau !in 1 until tauMax) {
            return PitchDetectionResult.NO_PITCH
        }

        // 5. Parabolic interpolation
        val x0 = max(1, selectedTau - 1)
        val x2 = min(tauMax, selectedTau + 1)

        val s0 = yinBuffer[x0]
        val s1 = yinBuffer[selectedTau]
        val s2 = yinBuffer[x2]

        val denominator = 2 * s0 - 4 * s1 + 2 * s2
        val delta = if (abs(denominator) > 1e-6) {
            (s0 - s2) / denominator
        } else {
            0.0
        }

        val betterTau = selectedTau + delta
        val interpolatedYinVal = s1 - (delta * (s0 - s2) / 4.0)

        val frequency = sampleRate / betterTau
        val confidence = (1.0 - interpolatedYinVal).coerceIn(0.0, 1.0)

        if (frequency !in minFrequency..maxFrequency) {
            return PitchDetectionResult.NO_PITCH
        }

        return PitchDetectionResult(
            frequency = frequency,
            confidence = confidence,
            isPitchDetected = true,
        )
    }
}
