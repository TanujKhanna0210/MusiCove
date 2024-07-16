package com.example.musicove

import android.content.Context
import com.example.musicove.domain.model.AudioMetadata

sealed class AudioPlayerEvent {

    data class InitAudio(
        val audio: AudioMetadata,
        val context: Context,
        val onAudioInitialized: () -> Unit
    ) : AudioPlayerEvent()

    data class Seek(
        val position: Float
    ) : AudioPlayerEvent()

    data class LikeOrUnlikeSong(val id: Long) : AudioPlayerEvent()

    object Play : AudioPlayerEvent()

    object Pause : AudioPlayerEvent()

    object Stop : AudioPlayerEvent()

    object LoadMedias : AudioPlayerEvent()

    object HideLoadingDialog : AudioPlayerEvent()
}
