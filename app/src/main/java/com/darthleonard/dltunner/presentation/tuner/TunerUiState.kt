package com.darthleonard.dltunner.presentation.tuner

import com.darthleonard.dltunner.domain.model.GuitarString
import com.darthleonard.dltunner.domain.model.TunerState
import com.darthleonard.dltunner.domain.model.Tuning

/**
 * State representation for the main Tuner UI.
 */
data class TunerUiState(
    val selectedTuning: Tuning,
    val availableTunings: List<Tuning>,
    val detectedString: GuitarString? = null,
    val detectedNote: String? = null,
    val frequency: Double? = null,
    val cents: Double? = null,
    val state: TunerState = TunerState.NO_SIGNAL,
    val confidence: Double = 0.0,
    val isListening: Boolean = false,
    val isPermissionGranted: Boolean = false,
    val errorMessage: String? = null
)
