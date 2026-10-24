package com.tarequlislam.matrixdialer.telephony

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.telephony.SubscriptionManager
import android.widget.Toast
import com.tarequlislam.matrixdialer.model.CallMethod

class CallManager(private val context: Context) {

    /**
     * Dispatch 4-Way Calling: SIM 1, SIM 2, WhatsApp, or Telegram
     */
    fun initiateCall(method: CallMethod, rawPhoneNumber: String) {
        val cleanNumber = rawPhoneNumber.replace("[^0-9+]".toRegex(), "")
        if (cleanNumber.isEmpty()) {
            Toast.makeText(context, "নম্বর লিখুন (Enter a valid number)", Toast.LENGTH_SHORT).show()
            return
        }

        when (method) {
            CallMethod.SIM_1 -> dialCellularWithSim(cleanNumber, simSlotIndex = 0)
            CallMethod.SIM_2 -> dialCellularWithSim(cleanNumber, simSlotIndex = 1)
            CallMethod.WHATSAPP -> dialWhatsAppVoice(cleanNumber)
            CallMethod.TELEGRAM -> dialTelegramVoice(cleanNumber)
        }
    }

    /**
     * Dial via specific SIM slot (SIM 1 or SIM 2)
     */
    private fun dialCellularWithSim(phoneNumber: String, simSlotIndex: Int) {
        try {
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse("tel:$phoneNumber")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val subscriptionManager = context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as? SubscriptionManager
                try {
                    val activeSubscriptions = subscriptionManager?.activeSubscriptionInfoList
                    if (!activeSubscriptions.isNullOrEmpty() && simSlotIndex < activeSubscriptions.size) {
                        val subInfo = activeSubscriptions[simSlotIndex]
                        intent.putExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE", subInfo.subscriptionId)
                        intent.putExtra("com.android.phone.force.slot", true)
                        intent.putExtra("Cdma_Supp", true)
                        intent.putExtra("simSlot", simSlotIndex)
                        intent.putExtra("com.android.phone.extra.slot", simSlotIndex)
                    }
                } catch (e: SecurityException) {
                    // Fallback to standard dial intent if permissions pending
                }
            }

            context.startActivity(intent)
        } catch (e: Exception) {
            val fallbackIntent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumber")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(fallbackIntent)
        }
    }

    private fun dialWhatsAppVoice(phoneNumber: String) {
        val formattedNumber = if (phoneNumber.startsWith("01")) "88$phoneNumber" else phoneNumber
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://api.whatsapp.com/send?phone=$formattedNumber")
                setPackage("com.whatsapp")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            try {
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$formattedNumber"))
                webIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(webIntent)
            } catch (err: Exception) {
                Toast.makeText(context, "WhatsApp is not installed on this device", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun dialTelegramVoice(phoneNumber: String) {
        val formattedNumber = if (phoneNumber.startsWith("01")) "+88$phoneNumber" else phoneNumber
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("tg://resolve?phone=$formattedNumber")
                setPackage("org.telegram.messenger")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            try {
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/$formattedNumber"))
                webIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(webIntent)
            } catch (err: Exception) {
                Toast.makeText(context, "Telegram is not installed on this device", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
