package com.darthleonard.dltunner.domain.model

/**
 * Represents detected pitch information.
 *
 * @property frequency Fundamental frequency in Hz.
 * @property note Note name representation.
 * @property cents Cents deviation from target note.
 * @property confidence Confidence score between 0.0 and 1.0.
 */
data class Pitch(
    val frequency: Double,
    val note: String,
    val cents: Double,
    val confidence: Double
)
