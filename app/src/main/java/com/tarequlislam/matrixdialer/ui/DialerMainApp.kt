package com.tarequlislam.matrixdialer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
    onToggleTheme: () -> Unit
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

    val contactsList = remember {
        mutableStateListOf(
            Contact(
                id = "c-tarequl",
                name = "Tarequl Islam",
                number = "01700000001",
                company = "KDS Logistics Ltd.",
                jobTitle = "Jr. officer",
                email = "trqemu@gmail.com",
                isFavorite = true,
                isLocked = true,
                eyeconData = EyeconIntelligence.AUTHOR_PROFILE
            ),
            Contact(
                id = "c-nusrat",
                name = "Nusrat Jahan",
                number = "01912987654",
                company = "KDS Logistics Ltd.",
                jobTitle = "Commercial Accounts Officer",
                isFavorite = true
            ),
            Contact(
                id = "c-rafiqul",
                name = "Rafiqul Hasan",
                number = "01819234567",
                company = "Apex Footwear Ltd.",
                jobTitle = "Senior Supply Chain Manager",
                isFavorite = true
            )
        )
    }

    val callRecordsList = remember {
        mutableStateListOf(
            CallRecord("1", "Tarequl Islam", "01700000001", CallType.OUTGOING, CallMethod.SIM_1, System.currentTimeMillis() - 600000, 85, true),
            CallRecord("2", "Nusrat Jahan", "01912987654", CallType.INCOMING, CallMethod.WHATSAPP, System.currentTimeMillis() - 3600000, 120, false),
            CallRecord("3", "Rafiqul Hasan", "01819234567", CallType.MISSED, CallMethod.SIM_2, System.currentTimeMillis() - 7200000, 0, false)
        )
    }

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
                            Badge(containerColor = Color(0xFF06B6D4)) {
                                Text("EYECON", color = Color.Black)
                            }
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { isKeypadVisible = !isKeypadVisible }) {
                        Icon(Icons.Default.Phone, contentDescription = "Toggle Keypad")
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
                    label = { Text("Favorites") }
                )
                NavigationBarItem(
                    selected = selectedTab == NavTab.RECENTS,
                    onClick = { selectedTab = NavTab.RECENTS; isKeypadVisible = false },
                    icon = { Icon(Icons.Default.History, "Recents") },
                    label = { Text("Recents") }
                )
                NavigationBarItem(
                    selected = selectedTab == NavTab.CONTACTS,
                    onClick = { selectedTab = NavTab.CONTACTS; isKeypadVisible = false },
                    icon = { Icon(Icons.Default.People, "Contacts") },
                    label = { Text("Contacts") }
                )
                NavigationBarItem(
                    selected = selectedTab == NavTab.RECORDINGS,
                    onClick = { selectedTab = NavTab.RECORDINGS; isKeypadVisible = false },
                    icon = { Icon(Icons.Default.Radio, "Recordings") },
                    label = { Text("Recordings") }
                )
                NavigationBarItem(
                    selected = selectedTab == NavTab.SETTINGS,
                    onClick = { selectedTab = NavTab.SETTINGS; isKeypadVisible = false },
                    icon = { Icon(Icons.Default.Settings, "Settings") },
                    label = { Text("Settings") }
                )
                NavigationBarItem(
                    selected = selectedTab == NavTab.ABOUT,
                    onClick = { selectedTab = NavTab.ABOUT; isKeypadVisible = false },
                    icon = { Icon(Icons.Default.Info, "About") },
                    label = { Text("About") }
                )
            }
        },
        floatingActionButton = {
            if (!isKeypadVisible) {
                FloatingActionButton(
                    onClick = { isKeypadVisible = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.Dialpad, contentDescription = "Open Dialpad")
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
                        contacts = contactsList,
                        onCall = handleCallAction,
                        onOpenEyecon = { selectedEyeconProfile = it },
                        eyeconEnabled = eyeconEnabled
                    )
                    NavTab.RECENTS -> RecentsScreenContent(
                        calls = callRecordsList,
                        onCall = handleCallAction,
                        onOpenEyecon = { selectedEyeconProfile = EyeconIntelligence.lookup(it) },
                        eyeconEnabled = eyeconEnabled
                    )
                    NavTab.CONTACTS -> ContactsScreenContent(
                        contacts = contactsList,
                        onCall = handleCallAction,
                        onOpenEyecon = { selectedEyeconProfile = it }
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
                        onSim2Change = { sim2Label = it }
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
