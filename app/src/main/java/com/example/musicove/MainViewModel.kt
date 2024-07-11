package com.example.musicove

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicove.domain.model.AudioMetadata
import com.example.musicove.domain.repository.MusiCoveRepository
import com.example.musicove.util.audio.VisualizerData
import com.example.musicove.util.audio.VisualizerHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MusiCoveRepository
) : ViewModel() {

    private var _player: MediaPlayer? = null

    private var _visualizerHelper = VisualizerHelper()

    private var _state = MutableStateFlow(AudioPlayerState())
    val state = _state.asStateFlow()

    private val _visualizerData = mutableStateOf(VisualizerData.emptyVisualizeData())
    val visualizerData: State<VisualizerData>
        get() = _visualizerData

    init {
        loadMedias()
    }

    fun onEvent(event: AudioPlayerEvent) {
        when(event) {

            is AudioPlayerEvent.InitAudio -> initAudio(
                context = event.context,
                audio = event.audio
            )

            AudioPlayerEvent.LoadMedias -> loadMedias()

            AudioPlayerEvent.Pause -> pause()

            AudioPlayerEvent.Play -> play()

            is AudioPlayerEvent.Seek -> seek(
                position = event.position
            )

            AudioPlayerEvent.Stop -> stop()
        }
    }

    private fun loadMedias() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val audios = mutableListOf<AudioMetadata>()
            audios.addAll(prepareAudios())
            _state.update { it.copy(audiosList = audios, isLoading = false) }
        }
    }

    private suspend fun prepareAudios(): List<AudioMetadata> {
        return repository.getAudios().map { audio ->
            val artist = if (audio.artist.contains("<unknown>"))
                "Unknown artist" else audio.artist
            audio.copy(artist = artist)
        }
    }

    private fun initAudio(audio: AudioMetadata, context: Context) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            delay(800)

            val cover = repository.loadCoverBitmap(
                context = context,
                uri = audio.contentUri
            )

            _state.update { it.copy(selectedAudio = audio.copy(cover = cover)) }

            _player = MediaPlayer().apply {
                setDataSource(context, audio.contentUri)
                prepare()
            }

            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun play() {
        _state.update { it.copy(isPlaying = true) }

        _player?.start()

        _player?.run {
            _visualizerHelper.start(audioSessionId = audioSessionId,
                onData = { data ->
                    _visualizerData.value = data
                }
            )
        }
    }

    private fun pause() {
        _state.update { it.copy(isPlaying = false) }
        _visualizerHelper.stop()
        _player?.pause()
    }

    private fun stop() {
        _visualizerHelper.stop()
        _player?.stop()
        _player?.reset()
        _player?.release()
        _state.update { it.copy(isPlaying = false) }
        _player = null
    }

    private fun seek(position: Float) {
        _player?.run {
            seekTo(position.toInt())
        }
    }

}