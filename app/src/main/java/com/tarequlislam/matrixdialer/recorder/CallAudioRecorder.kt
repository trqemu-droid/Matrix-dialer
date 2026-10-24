package com.tarequlislam.matrixdialer.recorder

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import java.io.File
import java.io.IOException

class CallAudioRecorder(private val context: Context) {
    private var recorder: MediaRecorder? = null
    var isRecording: Boolean = false
        private set
    var currentOutputFile: File? = null
        private set

    fun startRecording(targetNumber: String): Boolean {
        if (isRecording) return true

        val dir = File(context.filesDir, "call_recordings")
        if (!dir.exists()) dir.mkdirs()

        val fileName = "matrix_rec_${targetNumber}_${System.currentTimeMillis()}.m4a"
        val outputFile = File(dir, fileName)
        currentOutputFile = outputFile

        return try {
            val mediaRec = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context)
            } else {
                @Suppress("DEPRECATION")
                MediaRecorder()
            }

            mediaRec.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioEncodingBitRate(128000)
                setAudioSamplingRate(44100)
                setOutputFile(outputFile.absolutePath)
                prepare()
                start()
            }

            recorder = mediaRec
            isRecording = true
            true
        } catch (e: IOException) {
            e.printStackTrace()
            isRecording = false
            currentOutputFile = null
            false
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            isRecording = false
            currentOutputFile = null
            false
        }
    }

    fun stopRecording(): File? {
        if (!isRecording) return null

        return try {
            recorder?.apply {
                stop()
                reset()
                release()
            }
            recorder = null
            isRecording = false
            currentOutputFile
        } catch (e: Exception) {
            e.printStackTrace()
            recorder = null
            isRecording = false
            null
        }
    }

    fun toggleRecording(targetNumber: String): Boolean {
        return if (isRecording) {
            stopRecording()
            false
        } else {
            startRecording(targetNumber)
            true
        }
    }
}
