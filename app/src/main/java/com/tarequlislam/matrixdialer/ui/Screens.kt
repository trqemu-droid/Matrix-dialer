package com.tarequlislam.matrixdialer.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tarequlislam.matrixdialer.eyecon.EyeconIntelligence
import com.tarequlislam.matrixdialer.model.*
import com.tarequlislam.matrixdialer.recorder.CallAudioRecorder

@Composable
fun FavoritesScreenContent(
    contacts: List<Contact>,
    onCall: (CallMethod, String) -> Unit,
    onOpenEyecon: (EyeconProfile) -> Unit,
    eyeconEnabled: Boolean
) {
    val favorites = contacts.filter { it.isFavorite }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Speed Dial & Favorites", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Badge { Text("${favorites.size} VIPs") }
            }
        }

        items(favorites) { contact ->
            Card(
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF0891B2),
                                modifier = Modifier.size(46.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(contact.name.take(1), color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(contact.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    if (contact.isLocked) {
                                        Spacer(Modifier.width(4.dp))
                                        Icon(Icons.Default.Lock, "Locked", modifier = Modifier.size(14.dp), tint = Color(0xFF06B6D4))
                                    }
                                }
                                Text(contact.jobTitle ?: contact.number, fontSize = 12.sp, color = Color.Gray)
                            }
                        }

                        if (eyeconEnabled) {
                            IconButton(onClick = {
                                onOpenEyecon(contact.eyeconData ?: EyeconIntelligence.lookup(contact.number))
                            }) {
                                Icon(Icons.Default.VerifiedUser, "Eyecon", tint = Color(0xFF06B6D4))
                            }
                        }
                    }

                    Spacer(Modifier.height(10.dp))
                    Divider(color = Color.Gray.copy(alpha = 0.2f))
                    Spacer(Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = { onCall(CallMethod.SIM_1, contact.number) }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669))) {
                            Text("SIM 1", fontSize = 10.sp)
                        }
                        Button(onClick = { onCall(CallMethod.SIM_2, contact.number) }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5))) {
                            Text("SIM 2", fontSize = 10.sp)
                        }
                        Button(onClick = { onCall(CallMethod.WHATSAPP, contact.number) }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))) {
                            Text("WA", fontSize = 10.sp)
                        }
                        Button(onClick = { onCall(CallMethod.TELEGRAM, contact.number) }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF229ED9))) {
                            Text("TG", fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RecentsScreenContent(
    calls: List<CallRecord>,
    onCall: (CallMethod, String) -> Unit,
    onOpenEyecon: (String) -> Unit,
    eyeconEnabled: Boolean
) {
    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item { Text("Call History", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
        items(calls) { call ->
            Card(shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(call.contactName, fontWeight = FontWeight.Bold)
                        Text("${call.method.name} · ${call.number} · ${call.durationSeconds}s", fontSize = 12.sp, color = Color.Gray)
                    }
                    Row {
                        if (eyeconEnabled) {
                            IconButton(onClick = { onOpenEyecon(call.number) }) {
                                Icon(Icons.Default.Info, "Eyecon", tint = Color(0xFF06B6D4))
                            }
                        }
                        IconButton(onClick = { onCall(call.method, call.number) }) {
                            Icon(Icons.Default.Phone, "Call Back", tint = Color(0xFF059669))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ContactsScreenContent(
    contacts: List<Contact>,
    onCall: (CallMethod, String) -> Unit,
    onOpenEyecon: (EyeconProfile) -> Unit
) {
    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item { Text("Contacts Directory", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
        items(contacts) { c ->
            Card(shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(c.name, fontWeight = FontWeight.Bold)
                            if (c.isLocked) {
                                Spacer(Modifier.width(4.dp))
                                Icon(Icons.Default.Lock, "Protected", modifier = Modifier.size(12.dp), tint = Color(0xFF06B6D4))
                            }
                        }
                        Text(c.number, fontSize = 12.sp, color = Color.Gray)
                    }
                    Row {
                        IconButton(onClick = { onCall(CallMethod.SIM_1, c.number) }) {
                            Icon(Icons.Default.Phone, "Call")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RecordingsScreenContent(callRecorder: CallAudioRecorder, recordingEnabled: Boolean) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Call Recording Vault", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Automatic Call Recording: ${if (recordingEnabled) "ON" else "OFF"}", fontWeight = FontWeight.Bold)
                Text("Currently Recording: ${if (callRecorder.isRecording) "ACTIVE REC" else "Idle"}", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

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
    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { Text("Matrix Dialer Settings", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }

        item {
            Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("Theme (Dark / Light Mode)", fontWeight = FontWeight.Bold)
                        Text(if (isDarkTheme) "Matrix Dark Mode" else "Light Mode", fontSize = 12.sp, color = Color.Gray)
                    }
                    Switch(checked = isDarkTheme, onCheckedChange = { onToggleTheme() })
                }
            }
        }

        item {
            Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("Eyecon Caller Identifier", fontWeight = FontWeight.Bold)
                        Text("Social DB, Facebook, WhatsApp profiles & Spam scores", fontSize = 12.sp, color = Color.Gray)
                    }
                    Switch(checked = eyeconEnabled, onCheckedChange = onToggleEyecon)
                }
            }
        }

        item {
            Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("Call Recording Engine", fontWeight = FontWeight.Bold)
                        Text("Toggleable on-demand or automatic call audio records", fontSize = 12.sp, color = Color.Gray)
                    }
                    Switch(checked = callRecordingEnabled, onCheckedChange = onToggleRecording)
                }
            }
        }
    }
}

@Composable
fun AboutScreenContent(onDirectCallDeveloper: (CallMethod) -> Unit) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text("Matrix Dialer", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
                    Text("Google Dialer Material You Meets 4-Way Calling & Eyecon Intelligence", color = Color(0xFF06B6D4), fontSize = 12.sp)
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Developed by:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                    Text("TAREQUL ISLAM", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = Color(0xFF06B6D4))
                    Text("Jr. officer", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Text("KDS Logistics Ltd.", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    Text("Email: trqemu@gmail.com", fontWeight = FontWeight.Medium, fontSize = 14.sp, color = Color.Gray)

                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:trqemu@gmail.com")
                            }
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0891B2))
                    ) {
                        Icon(Icons.Default.Email, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Email Developer")
                    }
                }
            }
        }
    }
}

@Composable
fun EyeconDetailsDialog(
    profile: EyeconProfile,
    onDismiss: () -> Unit,
    onCall: (CallMethod) -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(profile.name, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(profile.tag, fontSize = 12.sp, color = Color.Gray)
                Text("Phone: ${profile.phone}", fontWeight = FontWeight.SemiBold)
                Text("Network: ${profile.carrier} · ${profile.location}", fontSize = 12.sp)
                Text("Truecaller Trust Score: ${profile.truecallerScore}% (${profile.spamRisk.uppercase()})", fontSize = 12.sp, color = if (profile.spamRisk == "spam") Color.Red else Color(0xFF059669))

                profile.facebookUrl?.let { fb ->
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(fb))
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1877F2)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("View Facebook Profile")
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        }
    )
}
