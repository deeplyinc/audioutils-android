package com.deeplyinc.library.audioutils

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import com.deeplyinc.library.audioutils.extensions.toByteArray
import com.deeplyinc.library.audioutils.types.AudioChannel
import com.deeplyinc.library.audioutils.types.AudioEncoding
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import kotlin.math.sin

class AudioUtils {
    companion object {
        /**
         * If the audio is stereo (two channels), Android AudioRecord and AudioTrack use the stereo
         * audio samples as the following form: { L0, R0, L1, R1, ..., Ln, Rn}, but .wav file format
         * requires the stereo audio form as follows: {R0, R1, ..., Rn, L0, L1, ..., Ln}.
         * This function doesn't change the form of value of audioSamples parameter, so if the audio
         * is not mono channel, developers must form the audioSamples.
         * Please note that this function doesn't call outputStream.close(), so the developer must
         * properly close the passing DataOutputStream outside of this function.
         */
        fun convertRawToWav(
            audioSamples: ShortArray,
            sampleRate: Int,
            numChannels: Short,
            bitDepthInByte: Short,
            outputStream: DataOutputStream
        ) {
            outputStream.write(
                buildRiffHeader(
                    audioSamples = audioSamples,
                    sampleRate = sampleRate,
                    numChannels = numChannels,
                    bitDepthInByte = bitDepthInByte
                )
            )
            audioSamples.forEach {
                outputStream.write(it.toByteArray())
            }
        }

        private fun buildRiffHeader(
            audioSamples: ShortArray,
            sampleRate: Int,
            numChannels: Short,
            bitDepthInByte: Short,
        ): ByteArray {
            val totalSamplesInByte = audioSamples.size * numChannels * bitDepthInByte

            val outputStream = ByteArrayOutputStream().apply {
                // RIFF Chunk Descriptor
                write("RIFF".toByteArray())                                      // ChunkID
                write((36 + totalSamplesInByte).toUInt().toByteArray())          // ChunkSize
                write("WAVE".toByteArray())                                      // Format
                // "fmt " Subchunk
                write("fmt ".toByteArray())                                      // Subchunk1ID
                write((16).toInt().toUInt().toByteArray())                       // Subchunk1Size, 16 for PCM
                write(1.toShort().toByteArray())                                 // AudioFormat, 1 for PCM
                write(numChannels.toByteArray())                                 // NumChannels
                write(sampleRate.toUInt().toByteArray())                                  // SampleRate
                write((sampleRate * numChannels * bitDepthInByte).toUInt().toByteArray()) // ByteRate
                write((numChannels * bitDepthInByte).toShort().toByteArray())    // BlockAlign
                write((bitDepthInByte * 8).toShort().toByteArray())              // BitsPerSample
                // "data" Subchunk
                write("data".toByteArray())                                      // Subchunk2ID
                write(totalSamplesInByte.toUInt().toByteArray())                          // Subchunk2Size
            }
            val result = outputStream.toByteArray()
            outputStream.close()

            return result
        }

        /**
         * Build an audio track from given audio samples.
         * Please note that this function is NOT for audio streaming, but for static audio samples.
         */
        fun buildAudioTrack(
            audioSamples: ShortArray,
            sampleRate: Int,
            audioSizeInBytes: Int,
            encoding: Int = AudioFormat.ENCODING_PCM_16BIT,
            channel: Int = AudioFormat.CHANNEL_OUT_MONO
        ): AudioTrack {
            when {
                encoding != AudioFormat.ENCODING_PCM_16BIT -> throw IllegalArgumentException()
                channel != AudioFormat.CHANNEL_OUT_MONO && channel != AudioFormat.CHANNEL_OUT_STEREO -> throw IllegalArgumentException()
            }

            val audioTrack = AudioTrack(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build(),
                AudioFormat.Builder()
                    .setSampleRate(sampleRate)
                    .setEncoding(encoding)
                    .setChannelMask(channel)
                    .build(),
                audioSizeInBytes,
                AudioTrack.MODE_STATIC,
                AudioManager.AUDIO_SESSION_ID_GENERATE
            )

            audioTrack.write(audioSamples, 0, audioSamples.size)
            return audioTrack
        }

        /**
         * Build an audio track from given audio samples.
         * Please note that this function is NOT for audio streaming, but for static audio samples.
         */
        fun buildAudioTrack(
            audioSamples: ShortArray,
            sampleRate: Int,
            encoding: AudioEncoding = AudioEncoding.PCM_16BIT,
            channel: AudioChannel = AudioChannel.MONO
        ): AudioTrack {
            when {
                // input audio samples are ShortArray type, so the only supporting encoding type is PCM_16BIT
                encoding != AudioEncoding.PCM_16BIT -> throw IllegalArgumentException()
            }

            val audioTrack = buildAudioTrack(
                audioSamples = audioSamples,
                sampleRate = sampleRate,
                audioSizeInBytes = audioSamples.size * encoding.bitDepthInByte,
                encoding = encoding.toAudioFormatEncoding(),
                channel = channel.toAudioFormatChannel(false)
            )
            audioTrack.write(audioSamples, 0, audioSamples.size)
            return audioTrack
        }

        /**
         * Generate constant tone audio samples.
         */
        fun generateToneSamples(freqHz: Double, durationMs: Int, sampleRate: Int = 44100): ShortArray {
            val count = (sampleRate * 2.0 * (durationMs / 1000.0)).toInt() and 1.inv()
            val samples = ShortArray(count)
            var i = 0
            while (i < count) {
                val sample = (sin(2 * Math.PI * i / (sampleRate / freqHz)) * 0x7FFF).toInt().toShort()
                samples[i + 0] = sample
                samples[i + 1] = sample
                i += 2
            }
            return samples
        }

        /**
         * Generate a AudioTrack instance with a constant tone audio.
         */
        fun generateToneAudioTrack(freqHz: Double, durationMs: Int, sampleRate: Int = 44100): AudioTrack {
            val samples = generateToneSamples(freqHz, durationMs, sampleRate)
            val track = AudioTrack(
                AudioManager.STREAM_MUSIC, sampleRate,
                AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
                samples.size * (java.lang.Short.SIZE / 8), AudioTrack.MODE_STATIC
            )
            track.write(samples, 0, samples.size)
            return track
        }
    }
}