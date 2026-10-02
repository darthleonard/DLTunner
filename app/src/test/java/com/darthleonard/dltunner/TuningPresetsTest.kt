package com.darthleonard.dltunner

import com.darthleonard.dltunner.data.tuning.TuningPresets
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TuningPresetsTest {

    @Test
    fun `all tuning presets contain six valid strings ordered 6 to 1`() {
        val tunings = TuningPresets.ALL_TUNINGS
        assertEquals(4, tunings.size)

        tunings.forEach { tuning ->
            assertEquals(6, tuning.strings.size)
            val numbers = tuning.strings.map { it.stringNumber }
            assertEquals(listOf(6, 5, 4, 3, 2, 1), numbers)

            // Frequencies must be strictly ascending from string 6 to string 1
            for (i in 0 until tuning.strings.size - 1) {
                assertTrue(
                    "String ${tuning.strings[i].stringNumber} target frequency should be lower than string ${tuning.strings[i + 1].stringNumber}",
                    tuning.strings[i].targetFrequency < tuning.strings[i + 1].targetFrequency
                )
            }
        }
    }
}
