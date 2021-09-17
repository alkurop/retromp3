package com.omar.retromp3recorder.iorecorder

data class RecorderDefaults(
    val audioSourcePref: Mp3VoiceRecorder.AudioSourcePref,
    val bitRate: Mp3VoiceRecorder.BitRate,
    val sampleRate: Mp3VoiceRecorder.SampleRate
)
