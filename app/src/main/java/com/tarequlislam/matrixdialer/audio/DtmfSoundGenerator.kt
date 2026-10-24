package com.tarequlislam.matrixdialer.audio

import android.media.AudioManager
import android.media.ToneGenerator

class DtmfSoundGenerator {
    private var toneGenerator: ToneGenerator? = null

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 70)
        } catch (e: Exception) {
            // Audio policy exception fallback
        }
    }

    fun playTone(digit: Char, durationMs: Int = 120) {
        val toneType = when (digit) {
            '0' -> ToneGenerator.TONE_DTMF_0
            '1' -> ToneGenerator.TONE_DTMF_1
            '2' -> ToneGenerator.TONE_DTMF_2
            '3' -> ToneGenerator.TONE_DTMF_3
            '4' -> ToneGenerator.TONE_DTMF_4
            '5' -> ToneGenerator.TONE_DTMF_5
            '6' -> ToneGenerator.TONE_DTMF_6
            '7' -> ToneGenerator.TONE_DTMF_7
            '8' -> ToneGenerator.TONE_DTMF_8
            '9' -> ToneGenerator.TONE_DTMF_9
            '*' -> ToneGenerator.TONE_DTMF_S
            '#' -> ToneGenerator.TONE_DTMF_P
            else -> return
        }

        try {
            toneGenerator?.startTone(toneType, durationMs)
        } catch (e: Exception) {
            // Ignore if tone generator error
        }
    }

    fun release() {
        toneGenerator?.release()
        toneGenerator = null
    }
}
