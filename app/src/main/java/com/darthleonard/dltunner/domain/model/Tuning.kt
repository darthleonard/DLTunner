package com.darthleonard.dltunner.domain.model

import androidx.annotation.StringRes

/**
 * Represents a guitar tuning preset containing a set of strings.
 *
 * @property id Unique string identifier for the tuning preset.
 * @property nameResId String resource ID for the user-facing localized name.
 * @property strings List of guitar strings, ordered from string 6 down to string 1.
 */
data class Tuning(
    val id: String,
    @param:StringRes val nameResId: Int,
    val strings: List<GuitarString>
)
