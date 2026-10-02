package com.darthleonard.dltunner

import com.darthleonard.dltunner.domain.usecase.CalculateCentsUseCase
import org.junit.Assert.assertEquals
import org.junit.Test

class CalculateCentsUseCaseTest {

    private val calculateCentsUseCase = CalculateCentsUseCase()

    @Test
    fun `invoke with identical frequencies returns zero cents`() {
        val cents = calculateCentsUseCase(440.0, 440.0)
        assertEquals(0.0, cents, 1e-4)
    }

    @Test
    fun `invoke with octave higher frequency returns 1200 cents`() {
        val cents = calculateCentsUseCase(880.0, 440.0)
        assertEquals(1200.0, cents, 1e-4)
    }

    @Test
    fun `invoke with octave lower frequency returns -1200 cents`() {
        val cents = calculateCentsUseCase(220.0, 440.0)
        assertEquals(-1200.0, cents, 1e-4)
    }

    @Test
    fun `invoke with sharp frequency returns positive cents`() {
        val cents = calculateCentsUseCase(112.0, 110.0)
        assertEquals(31.13, cents, 0.5)
    }

    @Test
    fun `invoke with flat frequency returns negative cents`() {
        val cents = calculateCentsUseCase(108.0, 110.0)
        assertEquals(-31.7, cents, 0.5)
    }

    @Test
    fun `invoke with zero or negative frequency returns zero`() {
        assertEquals(0.0, calculateCentsUseCase(0.0, 110.0), 1e-4)
        assertEquals(0.0, calculateCentsUseCase(110.0, 0.0), 1e-4)
        assertEquals(0.0, calculateCentsUseCase(-10.0, 110.0), 1e-4)
    }
}
