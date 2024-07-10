package com.example.musicove.util.audio

import android.media.audiofx.Visualizer

class VisualizerHelper {

    private var visualizer: Visualizer? = null

    companion object {
        val CAPTURE_SIZE = Visualizer.getCaptureSizeRange()[1]
        const val SAMPLING_INTERVAL = 100
    }

    private fun visualizerCallback(onData: (VisualizerData) -> Unit) =
        object : Visualizer.OnDataCaptureListener {

            var lastDateTimestamp: Long? = null

            override fun onWaveFormDataCapture(
                visualizer: Visualizer,
                waveform: ByteArray,
                samplingRate: Int
            ) {

                val now = System.currentTimeMillis()
                val durationSinceLastData = lastDateTimestamp?.let { now - it } ?: 0
                if (lastDateTimestamp == null || durationSinceLastData > SAMPLING_INTERVAL) {
                    onData(
                        VisualizerData(
                            rawWaveForm = waveform,
                            captureSize = CAPTURE_SIZE
                        )
                    )
                    lastDateTimestamp = now
                }

            }

            override fun onFftDataCapture(
                visualizer: Visualizer,
                fft: ByteArray,
                samplingRate: Int
            ) {
            }

        }

    fun start(audioSessionId: Int, onData: (VisualizerData) -> Unit) {
        stop()
        visualizer = Visualizer(audioSessionId).apply {
            enabled = false
            captureSize = CAPTURE_SIZE
            setDataCaptureListener(
                visualizerCallback(onData),
                Visualizer.getMaxCaptureRate(),
                true,
                true
            )
            enabled = true
        }
    }

    private fun stop() {
        visualizer?.release()
        visualizer = null
    }

}