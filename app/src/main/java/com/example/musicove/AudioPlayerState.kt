package com.example.musicove

import com.example.musicove.domain.model.AudioMetadata

data class AudioPlayerState(
    val isLoading: Boolean = false,
    val audiosList: List<AudioMetadata> = emptyList(),
    val selectedAudio: AudioMetadata = AudioMetadata.emptyMetadata(),
    val isPlaying: Boolean = false,
    val currentPosition: Int = 0,
    val favoriteSongs: List<Long> = emptyList()
)
