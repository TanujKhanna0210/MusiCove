package com.example.musicove

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicove.domain.model.AudioMetadata
import com.example.musicove.domain.repository.MusiCoveRepository
import com.example.musicove.util.audio.VisualizerData
import com.example.musicove.util.audio.VisualizerHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MusiCoveRepository
) : ViewModel() {

    private var _player: MediaPlayer? = null

    private var _visualizerHelper = VisualizerHelper()

    private var _state by mutableStateOf(AudioPlayerState())
    val state: AudioPlayerState
        get() = _state

    private val _visualizerData = mutableStateOf(VisualizerData.emptyVisualizeData())
    val visualizerData: State<VisualizerData>
        get() = _visualizerData

    private val _handler = Handler(Looper.getMainLooper())

    init {
        loadMedias()
    }

    fun onEvent(event: AudioPlayerEvent) {
        when (event) {

            is AudioPlayerEvent.InitAudio -> initAudio(
                context = event.context,
                audio = event.audio,
                onAudioInitialized = event.onAudioInitialized
            )

            AudioPlayerEvent.LoadMedias -> loadMedias()

            AudioPlayerEvent.Pause -> pause()

            AudioPlayerEvent.Play -> play()

            is AudioPlayerEvent.Seek -> seek(
                position = event.position
            )

            AudioPlayerEvent.Stop -> stop()

            AudioPlayerEvent.HideLoadingDialog -> hideLoadingDialog()

            is AudioPlayerEvent.LikeOrUnlikeSong -> likeOrUnlikeSong(id = event.id)
        }
    }

    private fun loadMedias() {
        viewModelScope.launch {
            _state = _state.copy(isLoading = true)
            val audios = mutableListOf<AudioMetadata>()
            audios.addAll(prepareAudios())
            _state = _state.copy(audiosList = audios)
            repository.getFavoriteSongs().collect { favoriteSongs ->
                _state = _state.copy(
                    favoriteSongs = favoriteSongs,
                    isLoading = false
                )
            }
        }
    }

    private suspend fun prepareAudios(): List<AudioMetadata> {
        return repository.getAudios().map { audio ->
            val artist = if (audio.artist.contains("<unknown>"))
                "Unknown artist" else audio.artist
            audio.copy(artist = artist)
        }
    }

    private fun initAudio(audio: AudioMetadata, context: Context, onAudioInitialized: () -> Unit) {
        viewModelScope.launch {
            _state = _state.copy(isLoading = true)

            delay(800)

            val cover = repository.loadCoverBitmap(
                context = context,
                uri = audio.contentUri
            )

            _state = _state.copy(selectedAudio = audio.copy(cover = cover))

            _player = MediaPlayer().apply {
                setDataSource(context, audio.contentUri)
                prepare()
            }

            _player?.setOnCompletionListener {
                pause()
            }

            _player?.setOnPreparedListener {
                onAudioInitialized()
            }

            _state = _state.copy(isLoading = false)
        }
    }

    private fun play() {
        _state = _state.copy(isPlaying = true)

        _player?.start()

        _player?.run {
            _visualizerHelper.start(audioSessionId = audioSessionId,
                onData = { data ->
                    _visualizerData.value = data
                }
            )
        }

        _handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    _state = _state.copy(currentPosition = _player!!.currentPosition)
                    _handler.postDelayed(this, 1000)
                } catch (exp: Exception) {
                    _state = _state.copy(currentPosition = 0)
                }
            }
        }, 0)
    }

    private fun pause() {
        _state = _state.copy(isPlaying = false)
        _visualizerHelper.stop()
        _player?.pause()
    }

    private fun stop() {
        _visualizerHelper.stop()
        _player?.stop()
        _player?.reset()
        _player?.release()
        _state = _state.copy(
            isPlaying = false,
            currentPosition = 0
        )
        _player = null
    }

    private fun seek(position: Float) {
        _player?.run {
            seekTo(position.toInt())
        }
    }

    private fun hideLoadingDialog() {
        _state = _state.copy(isLoading = false)
    }

    private fun likeOrUnlikeSong(id: Long) {
        viewModelScope.launch {
            repository.likeOrUnlikeSong(id = id)
        }
    }

}