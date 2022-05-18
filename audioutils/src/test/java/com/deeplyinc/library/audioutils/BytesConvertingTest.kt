package com.deeplyinc.library.audioutils

import com.deeplyinc.library.audioutils.extensions.toByteArray
import org.junit.Assert
import org.junit.Test

class BytesConvertingTest {
    @Test
    fun testIntegerLittleEndianConverting() {
        val oneIntegerLittleEndian = ByteArray(4) { i -> if (i == 0) 0x1 else 0x0 }
        val oneConverted = (1).toUInt().toByteArray(true)

        Assert.assertEquals(oneConverted.size, 4)
        Assert.assertArrayEquals(oneIntegerLittleEndian, oneConverted)

        // 2765719519 (unsigned int), big endian = 0xa4d987df, little endian = 0xdf87d9a4
        val largeIntegerLittleEndian = byteArrayOf(
            "df".toUInt(16).toByte(),
            "87".toUInt(16).toByte(),
            "d9".toUInt(16).toByte(),
            "a4".toUInt(16).toByte()
        )
        val largeIntegerConverted = (2765719519).toUInt().toByteArray(true)

        Assert.assertEquals(largeIntegerLittleEndian.size, 4)
        Assert.assertArrayEquals(largeIntegerLittleEndian, largeIntegerConverted)
    }

    @Test
    fun testIntegerBigEndianConverting() {
        val oneIntegerBigEndian = ByteArray(4) { i -> if (i == 3) 0x1 else 0x0 }
        val converted = (1).toUInt().toByteArray(false)

        Assert.assertEquals(converted.size, 4)
        Assert.assertArrayEquals(oneIntegerBigEndian, converted)

        // 2765719519 (unsigned int), big endian = 0xa4d987df, little endian = 0xdf87d9a4
        val largeIntegerBigEndian = byteArrayOf(
            "a4".toUInt(16).toByte(),
            "d9".toUInt(16).toByte(),
            "87".toUInt(16).toByte(),
            "df".toUInt(16).toByte()
        )
        val largeIntegerConverted = (2765719519).toUInt().toByteArray(false)

        Assert.assertEquals(largeIntegerBigEndian.size, 4)
        Assert.assertArrayEquals(largeIntegerBigEndian, largeIntegerConverted)
    }

    @Test
    fun testShortLittleEndianConverting() {
        val oneShortLittleEndian = ByteArray(2) { i -> if (i == 0) 0x1 else 0x0 }
        val converted = (1).toShort().toByteArray(true)

        Assert.assertEquals(converted.size, 2)
        Assert.assertArrayEquals(oneShortLittleEndian, converted)

        // 365 = 0x16d, big endian = 0x016d, little endian = 0x6d01
        val short365LittleEndian = byteArrayOf("6d".toInt(16).toByte(), "01".toInt(16).toByte())
        val short365converted = (365).toShort().toByteArray(true)

        Assert.assertEquals(short365LittleEndian.size, 2)
        Assert.assertArrayEquals(short365converted, short365converted)
    }

    @Test
    fun testShortBigEndianConverting() {
        val oneShortBigEndian = ByteArray(2) { i -> if (i == 1) 0x1 else 0x0 }
        val converted = (1).toShort().toByteArray(false)

        Assert.assertEquals(converted.size, 2)
        Assert.assertArrayEquals(oneShortBigEndian, converted)

        // 365 = 0x16d, big endian = 0x016d, little endian = 0x6d01
        val short365BigEndian = byteArrayOf("01".toInt(16).toByte(), "6d".toInt(16).toByte())
        val short365converted = (365).toShort().toByteArray(true)

        Assert.assertEquals(short365BigEndian.size, 2)
        Assert.assertArrayEquals(short365converted, short365converted)
    }
}