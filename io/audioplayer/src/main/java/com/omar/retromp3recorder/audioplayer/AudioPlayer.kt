package com.omar.retromp3recorder.audioplayer

import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.domain.FromToMillis
import com.omar.retromp3recorder.domain.PlayerControls
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance

interface AudioPlayer {

    fun flow(): Flow<Output>

    fun stateFlow(): Flow<State>

    fun onInput(input: Input)

    sealed interface Input {
        data class Start(val options: PlayerStartOptions) : Input
        object Stop : Input
        object SeekPause : Input
        data class ChangeControls(val controls: PlayerControls) : Input
    }

    sealed interface Output {
        data class Progress(val position: Long, val duration: Long, val end: Boolean) : Output

        sealed class Event : Output {
            data class Message(val message: Stringer) : Event()
            data class Error(val error: Stringer) : Event()
            data class AudioSessionId constructor(val playerId: Int) : Event()
        }
    }

    enum class State {
        Idle,
        Playing,
        PausedToSeek
    }
}

data class PlayerStartOptions(
    val isStopToRangeStartEnabled: Boolean,
    val rangeMillis: FromToMillis,
    val length: Long,
    val relativeSeekPosition: Long,
    val filePath: String,
)

fun AudioPlayer.eventsFlow(): Flow<AudioPlayer.Output.Event> = this.flow().filterIsInstance()

fun AudioPlayer.progressFlow(): Flow<AudioPlayer.Output.Progress> = this.flow().filterIsInstance()
