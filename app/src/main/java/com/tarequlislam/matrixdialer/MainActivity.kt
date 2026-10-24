package com.tarequlislam.matrixdialer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.tarequlislam.matrixdialer.audio.DtmfSoundGenerator
import com.tarequlislam.matrixdialer.recorder.CallAudioRecorder
import com.tarequlislam.matrixdialer.telephony.CallManager
import com.tarequlislam.matrixdialer.ui.DialerMainApp

class MainActivity : ComponentActivity() {

    private lateinit var callManager: CallManager
    private lateinit var dtmfGenerator: DtmfSoundGenerator
    private lateinit var callRecorder: CallAudioRecorder

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Handle runtime telephony and audio permissions
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        callManager = CallManager(this)
        dtmfGenerator = DtmfSoundGenerator()
        callRecorder = CallAudioRecorder(this)

        checkAndRequestPermissions()

        setContent {
            var isDarkTheme by remember { mutableStateOf(true) }

            val colorScheme = if (isDarkTheme) {
                darkColorScheme(
                    primary = Color(0xFF06B6D4), // Cyan Matrix Primary
                    secondary = Color(0xFF10B981), // Emerald
                    background = Color(0xFF090D16), // Dark Slate
                    surface = Color(0xFF131B2E),
                    onPrimary = Color.Black,
                    onBackground = Color.White,
                    onSurface = Color.White
                )
            } else {
                lightColorScheme(
                    primary = Color(0xFF0891B2),
                    secondary = Color(0xFF059669),
                    background = Color(0xFFF8FAFC),
                    surface = Color.White,
                    onPrimary = Color.White,
                    onBackground = Color(0xFF0F172A),
                    onSurface = Color(0xFF0F172A)
                )
            }

            MaterialTheme(colorScheme = colorScheme) {
                DialerMainApp(
                    callManager = callManager,
                    dtmfGenerator = dtmfGenerator,
                    callRecorder = callRecorder,
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = { isDarkTheme = !isDarkTheme }
                )
            }
        }
    }

    private fun checkAndRequestPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            permissions.add(Manifest.permission.READ_PHONE_NUMBERS)
        }

        val neededPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (neededPermissions.isNotEmpty()) {
            permissionLauncher.launch(neededPermissions.toTypedArray())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dtmfGenerator.release()
    }
}
