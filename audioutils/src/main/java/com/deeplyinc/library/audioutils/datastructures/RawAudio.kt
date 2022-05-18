package com.deeplyinc.library.audioutils.datastructures

import android.media.AudioTrack
import com.deeplyinc.library.audioutils.AudioUtils
import com.deeplyinc.library.audioutils.types.AudioChannel
import com.deeplyinc.library.audioutils.types.AudioEncoding
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream

class RawAudio private constructor(
    val audioSamples: ShortArray,
    val sampleRate: Int,
    val channel: AudioChannel,
    val encoding: AudioEncoding
) {
    /**
     * Build an audio track.
     * Please note that this function is NOT for audio streaming, but for static audio samples.
     */
    fun toAudioTrack(): AudioTrack = AudioUtils.buildAudioTrack(
        audioSamples, sampleRate, encoding, channel
    )

    /**
     * Write a .wav file with RIFF header.
     * Currently it only support MONO channel.
     */
    fun toWavFile(wavFile: File) {
        when {
            // TODO: add stereo support
            channel != AudioChannel.MONO -> throw UnsupportedOperationException()
        }

        val outputStream = DataOutputStream(FileOutputStream(wavFile))
        AudioUtils.convertRawToWav(
            audioSamples,
            sampleRate,
            channel.numChannels,
            encoding.bitDepthInByte,
            outputStream
        )
        outputStream.close()
    }

    companion object {
        /**
         * Create a RawAudio instance from given audio samples.
         */
        fun from(
            audioSamples: ShortArray,
            sampleRate: Int,
            channel: AudioChannel,
            encoding: AudioEncoding
        ) = RawAudio(audioSamples, sampleRate, channel, encoding)
    }
}

