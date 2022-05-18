package com.deeplyinc.library.audioutils.types

import android.media.AudioFormat

enum class AudioChannel(val numChannels: Short) {
    MONO(1),
    STEREO(2);

    fun toAudioFormatChannel(channelIn: Boolean): Int = when (this) {
        MONO -> if (channelIn) AudioFormat.CHANNEL_IN_MONO else AudioFormat.CHANNEL_OUT_MONO
        STEREO -> if (channelIn) AudioFormat.CHANNEL_IN_STEREO else AudioFormat.CHANNEL_OUT_STEREO
    }
}