package com.perru.choreo


import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor

class PitchDetectionManager(
    private val onPitchDetected: (String) -> Unit
) {
    private var dispatcher: AudioDispatcher? = null

    // Frequency ranges for each note name
    private val noteRanges = listOf(
        "C4"  to (246f..277f),
        "D4"  to (277f..311f),
        "E4"  to (311f..349f),
        "F4"  to (349f..370f),
        "G4"  to (370f..415f),
        "A4"  to (415f..466f),
        "B4"  to (466f..494f),
        "C5"  to (494f..554f),
        "D5"  to (554f..622f),
        "E5"  to (622f..659f)
    )

    fun start() {
        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0)

        val handler = PitchDetectionHandler { result, _ ->
            val pitch = result.pitch
            if (pitch > 0) {
                val noteName = frequencyToNote(pitch)
                if (noteName != null) {
                    onPitchDetected(noteName)
                }
            }
        }

        val pitchProcessor = PitchProcessor(
            PitchProcessor.PitchEstimationAlgorithm.FFT_YIN,
            22050f,
            1024,
            handler
        )

        dispatcher?.addAudioProcessor(pitchProcessor)
        Thread(dispatcher).start()
    }

    fun stop() {
        dispatcher?.stop()
        dispatcher = null
    }

    private fun frequencyToNote(freq: Float): String? {
        return noteRanges.firstOrNull { (_, range) -> freq in range }?.first
    }
}