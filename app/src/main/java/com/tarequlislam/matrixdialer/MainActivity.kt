package com.tarequlislam.matrixdialer

import android.Manifest
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.CallLog
import android.provider.ContactsContract
import android.telecom.TelecomManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.tarequlislam.matrixdialer.audio.DtmfSoundGenerator
import com.tarequlislam.matrixdialer.model.*
import com.tarequlislam.matrixdialer.recorder.CallAudioRecorder
import com.tarequlislam.matrixdialer.telephony.CallManager
import com.tarequlislam.matrixdialer.ui.DialerMainApp

class MainActivity : ComponentActivity() {

    private lateinit var callManager: CallManager
    private lateinit var dtmfGenerator: DtmfSoundGenerator
    private lateinit var callRecorder: CallAudioRecorder

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { _ ->
        // পারমিশন পাওয়ার পর সিস্টেম কন্টাক্ট ও কল লগ রিফ্রেশ হবে
    }

    private val defaultDialerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        callManager = CallManager(this)
        dtmfGenerator = DtmfSoundGenerator()
        callRecorder = CallAudioRecorder(this)

        requestAllPermissions()
        promptSetDefaultDialer()

        setContent {
            var isDarkTheme by remember { mutableStateOf(true) }

            val colorScheme = if (isDarkTheme) {
                darkColorScheme(
                    primary = Color(0xFF00E5FF),
                    secondary = Color(0xFF10B981),
                    background = Color(0xFF060913),
                    surface = Color(0xFF0E1626),
                    onPrimary = Color.Black,
                    onBackground = Color.White,
                    onSurface = Color.White
                )
            } else {
                lightColorScheme(
                    primary = Color(0xFF0284C7),
                    secondary = Color(0xFF059669),
                    background = Color(0xFFF1F5F9),
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

    private fun requestAllPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.RECORD_AUDIO
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            permissions.add(Manifest.permission.READ_PHONE_NUMBERS)
        }

        val missing = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (missing.isNotEmpty()) {
            permissionLauncher.launch(missing.toTypedArray())
        }
    }

    private fun promptSetDefaultDialer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = getSystemService(Context.ROLE_SERVICE) as? RoleManager
            if (roleManager?.isRoleAvailable(RoleManager.ROLE_DIALER) == true &&
                !roleManager.isRoleHeld(RoleManager.ROLE_DIALER)) {
                defaultDialerLauncher.launch(roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER))
            }
        } else {
            val telecomManager = getSystemService(Context.TELECOM_SERVICE) as? TelecomManager
            if (telecomManager?.defaultDialerPackage != packageName) {
                val intent = Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER).apply {
                    putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName)
                }
                defaultDialerLauncher.launch(intent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dtmfGenerator.release()
    }
}
