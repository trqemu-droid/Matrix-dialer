package com.tarequlislam.matrixdialer.model

data class Contact(
    val id: String,
    val name: String,
    val number: String,
    val secondaryNumber: String? = null,
    val email: String? = null,
    val company: String? = null,
    val jobTitle: String? = null,
    val photoUrl: String? = null,
    val isFavorite: Boolean = false,
    val isLocked: Boolean = false, // Permanent developer/author lock
    val eyeconData: EyeconProfile? = null
)

data class EyeconProfile(
    val name: String,
    val phone: String,
    val photoUrl: String? = null,
    val isVerified: Boolean = false,
    val tag: String,
    val location: String,
    val carrier: String,
    val truecallerScore: Int,
    val spamRisk: String, // "safe", "neutral", "spam"
    val facebookName: String? = null,
    val facebookUrl: String? = null,
    val facebookUsername: String? = null,
    val mutualFriends: Int? = null,
    val whatsappAvailable: Boolean = true,
    val whatsappStatus: String? = null,
    val telegramUsername: String? = null,
    val linkedinTitle: String? = null,
    val linkedinCompany: String? = null,
    val linkedinUrl: String? = null
)

enum class CallMethod {
    SIM_1, SIM_2, WHATSAPP, TELEGRAM
}

enum class CallType {
    INCOMING, OUTGOING, MISSED
}

data class CallRecord(
    val id: String,
    val contactName: String,
    val number: String,
    val type: CallType,
    val method: CallMethod,
    val timestamp: Long,
    val durationSeconds: Int,
    val isRecorded: Boolean = false,
    val recordingPath: String? = null,
    val simCarrier: String? = null,
    val eyeconVerified: Boolean = false
)

data class RecordedCall(
    val id: String,
    val callId: String,
    val contactName: String,
    val number: String,
    val method: CallMethod,
    val timestamp: Long,
    val durationSeconds: Int,
    val fileSizeFormatted: String,
    val filePath: String
)
