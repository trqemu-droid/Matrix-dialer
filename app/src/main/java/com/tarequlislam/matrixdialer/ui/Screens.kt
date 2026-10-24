package com.tarequlislam.matrixdialer.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tarequlislam.matrixdialer.eyecon.EyeconIntelligence
import com.tarequlislam.matrixdialer.model.*
import com.tarequlislam.matrixdialer.recorder.CallAudioRecorder

// ==========================================
// ১. FAVORITES SCREEN (স্ক্রিনশট ৪ এর মতো গ্রিড ভিউ)
// ==========================================
@Composable
fun FavoritesScreenContent(
    contacts: List<Contact>,
    onCall: (CallMethod, String) -> Unit,
    onOpenEyecon: (EyeconProfile) -> Unit,
    eyeconEnabled: Boolean,
    onOpenKeypad: () -> Unit
) {
    val favorites = contacts.filter { it.isFavorite }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB703))
                    Spacer(Modifier.width(6.dp))
                    Text("Speed Dial & Favorites", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF1E293B)
                ) {
                    Text(
                        "${favorites.size} VIPs",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                }
            }
        }

        // ২-কলামের কার্ড গ্রিড
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                favorites.chunked(2).forEach { rowContacts ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowContacts.forEach { contact ->
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = Color(0xFF0284C7),
                                            modifier = Modifier.size(44.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Text(contact.name.take(1), fontWeight = FontWeight.Bold, color = Color.White)
                                            }
                                        }
                                        if (eyeconEnabled) {
                                            IconButton(
                                                onClick = { onOpenEyecon(contact.eyeconData ?: EyeconIntelligence.lookup(contact.number)) },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(Icons.Default.AutoAwesome, null, tint = Color(0xFF00E5FF), modifier = Modifier.size(16.dp))
                                            }
                                        }
                                    }

                                    Spacer(Modifier.height(8.dp))
                                    Text(contact.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1)
                                    Text(contact.jobTitle ?: contact.number, fontSize = 11.sp, color = Color.Gray, maxLines = 1)

                                    Spacer(Modifier.height(10.dp))

                                    // ৪টি বাটন (SIM1, SIM2, WA, TG)
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0xFF064E3B),
                                            modifier = Modifier.weight(1f).height(32.dp).clickable { onCall(CallMethod.SIM_1, contact.number) }
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(Icons.Default.Call, null, tint = Color(0xFF10B981), modifier = Modifier.size(14.dp))
                                            }
                                        }
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0xFF312E81),
                                            modifier = Modifier.weight(1f).height(32.dp).clickable { onCall(CallMethod.SIM_2, contact.number) }
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(Icons.Default.Call, null, tint = Color(0xFF818CF8), modifier = Modifier.size(14.dp))
                                            }
                                        }
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0xFF064E3B),
                                            modifier = Modifier.weight(1f).height(32.dp).clickable { onCall(CallMethod.WHATSAPP, contact.number) }
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(Icons.Default.Chat, null, tint = Color(0xFF25D366), modifier = Modifier.size(14.dp))
                                            }
                                        }
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0xFF0C4A6E),
                                            modifier = Modifier.weight(1f).height(32.dp).clickable { onCall(CallMethod.TELEGRAM, contact.number) }
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(Icons.Default.Share, null, tint = Color(0xFF38BDF8), modifier = Modifier.size(14.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (rowContacts.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }
        }

        // নিচে নোটিশ ও Open Keypad বাটন
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text("One-Touch 4-Way Calling Enabled", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("Every favorite has instant SIM 1, SIM 2, WhatsApp, and Telegram voice buttons.", fontSize = 11.sp, color = Color.Gray)
                    }
                    Button(
                        onClick = onOpenKeypad,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0891B2)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Open Keypad", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

// ==========================================
// ২. CALL HISTORY (স্ক্রিনশট ৫ এর মতো ফিল্টার ও হিস্ট্রি)
// ==========================================
@Composable
fun RecentsScreenContent(
    calls: List<CallRecord>,
    onCall: (CallMethod, String) -> Unit,
    onOpenEyecon: (String) -> Unit
) {
    var selectedFilter by remember { mutableStateOf("All Calls") }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Call History", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChip(
                        selected = selectedFilter == "All Calls",
                        onClick = { selectedFilter = "All Calls" },
                        label = { Text("All Calls", fontSize = 11.sp) }
                    )
                    FilterChip(
                        selected = selectedFilter == "Missed Only",
                        onClick = { selectedFilter = "Missed Only" },
                        label = { Text("Missed Only", fontSize = 11.sp) }
                    )
                }
            }
        }

        val filteredList = if (selectedFilter == "Missed Only") calls.filter { it.type == CallType.MISSED } else calls

        items(filteredList) { call ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = if (call.type == CallType.MISSED) Color(0xFF450A0A) else Color(0xFF064E3B),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (call.type == CallType.MISSED) Icons.Default.CallMissed else Icons.Default.Call,
                                    contentDescription = null,
                                    tint = if (call.type == CallType.MISSED) Color.Red else Color(0xFF10B981)
                                )
                            }
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(call.contactName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color(0xFF064E3B)
                                ) {
                                    Text(
                                        call.method.name,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                        fontSize = 9.sp,
                                        color = Color(0xFF10B981)
                                    )
                                }
                                if (call.isRecorded) {
                                    Spacer(Modifier.width(4.dp))
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = Color(0xFF7F1D1D)
                                    ) {
                                        Text("● REC", modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp), fontSize = 9.sp, color = Color.Red)
                                    }
                                }
                                Spacer(Modifier.width(6.dp))
                                Text("· ${call.durationSeconds}s", fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { onOpenEyecon(call.number) }) {
                            Icon(Icons.Default.AutoAwesome, null, tint = Color(0xFF00E5FF))
                        }
                        IconButton(onClick = { onCall(call.method, call.number) }) {
                            Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// ৩. CONTACTS (স্ক্রিনশট ৭ এর মতো কন্টাক্ট ডিরেক্টরি ও New/Edit/Delete)
// ==========================================
@Composable
fun ContactsScreenContent(
    contacts: MutableList<Contact>,
    onCall: (CallMethod, String) -> Unit,
    onOpenEyecon: (EyeconProfile) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }
    var editingContact by remember { mutableStateOf<Contact?>(null) }

    Column(Modifier.fillMaxSize().padding(14.dp)) {
        // সার্চ বার এবং + New বাটন
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search contacts by name, number...", fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = { showAddDialog = true },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7))
            ) {
                Icon(Icons.Default.PersonAdd, null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("New", fontSize = 12.sp)
            }
        }

        Spacer(Modifier.height(14.dp))

        val filtered = contacts.filter {
            it.name.contains(searchQuery, ignoreCase = true) || it.number.contains(searchQuery)
        }.sortedBy { it.name }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filtered) { contact ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFF8B5CF6),
                                    modifier = Modifier.size(42.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(contact.name.take(1), fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                }
                                Spacer(Modifier.width(10.dp))
                                Column {
                                    Text(contact.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text(contact.number, fontSize = 11.sp, color = Color.Gray)
                                    Text("${contact.jobTitle ?: ""} · ${contact.company ?: ""}", fontSize = 10.sp, color = Color.DarkGray)
                                }
                            }
                        }

                        Spacer(Modifier.height(8.dp))
                        // ৭টি বাটন (Eyecon, Call1, Call2, WA, TG, Edit, Delete)
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IconButton(onClick = { onOpenEyecon(contact.eyeconData ?: EyeconIntelligence.lookup(contact.number)) }) {
                                Icon(Icons.Default.AutoAwesome, null, tint = Color(0xFF00E5FF), modifier = Modifier.size(16.dp))
                            }
                            IconButton(onClick = { onCall(CallMethod.SIM_1, contact.number) }) {
                                Icon(Icons.Default.Call, null, tint = Color(0xFF10B981), modifier = Modifier.size(16.dp))
                            }
                            IconButton(onClick = { onCall(CallMethod.SIM_2, contact.number) }) {
                                Icon(Icons.Default.Call, null, tint = Color(0xFF818CF8), modifier = Modifier.size(16.dp))
                            }
                            IconButton(onClick = { onCall(CallMethod.WHATSAPP, contact.number) }) {
                                Icon(Icons.Default.Chat, null, tint = Color(0xFF25D366), modifier = Modifier.size(16.dp))
                            }
                            IconButton(onClick = { onCall(CallMethod.TELEGRAM, contact.number) }) {
                                Icon(Icons.Default.Share, null, tint = Color(0xFF38BDF8), modifier = Modifier.size(16.dp))
                            }
                            IconButton(onClick = { editingContact = contact }) {
                                Icon(Icons.Default.Edit, null, tint = Color.LightGray, modifier = Modifier.size(16.dp))
                            }
                            IconButton(
                                onClick = { if (!contact.isLocked) contacts.remove(contact) },
                                enabled = !contact.isLocked
                            ) {
                                Icon(Icons.Default.Delete, null, tint = if (contact.isLocked) Color.DarkGray else Color(0xFFEF4444), modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }

    // নতুন কন্টাক্ট তৈরির ডায়ালগ
    if (showAddDialog) {
        ContactFormDialog(
            title = "Add New Contact",
            onDismiss = { showAddDialog = false },
            onSave = { name, number, job, comp ->
                contacts.add(Contact(id = System.currentTimeMillis().toString(), name = name, number = number, jobTitle = job, company = comp))
                showAddDialog = false
            }
        )
    }

    // কন্টাক্ট এডিট করার ডায়ালগ
    editingContact?.let { c ->
        ContactFormDialog(
            title = "Edit Contact",
            initialName = c.name,
            initialNumber = c.number,
            initialJob = c.jobTitle ?: "",
            initialCompany = c.company ?: "",
            onDismiss = { editingContact = null },
            onSave = { name, number, job, comp ->
                val index = contacts.indexOf(c)
                if (index != -1) {
                    contacts[index] = c.copy(name = name, number = number, jobTitle = job, company = comp)
                }
                editingContact = null
            }
        )
    }
}

