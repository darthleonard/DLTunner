package com.darthleonard.dltunner.presentation.tuner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darthleonard.dltunner.core.audio.AudioCapture
import com.darthleonard.dltunner.core.audio.AudioCaptureImpl
import com.darthleonard.dltunner.core.detection.PitchDetector
import com.darthleonard.dltunner.core.detection.YinPitchDetector
import com.darthleonard.dltunner.data.repository.TunerRepositoryImpl
import com.darthleonard.dltunner.domain.model.GuitarString
import com.darthleonard.dltunner.domain.model.TunerState
import com.darthleonard.dltunner.domain.model.Tuning
import com.darthleonard.dltunner.domain.repository.TunerRepository
import com.darthleonard.dltunner.domain.usecase.CalculateCentsUseCase
import com.darthleonard.dltunner.domain.usecase.DetectStringUseCase
import com.darthleonard.dltunner.domain.usecase.DetermineTunerStateUseCase
import com.darthleonard.dltunner.domain.usecase.GetTuningsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel managing tuner logic, audio pipeline, and UI state.
 */
class TunerViewModel(
    private val audioCapture: AudioCapture = AudioCaptureImpl(),
    private val pitchDetector: PitchDetector = YinPitchDetector(),
    repository: TunerRepository = TunerRepositoryImpl(),
    getTuningsUseCase: GetTuningsUseCase = GetTuningsUseCase(repository),
    private val detectStringUseCase: DetectStringUseCase = DetectStringUseCase(),
    private val calculateCentsUseCase: CalculateCentsUseCase = CalculateCentsUseCase(),
    private val determineTunerStateUseCase: DetermineTunerStateUseCase = DetermineTunerStateUseCase(),
) : ViewModel() {

    private val availableTunings = getTuningsUseCase.getTunings()
    private val defaultTuning = getTuningsUseCase.getDefaultTuning()

    private val _uiState = MutableStateFlow(
        TunerUiState(
            selectedTuning = defaultTuning,
            availableTunings = availableTunings,
        )
    )
    val uiState: StateFlow<TunerUiState> = _uiState.asStateFlow()

    private var audioJob: Job? = null
    private var noSignalCount = 0
    // Keep pitch reading displayed for ~3 seconds (~32 buffer chunks of ~93ms each) after string decays
    private val noSignalHoldChunks = 32
    private var smoothedCents: Double? = null
    private var smoothedFrequency: Double? = null
    private var currentDetectedString: GuitarString? = null

    fun onPermissionResult(isGranted: Boolean) {
        _uiState.update { it.copy(isPermissionGranted = isGranted) }
        if (isGranted) {
            startListening()
        } else {
            stopListening()
        }
    }

    fun selectTuning(tuning: Tuning) {
        _uiState.update {
            it.copy(
                selectedTuning = tuning,
                detectedString = null,
                detectedNote = null,
                frequency = null,
                cents = null,
                state = TunerState.NO_SIGNAL,
                stringStates = emptyMap()
            )
        }
        resetSmoothing()
    }

    fun startListening() {
        if (audioJob?.isActive == true) return

        _uiState.update { it.copy(isListening = true, errorMessage = null) }

        audioJob = viewModelScope.launch {
            audioCapture.startCapture()
                .catch { error ->
                    _uiState.update {
                        it.copy(
                            isListening = false,
                            errorMessage = error.localizedMessage
                        )
                    }
                }
                .collect { pcmSamples ->
                    processAudioChunk(pcmSamples)
                }
        }
    }

    fun stopListening() {
        audioJob?.cancel()
        audioJob = null
        audioCapture.stopCapture()
        _uiState.update {
            it.copy(
                isListening = false,
                detectedString = null,
                detectedNote = null,
                frequency = null,
                cents = null,
                state = TunerState.NO_SIGNAL
            )
        }
        resetSmoothing()
    }

    private suspend fun processAudioChunk(pcmSamples: FloatArray) {
        val detectionResult = pitchDetector.detect(pcmSamples, sampleRate = 44100)
        val tuning = _uiState.value.selectedTuning

        val detectedString = detectStringUseCase(
            result = detectionResult,
            tuning = tuning,
            previousString = currentDetectedString
        )

        if ((detectedString != null) && detectionResult.isPitchDetected) {
            noSignalCount = 0
            currentDetectedString = detectedString

            val rawCents = calculateCentsUseCase(
                detectedFrequency = detectionResult.frequency,
                targetFrequency = detectedString.targetFrequency
            )

            // Low-pass exponential smoothing (alpha = 0.35)
            val alpha = 0.35
            val currentSmoothCents = smoothedCents?.let { (alpha * rawCents) + ((1 - alpha) * it) } ?: rawCents
            val currentSmoothFreq = smoothedFrequency?.let { (alpha * detectionResult.frequency) + ((1 - alpha) * it) } ?: detectionResult.frequency

            smoothedCents = currentSmoothCents
            smoothedFrequency = currentSmoothFreq

            val tunerState = determineTunerStateUseCase(
                cents = currentSmoothCents,
                isSignalValid = true
            )

            // Save string indicator status matching TunerState for this string
            val updatedStringStates = _uiState.value.stringStates + (detectedString.stringNumber to tunerState)

            _uiState.update {
                it.copy(
                    detectedString = detectedString,
                    detectedNote = detectedString.displayName,
                    frequency = currentSmoothFreq,
                    cents = currentSmoothCents,
                    state = tunerState,
                    confidence = detectionResult.confidence,
                    stringStates = updatedStringStates
                )
            }
        } else {
            noSignalCount++
            // Hold pitch display for ~3 seconds before resetting to NO_SIGNAL state
            if (noSignalCount >= noSignalHoldChunks) {
                resetSmoothing()
                _uiState.update {
                    it.copy(
                        detectedString = null,
                        detectedNote = null,
                        frequency = null,
                        cents = null,
                        state = TunerState.NO_SIGNAL,
                        confidence = 0.0
                    )
                }
            }
        }
    }

    private fun resetSmoothing() {
        smoothedCents = null
        smoothedFrequency = null
        currentDetectedString = null
        noSignalCount = 0
    }

    override fun onCleared() {
        super.onCleared()
        stopListening()
    }
}
