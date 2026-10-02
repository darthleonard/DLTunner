package com.darthleonard.dltunner

import com.darthleonard.dltunner.core.detection.PitchDetectionResult
import com.darthleonard.dltunner.data.tuning.TuningPresets
import com.darthleonard.dltunner.domain.usecase.DetectStringUseCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DetectStringUseCaseTest {

    private val detectStringUseCase = DetectStringUseCase()
    private val standardTuning = TuningPresets.STANDARD

    @Test
    fun `detects correct strings in standard tuning`() {
        val lowE = detectStringUseCase(
            result = PitchDetectionResult(frequency = 82.41, confidence = 0.9, isPitchDetected = true),
            tuning = standardTuning
        )
        assertEquals(6, lowE?.stringNumber)
        assertEquals("E", lowE?.displayName)

        val stringA = detectStringUseCase(
            result = PitchDetectionResult(frequency = 110.0, confidence = 0.9, isPitchDetected = true),
            tuning = standardTuning
        )
        assertEquals(5, stringA?.stringNumber)
        assertEquals("A", stringA?.displayName)

        val highE = detectStringUseCase(
            result = PitchDetectionResult(frequency = 329.63, confidence = 0.9, isPitchDetected = true),
            tuning = standardTuning
        )
        assertEquals(1, highE?.stringNumber)
        assertEquals("E", highE?.displayName)
    }

    @Test
    fun `returns null on low confidence or no pitch`() {
        val lowConf = detectStringUseCase(
            result = PitchDetectionResult(frequency = 110.0, confidence = 0.2, isPitchDetected = true),
            tuning = standardTuning
        )
        assertNull(lowConf)

        val noPitch = detectStringUseCase(
            result = PitchDetectionResult.NO_PITCH,
            tuning = standardTuning
        )
        assertNull(noPitch)
    }

    @Test
    fun `applies hysteresis to retain previous string when near boundary`() {
        val stringA = standardTuning.strings.first { it.stringNumber == 5 }

        // Slightly shifted frequency closer to A2
        val detected = detectStringUseCase(
            result = PitchDetectionResult(frequency = 105.0, confidence = 0.8, isPitchDetected = true),
            tuning = standardTuning,
            previousString = stringA
        )

        assertEquals(5, detected?.stringNumber)
    }
}
