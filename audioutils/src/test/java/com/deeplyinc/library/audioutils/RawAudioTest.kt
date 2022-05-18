package com.deeplyinc.library.audioutils

import com.deeplyinc.library.audioutils.datastructures.RawAudio
import com.deeplyinc.library.audioutils.types.AudioChannel
import com.deeplyinc.library.audioutils.types.AudioEncoding
import org.junit.Assert
import org.junit.Test

class RawAudioTest {
    private val samples = shortArrayOf(16000)
    private val sampleRate = 16000
    private val channelMono = AudioChannel.MONO
    private val channelStereo = AudioChannel.STEREO
    private val encoding = AudioEncoding.PCM_16BIT

    private val rawAudioMono = RawAudio.from(
        audioSamples = samples,
        sampleRate = sampleRate,
        channel = channelMono,
        encoding = encoding
    )

    private val rawAudioStereo = RawAudio.from(
        audioSamples = samples,
        sampleRate = sampleRate,
        channel = channelStereo,
        encoding = encoding
    )

    @Test
    fun testInstanceCreation() {
        Assert.assertEquals(rawAudioMono.audioSamples.size, samples.size)
        Assert.assertEquals(rawAudioMono.sampleRate, sampleRate)
        Assert.assertEquals(rawAudioMono.channel, channelMono)
        Assert.assertEquals(rawAudioMono.encoding, encoding)

        Assert.assertEquals(rawAudioStereo.audioSamples.size, samples.size)
        Assert.assertEquals(rawAudioStereo.sampleRate, sampleRate)
        Assert.assertEquals(rawAudioStereo.channel, channelStereo)
        Assert.assertEquals(rawAudioStereo.encoding, encoding)
    }
}