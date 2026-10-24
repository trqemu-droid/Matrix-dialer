package com.tarequlislam.matrixdialer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tarequlislam.matrixdialer.model.CallMethod

@Composable
fun KeypadBottomPanel(
    digits: String,
    onDigitsChange: (String) -> Unit,
    onDigitPress: (Char) -> Unit,
    onClose: () -> Unit,
    onCall: (CallMethod) -> Unit,
    sim1Label: String,
    sim2Label: String,
    eyeconEnabled: Boolean,
    onOpenEyecon: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.width(32.dp))
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color.Gray.copy(alpha = 0.4f))
                )
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.KeyboardArrowDown, "Close")
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = if (digits.isEmpty()) "Ready to call" else digits,
                    fontSize = if (digits.isEmpty()) 16.sp else 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (digits.isEmpty()) Color.Gray else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                if (digits.isNotEmpty()) {
                    IconButton(onClick = {
                        if (digits.isNotEmpty()) onDigitsChange(digits.dropLast(1))
                    }) {
                        Icon(Icons.Default.Backspace, "Delete")
                    }
                }
            }

            if (eyeconEnabled && digits.length >= 3) {
                AssistChip(
                    onClick = onOpenEyecon,
                    label = { Text("Eyecon ID: Inspect Social & Spam Info", fontSize = 11.sp) },
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }

            val keys = listOf(
                listOf('1' to "", '2' to "ABC", '3' to "DEF"),
                listOf('4' to "GHI", '5' to "JKL", '6' to "MNO"),
                listOf('7' to "PQRS", '8' to "TUV", '9' to "WXYZ"),
                listOf('*' to "", '0' to "+", '#' to "")
            )

            keys.forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                ) {
                    row.forEach { (char, sub) ->
                        KeypadButton(char, sub) {
                            onDigitPress(char)
                            onDigitsChange(digits + char)
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { onCall(CallMethod.SIM_1) },
                    enabled = digits.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Phone, null, modifier = Modifier.size(16.dp))
                        Text(sim1Label, fontSize = 10.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                    }
                }

                Button(
                    onClick = { onCall(CallMethod.SIM_2) },
                    enabled = digits.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Phone, null, modifier = Modifier.size(16.dp))
                        Text(sim2Label, fontSize = 10.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                    }
                }

                Button(
                    onClick = { onCall(CallMethod.WHATSAPP) },
                    enabled = digits.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("WA", fontWeight = FontWeight.Black, fontSize = 12.sp)
                        Text("WhatsApp", fontSize = 9.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                    }
                }

                Button(
                    onClick = { onCall(CallMethod.TELEGRAM) },
                    enabled = digits.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF229ED9)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("TG", fontWeight = FontWeight.Black, fontSize = 12.sp)
                        Text("Telegram", fontSize = 9.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                    }
                }
            }
        }
    }
}

@Composable
fun KeypadButton(char: Char, sub: String, onClick: () -> Unit) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .size(62.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(char.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold)
            if (sub.isNotEmpty()) {
                Text(sub, fontSize = 9.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray)
            }
        }
    }
}
