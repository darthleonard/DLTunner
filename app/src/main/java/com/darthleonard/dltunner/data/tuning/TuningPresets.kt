package com.darthleonard.dltunner.data.tuning

import com.darthleonard.dltunner.R
import com.darthleonard.dltunner.domain.model.GuitarString
import com.darthleonard.dltunner.domain.model.Tuning

/**
 * Contains predefined tuning configurations for guitar.
 */
object TuningPresets {

    val STANDARD = Tuning(
        id = "standard",
        nameResId = R.string.tuning_standard,
        strings = listOf(
            GuitarString(stringNumber = 6, displayName = "E", targetNote = "E2", targetFrequency = 82.41),
            GuitarString(stringNumber = 5, displayName = "A", targetNote = "A2", targetFrequency = 110.00),
            GuitarString(stringNumber = 4, displayName = "D", targetNote = "D3", targetFrequency = 146.83),
            GuitarString(stringNumber = 3, displayName = "G", targetNote = "G3", targetFrequency = 196.00),
            GuitarString(stringNumber = 2, displayName = "B", targetNote = "B3", targetFrequency = 246.94),
            GuitarString(stringNumber = 1, displayName = "E", targetNote = "E4", targetFrequency = 329.63)
        )
    )

    val DROP_D = Tuning(
        id = "drop_d",
        nameResId = R.string.tuning_drop_d,
        strings = listOf(
            GuitarString(stringNumber = 6, displayName = "D", targetNote = "D2", targetFrequency = 73.42),
            GuitarString(stringNumber = 5, displayName = "A", targetNote = "A2", targetFrequency = 110.00),
            GuitarString(stringNumber = 4, displayName = "D", targetNote = "D3", targetFrequency = 146.83),
            GuitarString(stringNumber = 3, displayName = "G", targetNote = "G3", targetFrequency = 196.00),
            GuitarString(stringNumber = 2, displayName = "B", targetNote = "B3", targetFrequency = 246.94),
            GuitarString(stringNumber = 1, displayName = "E", targetNote = "E4", targetFrequency = 329.63)
        )
    )

    val EB_STANDARD = Tuning(
        id = "eb_standard",
        nameResId = R.string.tuning_eb_standard,
        strings = listOf(
            GuitarString(stringNumber = 6, displayName = "E♭", targetNote = "E♭2", targetFrequency = 77.78),
            GuitarString(stringNumber = 5, displayName = "A♭", targetNote = "A♭2", targetFrequency = 103.83),
            GuitarString(stringNumber = 4, displayName = "D♭", targetNote = "D♭3", targetFrequency = 138.59),
            GuitarString(stringNumber = 3, displayName = "G♭", targetNote = "G♭3", targetFrequency = 185.00),
            GuitarString(stringNumber = 2, displayName = "B♭", targetNote = "B♭3", targetFrequency = 233.08),
            GuitarString(stringNumber = 1, displayName = "E♭", targetNote = "E♭4", targetFrequency = 311.13)
        )
    )

    val D_STANDARD = Tuning(
        id = "d_standard",
        nameResId = R.string.tuning_d_standard,
        strings = listOf(
            GuitarString(stringNumber = 6, displayName = "D", targetNote = "D2", targetFrequency = 73.42),
            GuitarString(stringNumber = 5, displayName = "G", targetNote = "G2", targetFrequency = 98.00),
            GuitarString(stringNumber = 4, displayName = "C", targetNote = "C3", targetFrequency = 130.81),
            GuitarString(stringNumber = 3, displayName = "F", targetNote = "F3", targetFrequency = 174.61),
            GuitarString(stringNumber = 2, displayName = "A", targetNote = "A3", targetFrequency = 220.00),
            GuitarString(stringNumber = 1, displayName = "D", targetNote = "D4", targetFrequency = 293.66)
        )
    )

    val ALL_TUNINGS = listOf(STANDARD, DROP_D, EB_STANDARD, D_STANDARD)
}