// ==========================================
// ৪. RECORDINGS VAULT (স্ক্রিনশট ৩ এর মতো ওয়েভফর্ম ও ফাইল ইনফো)
// ==========================================
@Composable
fun RecordingsScreenContent(callRecorder: CallAudioRecorder, recordingEnabled: Boolean) {
    var autoRecord by remember { mutableStateOf(recordingEnabled) }

    LazyColumn(Modifier.fillMaxSize().padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Call Recording Vault", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("Automatic and manual audio recordings of your conversations", fontSize = 11.sp, color = Color.Gray)
                        }
                        Switch(checked = autoRecord, onCheckedChange = { autoRecord = it })
                    }
                }
            }
        }

        // রেকর্ড আইটেম
        items(listOf("Tarequl Islam" to "01700000001", "Rafiqul Hasan" to "01819234567")) { (name, num) ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFBE123C),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.PlayArrow, null, tint = Color.White)
                            }
                        }
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) {
                            Text(name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("$num · 24 Oct", fontSize = 11.sp, color = Color.Gray)
                        }
                        Text("0m 45s · 1.2 MB", fontSize = 11.sp, color = Color.Gray)
                    }

                    Spacer(Modifier.height(10.dp))
                    // সিমুলেটেড অডিও ওয়েভফর্ম (Waveform)
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        repeat(22) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height((8..24).random().dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(Color(0xFF334155))
                            )
                        }
                    }

                    Spacer(Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Audio Format: High Definition AMR-WB", fontSize = 10.sp, color = Color.DarkGray)
                        Row {
                            IconButton(onClick = {}) { Icon(Icons.Default.FileDownload, null, tint = Color.LightGray, modifier = Modifier.size(16.dp)) }
                            IconButton(onClick = {}) { Icon(Icons.Default.Delete, null, tint = Color.LightGray, modifier = Modifier.size(16.dp)) }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// ৫. SETTINGS (স্ক্রিনশট ৬ এর মতো থিম, টেস্ট আইকন ও ডাবল সিম ইনপুট)
// ==========================================
@Composable
fun SettingsScreenContent(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    eyeconEnabled: Boolean,
    onToggleEyecon: (Boolean) -> Unit,
    callRecordingEnabled: Boolean,
    onToggleRecording: (Boolean) -> Unit,
    sim1Label: String,
    onSim1Change: (String) -> Unit,
    sim2Label: String,
    onSim2Change: (String) -> Unit
) {
    var testNumber by remember { mutableStateOf("01700000001") }
    var inspectedProfile by remember { mutableStateOf<EyeconProfile?>(null) }

    LazyColumn(Modifier.fillMaxSize().padding(14.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Text("Matrix Dialer Settings", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        // Appearance & Theme
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Appearance & Theme", fontWeight = FontWeight.Bold)
                        Text("Switch between High-Contrast Dark Matrix mode or Light", fontSize = 11.sp, color = Color.Gray)
                    }
                    Switch(checked = isDarkTheme, onCheckedChange = { onToggleTheme() })
                }
            }
        }

        // Eyecon Tester Box
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Eyecon Identifier & Social DB", fontWeight = FontWeight.Bold)
                        Switch(checked = eyeconEnabled, onCheckedChange = onToggleEyecon)
                    }

                    OutlinedTextField(
                        value = testNumber,
                        onValueChange = { testNumber = it },
                        label = { Text("Test Eyecon Database on Any Phone Number:") },
                        trailingIcon = {
                            Button(
                                onClick = { inspectedProfile = EyeconIntelligence.lookup(testNumber) },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Inspect ID", fontSize = 11.sp)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Dual SIM Labels Configuration
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Dual SIM Labels & Carrier Names", fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = sim1Label,
                        onValueChange = onSim1Change,
                        label = { Text("SIM 1 Configuration (Button Label)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = sim2Label,
                        onValueChange = onSim2Change,
                        label = { Text("SIM 2 Configuration (Button Label)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

// ==========================================
// ৬. ABOUT (স্ক্রিনশট ২ এর মতো Tarequl Islam এর প্রোফাইল কার্ড)
// ==========================================
@Composable
fun AboutScreenContent(onDirectCallDeveloper: (CallMethod) -> Unit) {
    val context = LocalContext.current

    LazyColumn(Modifier.fillMaxSize().padding(14.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFF060913),
                        modifier = Modifier.size(70.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Phone, null, tint = Color(0xFF00E5FF), modifier = Modifier.size(36.dp))
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("Matrix Dialer", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                    Text("NEXT-GEN TELEPHONY · V2.4 PRO", fontSize = 11.sp, color = Color(0xFF00E5FF), fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(10.dp))
                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7))
                    ) {
                        Icon(Icons.Default.FileDownload, null)
                        Spacer(Modifier.width(6.dp))
                        Text("ফোনে ইনস্টল / ডাউনলোড করুন (Install App)")
                    }
                }
            }
        }

        // Tarequl Islam - Verified Author Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("OFFICIAL DEVELOPER INFORMATION", fontSize = 11.sp, color = Color(0xFF00E5FF), fontWeight = FontWeight.Bold)
                    Text("Developed by:", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF0284C7),
                            modifier = Modifier.size(54.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("TI", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 20.sp)
                            }
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("TAREQUL ISLAM", fontWeight = FontWeight.Black, fontSize = 17.sp)
                                Spacer(Modifier.width(6.dp))
                                Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFF064E3B)) {
                                    Text("VERIFIED AUTHOR", modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp), fontSize = 8.sp, color = Color(0xFF10B981))
                                }
                            }
                            Text("Jr. officer · KDS Logistics Ltd.", fontSize = 12.sp, color = Color.Gray)
                            Text("trqemu@gmail.com", fontSize = 12.sp, color = Color.Gray)
                        }
                    }

                    Spacer(Modifier.height(6.dp))
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:trqemu@gmail.com"))
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Email, null)
                        Spacer(Modifier.width(6.dp))
                        Text("Send Email")
                    }

                    OutlinedButton(
                        onClick = { onDirectCallDeveloper(CallMethod.SIM_1) },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Call, null, tint = Color(0xFF10B981))
                        Spacer(Modifier.width(6.dp))
                        Text("Direct Dial", color = Color.White)
                    }
                }
            }
        }
    }
}

