package com.darthleonard.dltunner.domain.model

/**
 * Represents a single guitar string within a tuning configuration.
 *
 * @property stringNumber The string number (1 to 6, where 6 is the lowest pitch / thickest string).
 * @property displayName The display letter/note for the string (e.g. "E", "A", "D", "G", "B").
 * @property targetNote The full scientific pitch name (e.g. "E2", "A2", "D3").
 * @property targetFrequency The exact target fundamental frequency in Hertz.
 */
data class GuitarString(
    val stringNumber: Int,
    val displayName: String,
    val targetNote: String,
    val targetFrequency: Double
)
