package com.darthleonard.dltunner.core.audio

import kotlinx.coroutines.flow.Flow

/**
 * Interface for capturing PCM audio samples from the device microphone.
 */
interface AudioCapture {
    /**
     * Starts continuous audio capture and streams normalized float PCM audio samples [-1.0, 1.0].
     */
    fun startCapture(): Flow<FloatArray>

    /**
     * Stops audio capture and releases resources.
     */
    fun stopCapture()

    /**
     * Returns true if audio is currently being captured.
     */
    fun isCapturing(): Boolean
}