// ইন-অ্যাপ কন্টাক্ট তৈরির ফর্ম ডায়ালগ
@Composable
fun ContactFormDialog(
    title: String,
    initialName: String = "",
    initialNumber: String = "",
    initialJob: String = "",
    initialCompany: String = "",
    onDismiss: () -> Unit,
    onSave: (String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var number by remember { mutableStateOf(initialNumber) }
    var job by remember { mutableStateOf(initialJob) }
    var company by remember { mutableStateOf(initialCompany) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = number, onValueChange = { number = it }, label = { Text("Phone Number") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = job, onValueChange = { job = it }, label = { Text("Job Title / Designation") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = company, onValueChange = { company = it }, label = { Text("Company Name") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(onClick = { if (name.isNotEmpty() && number.isNotEmpty()) onSave(name, number, job, company) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

// Eyecon Social ID Dialog
@Composable
fun EyeconDetailsDialog(profile: EyeconProfile, onDismiss: () -> Unit, onCall: (CallMethod) -> Unit) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(profile.name, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(profile.tag, fontSize = 12.sp, color = Color.Gray)
                Text("Phone: ${profile.phone}", fontWeight = FontWeight.SemiBold)
                Text("Network: ${profile.carrier} · ${profile.location}", fontSize = 12.sp)
                Text("Truecaller Score: ${profile.truecallerScore}%", color = Color(0xFF10B981))
                profile.facebookUrl?.let { fb ->
                    Button(
                        onClick = { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(fb))) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1877F2)),
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("View Facebook Profile") }
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Close") } }
    )
}
