package com.darthleonard.dltunner.domain.model

/**
 * Represents the state of the tuner relative to the target pitch.
 */
enum class TunerState {
    NO_SIGNAL,
    TOO_LOW,
    IN_TUNE,
    TOO_HIGH
}
