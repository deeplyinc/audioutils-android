## Deeply Audio Utils

```kotlin
val sampleRate = 16000
val audioSamples: ShortArray = AudioUtils.generateToneSamples(
    freqHz = 400.0,
    durationMs = 1000,
    sampleRate = sampleRate
)

// play the raw audio samples
val audioTrack = RawAudio.from(audioSamples, sampleRate, AudioChannel.MONO, AudioEncoding.PCM_16BIT)
    .toAudioTrack()
audioTrack.play()

// export to a wav file
val wavFile = File("hello.wav")
RawAudio.from(audioSamples, sampleRate, AudioChannel.MONO, AudioEncoding.PCM_16BIT)
    .toWavFile(wavFile)
```