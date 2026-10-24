package com.tarequlislam.matrixdialer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tarequlislam.matrixdialer.audio.DtmfSoundGenerator
import com.tarequlislam.matrixdialer.eyecon.EyeconIntelligence
import com.tarequlislam.matrixdialer.model.*
import com.tarequlislam.matrixdialer.recorder.CallAudioRecorder
import com.tarequlislam.matrixdialer.telephony.CallManager

enum class NavTab {
    FAVORITES, RECENTS, CONTACTS, RECORDINGS, SETTINGS, ABOUT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialerMainApp(
    callManager: CallManager,
    dtmfGenerator: DtmfSoundGenerator,
    callRecorder: CallAudioRecorder,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    externalContacts: MutableList<Contact>,
    externalCallRecords: MutableList<CallRecord>,
    onRequestDefaultDialer: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(NavTab.FAVORITES) }
    var isKeypadVisible by remember { mutableStateOf(false) }
    var dialedDigits by remember { mutableStateOf("") }
    var selectedEyeconProfile by remember { mutableStateOf<EyeconProfile?>(null) }
    var activeInCallNumber by remember { mutableStateOf<String?>(null) }
    var activeCallMethod by remember { mutableStateOf<CallMethod?>(null) }

    var eyeconEnabled by remember { mutableStateOf(true) }
    var callRecordingEnabled by remember { mutableStateOf(true) }
    var sim1Label by remember { mutableStateOf("SIM 1 (GP)") }
    var sim2Label by remember { mutableStateOf("SIM 2 (BL)") }

    val handleCallAction: (CallMethod, String) -> Unit = { method, number ->
        activeInCallNumber = number
        activeCallMethod = method
        callManager.initiateCall(method, number)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row {
                        Text("Matrix Dialer", style = MaterialTheme.typography.titleMedium)
                        if (eyeconEnabled) {
                            Spacer(Modifier.width(8.dp))
                            Badge(containerColor = Color(0xFF00E5FF)) {
                                Text("EYECON", color = Color.Black, fontSize = 10.sp)
                            }
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { isKeypadVisible = !isKeypadVisible }) {
                        Icon(Icons.Default.Dialpad, contentDescription = "Toggle Keypad")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == NavTab.FAVORITES,
                    onClick = { selectedTab = NavTab.FAVORITES; isKeypadVisible = false },
                    icon = { Icon(Icons.Default.Star, "Favorites") },
                    label = { Text("Favorites", fontSize = 11.sp) }
                )
                NavigationBarItem(
                    selected = selectedTab == NavTab.RECENTS,
                    onClick = { selectedTab = NavTab.RECENTS; isKeypadVisible = false },
                    icon = { Icon(Icons.Default.History, "Recents") },
                    label = { Text("Recents", fontSize = 11.sp) }
                )
                NavigationBarItem(
                    selected = selectedTab == NavTab.CONTACTS,
                    onClick = { selectedTab = NavTab.CONTACTS; isKeypadVisible = false },
                    icon = { Icon(Icons.Default.People, "Contacts") },
                    label = { Text("Contacts", fontSize = 11.sp) }
                )
                NavigationBarItem(
                    selected = selectedTab == NavTab.RECORDINGS,
                    onClick = { selectedTab = NavTab.RECORDINGS; isKeypadVisible = false },
                    icon = { Icon(Icons.Default.Radio, "Recordings") },
                    label = { Text("Recordings", fontSize = 11.sp) }
                )
                NavigationBarItem(
                    selected = selectedTab == NavTab.SETTINGS,
                    onClick = { selectedTab = NavTab.SETTINGS; isKeypadVisible = false },
                    icon = { Icon(Icons.Default.Settings, "Settings") },
                    label = { Text("Settings", fontSize = 11.sp) }
                )
                NavigationBarItem(
                    selected = selectedTab == NavTab.ABOUT,
                    onClick = { selectedTab = NavTab.ABOUT; isKeypadVisible = false },
                    icon = { Icon(Icons.Default.Info, "About") },
                    label = { Text("About", fontSize = 11.sp) }
                )
            }
        },
        floatingActionButton = {
            if (!isKeypadVisible) {
                ExtendedFloatingActionButton(
                    onClick = { isKeypadVisible = true },
                    containerColor = Color(0xFF00E5FF),
                    contentColor = Color.Black
                ) {
                    Icon(Icons.Default.Dialpad, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Keypad", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (selectedTab) {
                    NavTab.FAVORITES -> FavoritesScreenContent(
                        contacts = externalContacts,
                        onCall = handleCallAction,
                        onOpenEyecon = { selectedEyeconProfile = it },
                        eyeconEnabled = eyeconEnabled,
                        onOpenKeypad = { isKeypadVisible = true }
                    )
                    NavTab.RECENTS -> RecentsScreenContent(
                        calls = externalCallRecords,
                        onCall = handleCallAction,
                        onOpenEyecon = { selectedEyeconProfile = EyeconIntelligence.lookup(it) }
                    )
                    NavTab.CONTACTS -> ContactsScreenContent(
                        contacts = externalContacts,
                        onCall = handleCallAction,
                        onOpenEyecon = { selectedEyeconProfile = it },
                        onToggleFavorite = { contact ->
                            val index = externalContacts.indexOf(contact)
                            if (index != -1) {
                                externalContacts[index] = contact.copy(isFavorite = !contact.isFavorite)
                            }
                        }
                    )
                    NavTab.RECORDINGS -> RecordingsScreenContent(
                        callRecorder = callRecorder,
                        recordingEnabled = callRecordingEnabled
                    )
                    NavTab.SETTINGS -> SettingsScreenContent(
                        isDarkTheme = isDarkTheme,
                        onToggleTheme = onToggleTheme,
                        eyeconEnabled = eyeconEnabled,
                        onToggleEyecon = { eyeconEnabled = it },
                        callRecordingEnabled = callRecordingEnabled,
                        onToggleRecording = { callRecordingEnabled = it },
                        sim1Label = sim1Label,
                        onSim1Change = { sim1Label = it },
                        sim2Label = sim2Label,
                        onSim2Change = { sim2Label = it },
                        onRequestDefaultDialer = onRequestDefaultDialer
                    )
                    NavTab.ABOUT -> AboutScreenContent(
                        onDirectCallDeveloper = { method ->
                            handleCallAction(method, "01700000001")
                        }
                    )
                }
            }

            if (isKeypadVisible) {
                KeypadBottomPanel(
                    digits = dialedDigits,
                    onDigitsChange = { dialedDigits = it },
                    onDigitPress = { digit ->
                        dtmfGenerator.playTone(digit)
                    },
                    onClose = { isKeypadVisible = false },
                    onCall = { method ->
                        handleCallAction(method, dialedDigits)
                    },
                    sim1Label = sim1Label,
                    sim2Label = sim2Label,
                    eyeconEnabled = eyeconEnabled,
                    onOpenEyecon = { selectedEyeconProfile = EyeconIntelligence.lookup(dialedDigits) }
                )
            }
        }
    }

    selectedEyeconProfile?.let { profile ->
        EyeconDetailsDialog(
            profile = profile,
            onDismiss = { selectedEyeconProfile = null },
            onCall = { method ->
                handleCallAction(method, profile.phone)
                selectedEyeconProfile = null
            }
        )
    }
}
