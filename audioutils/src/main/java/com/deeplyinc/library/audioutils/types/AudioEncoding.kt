package com.deeplyinc.library.audioutils.types

import android.media.AudioFormat

enum class AudioEncoding(val bitDepthInByte: Short) {
    PCM_16BIT(2);

    fun toAudioFormatEncoding(): Int = when (this) {
        PCM_16BIT -> AudioFormat.ENCODING_PCM_16BIT
    }
}