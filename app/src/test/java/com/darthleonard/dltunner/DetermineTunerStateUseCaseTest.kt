package com.darthleonard.dltunner

import com.darthleonard.dltunner.domain.model.TunerState
import com.darthleonard.dltunner.domain.usecase.DetermineTunerStateUseCase
import org.junit.Assert.assertEquals
import org.junit.Test

class DetermineTunerStateUseCaseTest {

    private val determineState = DetermineTunerStateUseCase(inTuneToleranceCents = 3.0)

    @Test
    fun `null cents or invalid signal returns NO_SIGNAL`() {
        assertEquals(TunerState.NO_SIGNAL, determineState(cents = null, isSignalValid = true))
        assertEquals(TunerState.NO_SIGNAL, determineState(cents = 0.0, isSignalValid = false))
    }

    @Test
    fun `cents within tolerance returns IN_TUNE`() {
        assertEquals(TunerState.IN_TUNE, determineState(cents = 0.0, isSignalValid = true))
        assertEquals(TunerState.IN_TUNE, determineState(cents = 2.5, isSignalValid = true))
        assertEquals(TunerState.IN_TUNE, determineState(cents = -2.9, isSignalValid = true))
    }

    @Test
    fun `cents less than negative tolerance returns TOO_LOW`() {
        assertEquals(TunerState.TOO_LOW, determineState(cents = -3.1, isSignalValid = true))
        assertEquals(TunerState.TOO_LOW, determineState(cents = -25.0, isSignalValid = true))
    }

    @Test
    fun `cents greater than positive tolerance returns TOO_HIGH`() {
        assertEquals(TunerState.TOO_HIGH, determineState(cents = 3.5, isSignalValid = true))
        assertEquals(TunerState.TOO_HIGH, determineState(cents = 40.0, isSignalValid = true))
    }
}
