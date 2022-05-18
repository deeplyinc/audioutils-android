package com.deeplyinc.library.audioutils.extensions

fun UInt.toByteArray(littleEndian: Boolean = true): ByteArray {
    val bytes = ByteArray(4)
    if (littleEndian) {
        for (i in 0..3) bytes[i] = (this shr (i * 8)).toByte()
    } else {
        for (i in 0..3) bytes[3 - i] = (this shr (i * 8)).toByte()
    }
    return bytes
}

fun Short.toByteArray(littleEndian: Boolean = true): ByteArray {
    return if (littleEndian) {
        byteArrayOf((this.toInt() and 0x00FF).toByte(), ((this.toInt() and 0xFF00) shr 8).toByte())
    } else {
        byteArrayOf(((this.toInt() and 0xFF00) shr 8).toByte(), (this.toInt() and 0x00FF).toByte())
    }
}
