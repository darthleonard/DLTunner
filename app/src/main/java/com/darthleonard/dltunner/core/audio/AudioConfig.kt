package com.darthleonard.dltunner.core.audio

import android.media.AudioFormat
import android.media.AudioRecord

/**
 * Audio recording parameters configuration.
 */
data class AudioConfig(
    val sampleRate: Int = 44100,
    val channelConfig: Int = AudioFormat.CHANNEL_IN_MONO,
    val audioFormat: Int = AudioFormat.ENCODING_PCM_16BIT,
    val bufferSizeInBytes: Int = calculateBufferSize(sampleRate, channelConfig, audioFormat)
) {
    companion object {
        private fun calculateBufferSize(sampleRate: Int, channelConfig: Int, audioFormat: Int): Int {
            val minSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
            val defaultSize = 8192 // 4096 16-bit PCM shorts = 8192 bytes
            return if (minSize <= 0) defaultSize else maxOf(minSize, defaultSize)
        }
    }
}
