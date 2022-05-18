package com.deeplyinc.library.audioutils

import org.junit.Assert
import org.junit.Test

class AudioUtilsTest {
    @Test
    fun generateToneSamples() {
        val samples = AudioUtils.generateToneSamples(
            400.0,
            1000,
            16000
        )
        Assert.assertEquals(samples.size, 1 * 16000 * 2) // 1s * 16000 sample rate * 2 channels
    }

    // TODO: test AudioUtils.convertRawToWav()
    // TODO: test AudioUtils.generateToneAudioTrack()
    // TODO: test AudioUtils.buildAudioTrack()
}