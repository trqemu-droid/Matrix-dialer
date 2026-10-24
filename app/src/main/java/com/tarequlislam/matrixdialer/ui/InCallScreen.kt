package com.tarequlislam.matrixdialer.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tarequlislam.matrixdialer.model.Contact

@Composable
fun InCallScreen(
    contact: Contact,
    callDuration: String = "01:24",
    isRecording: Boolean = true,
    onEndCall: () -> Unit,
    onToggleRecord: () -> Unit
) {
    var isMuted by remember { mutableStateOf(false) }
    var isSpeakerOn by remember { mutableStateOf(false) }
    var isOnHold by remember { mutableStateOf(false) }

    // পালসিং নিয়ন বর্ডার অ্যানিমেশন
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF060913), Color(0xFF0F172A), Color(0xFF090D16))
                )
            )
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(Modifier.height(20.dp))

            // সিম ও নেটওয়ার্ক স্ট্যাটাস বার
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF1E293B)
            ) {
                Text(
                    text = "SIM 1 (GP 4G) · HD VOICE ENCRYPTED",
                    fontSize = 11.sp,
                    color = Color(0xFF00E5FF),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            Spacer(Modifier.height(30.dp))

            // বড় কন্টাক্ট ছবি (Hero Image with Neon Pulse)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(170.dp)
                    .border(
                        width = 4.dp,
                        color = Color(0xFF00E5FF).copy(alpha = pulseAlpha),
                        shape = CircleShape
                    )
                    .padding(8.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF0284C7),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = contact.name.take(1),
                            fontSize = 64.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // কলারের নাম, পদবি ও নাম্বার
            Text(
                text = contact.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Text(
                text = contact.jobTitle ?: "Direct Mobile Call",
                fontSize = 14.sp,
                color = Color(0xFF94A3B8)
            )
            Text(
                text = contact.number,
                fontSize = 13.sp,
                color = Color(0xFF64748B)
            )

            Spacer(Modifier.height(12.dp))

            // Eyecon ভেরিফাইড ব্যাজ
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF064E3B)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.VerifiedUser, null, tint = Color(0xFF10B981), modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Eyecon Verified · Safe (99%)", fontSize = 11.sp, color = Color(0xFF10B981), fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(16.dp))

            // লাইভ কল টাইমার ও রেকর্ডিং স্ট্যাটাস
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = callDuration,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                if (isRecording) {
                    Spacer(Modifier.width(8.dp))
                    Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFF7F1D1D)) {
                        Text("● REC", fontSize = 10.sp, color = Color.Red, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            // ৬টি অ্যাকশন বাটনের গ্রিড (Mute, Keypad, Speaker, Record, Hold, Eyecon)
            val actionButtons = listOf(
                Triple("Mute", Icons.Default.Mic, isMuted) to { isMuted = !isMuted },
                Triple("Keypad", Icons.Default.Dialpad, false) to {},
                Triple("Speaker", Icons.Default.VolumeUp, isSpeakerOn) to { isSpeakerOn = !isSpeakerOn },
                Triple("Record", Icons.Default.FiberManualRecord, isRecording) to onToggleRecord,
                Triple("Hold", Icons.Default.Pause, isOnHold) to { isOnHold = !isOnHold },
                Triple("Eyecon", Icons.Default.Info, false) to {}
            )

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                actionButtons.chunked(3).forEach { row ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        row.forEach { (data, action) ->
                            val (title, icon, active) = data
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable { action() }
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = if (active) Color(0xFF00E5FF) else Color(0xFF1E293B),
                                    modifier = Modifier.size(56.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = title,
                                            tint = if (active) Color.Black else Color.White
                                        )
                                    }
                                }
                                Spacer(Modifier.height(4.dp))
                                Text(title, fontSize = 11.sp, color = Color.LightGray)
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(30.dp))

            // এন্ড কল বাটন (Red End Call Button)
            Surface(
                shape = CircleShape,
                color = Color(0xFFE11D48),
                modifier = Modifier
                    .size(72.dp)
                    .clickable { onEndCall() }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.CallEnd,
                        contentDescription = "End Call",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}
