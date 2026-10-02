package com.darthleonard.dltunner.core.audio

import android.annotation.SuppressLint
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Concrete implementation of [AudioCapture] using Android's [AudioRecord].
 */
class AudioCaptureImpl(
    private val config: AudioConfig = AudioConfig()
) : AudioCapture {

    private val capturing = AtomicBoolean(false)
    private var audioRecord: AudioRecord? = null

    @SuppressLint("MissingPermission")
    override fun startCapture(): Flow<FloatArray> = callbackFlow {
        capturing.set(true)
        val sampleChunkSize = 4096
        val shortBuffer = ShortArray(sampleChunkSize)

        try {
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                config.sampleRate,
                config.channelConfig,
                config.audioFormat,
                config.bufferSizeInBytes
            )

            if (audioRecord?.state == AudioRecord.STATE_INITIALIZED) {
                audioRecord?.startRecording()

                while (capturing.get()) {
                    val readSize = audioRecord?.read(shortBuffer, 0, sampleChunkSize) ?: -1
                    if (readSize > 0) {
                        val floatSamples = FloatArray(readSize)
                        for (i in 0 until readSize) {
                            floatSamples[i] = shortBuffer[i] / 32768.0f
                        }
                        trySend(floatSamples)
                    }
                }
            } else {
                close(IllegalStateException("AudioRecord failed to initialize"))
            }
        } catch (e: SecurityException) {
            close(e)
        } catch (e: Exception) {
            close(e)
        } finally {
            cleanupAudioRecord()
        }

        awaitClose {
            stopCapture()
        }
    }.flowOn(Dispatchers.IO)

    override fun stopCapture() {
        if (capturing.getAndSet(false)) {
            cleanupAudioRecord()
        }
    }

    override fun isCapturing(): Boolean = capturing.get()

    private fun cleanupAudioRecord() {
        try {
            audioRecord?.let { record ->
                if (record.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                    record.stop()
                }
                record.release()
            }
        } catch (_: Exception) {
            // Ignore cleanup exceptions
        } finally {
            audioRecord = null
        }
    }
}
