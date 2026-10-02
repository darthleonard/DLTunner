package com.darthleonard.dltunner.core.detection

/**
 * Abstraction for detecting fundamental pitch from audio sample buffers.
 */
interface PitchDetector {
    /**
     * Detects fundamental pitch from audio samples.
     *
     * @param samples Normalized PCM float audio buffer [-1.0, 1.0].
     * @param sampleRate Audio sampling rate in Hz (e.g. 44100).
     * @return [PitchDetectionResult] containing detected frequency and confidence score.
     */
    suspend fun detect(samples: FloatArray, sampleRate: Int): PitchDetectionResult
}
