package com.tarequlislam.matrixdialer.eyecon

import com.tarequlislam.matrixdialer.model.EyeconProfile
import kotlin.math.abs

object EyeconIntelligence {

    val AUTHOR_PROFILE = EyeconProfile(
        name = "Tarequl Islam",
        phone = "01700-000001",
        photoUrl = "file:///android_asset/developer_tarequl_islam.jpg",
        isVerified = true,
        tag = "Jr. officer · KDS Logistics Ltd.",
        location = "Chittagong, Bangladesh",
        carrier = "Grameenphone 4G",
        truecallerScore = 99,
        spamRisk = "safe",
        facebookName = "Tarequl Islam",
        facebookUsername = "trq.islam",
        facebookUrl = "https://facebook.com/trqemu",
        mutualFriends = 42,
        whatsappAvailable = true,
        whatsappStatus = "Always on duty | KDS Logistics Ltd. 🚢",
        telegramUsername = "@trqemu",
        linkedinTitle = "Jr. officer",
        linkedinCompany = "KDS Logistics Ltd.",
        linkedinUrl = "https://linkedin.com/in/trqemu"
    )

    fun detectCarrier(rawNumber: String): Pair<String, String> {
        val clean = rawNumber.replace("[^0-9]".toRegex(), "")
        return when {
            clean.startsWith("017") || clean.startsWith("88017") || clean.startsWith("013") || clean.startsWith("88013") ->
                Pair("Grameenphone 4G", "Bangladesh (GP)")
            clean.startsWith("018") || clean.startsWith("88018") ->
                Pair("Robi Axiata", "Bangladesh (Robi)")
            clean.startsWith("019") || clean.startsWith("88019") || clean.startsWith("014") || clean.startsWith("88014") ->
                Pair("Banglalink Digital", "Bangladesh (BL)")
            clean.startsWith("016") || clean.startsWith("88016") ->
                Pair("Airtel Bangladesh", "Bangladesh (Airtel)")
            clean.startsWith("015") || clean.startsWith("88015") ->
                Pair("Teletalk Bangladesh", "Bangladesh (State Tel)")
            else ->
                Pair("GSM Cellular Network", "Global")
        }
    }

    fun lookup(phoneNumber: String): EyeconProfile {
        val clean = phoneNumber.replace("[^0-9]".toRegex(), "")

        if (clean == "01700000001" || clean.endsWith("1700000001")) {
            return AUTHOR_PROFILE
        }

        val (carrier, location) = detectCarrier(phoneNumber)
        val hash = abs(clean.hashCode())
        val isSpam = hash % 19 == 0

        val firstNames = listOf("Mohammad", "Kamrul", "Farhan", "Mahmud", "Arif", "Zubair", "Shahid", "Sadia", "Fatima")
        val lastNames = listOf("Hossain", "Rahman", "Chowdhury", "Karim", "Siddique", "Hasan", "Alam")
        val designations = listOf("Logistics Executive", "Fleet Manager", "Operations Lead", "Procurement Officer")
        val companies = listOf("KDS Group", "Chittagong Port Trade", "Inland Container Depot", "Apex Logistics")

        val name = "${firstNames[hash % firstNames.size]} ${lastNames[(hash / 2) % lastNames.size]}"
        val designation = designations[(hash / 3) % designations.size]
        val company = companies[(hash / 4) % companies.size]

        val trustScore = if (isSpam) 21 else (88 + (hash % 11))
        val fbUser = name.lowercase().replace(" ", ".") + "." + (hash % 999)

        return EyeconProfile(
            name = if (isSpam) "Reported Telemarketer" else name,
            phone = phoneNumber,
            isVerified = !isSpam && trustScore > 90,
            tag = if (isSpam) "Spam Reported by Community" else "$designation · $company",
            location = location,
            carrier = carrier,
            truecallerScore = trustScore,
            spamRisk = if (isSpam) "spam" else "safe",
            facebookName = if (!isSpam) name else null,
            facebookUsername = if (!isSpam) fbUser else null,
            facebookUrl = if (!isSpam) "https://facebook.com/$fbUser" else null,
            mutualFriends = if (!isSpam) (hash % 20) + 1 else null,
            whatsappAvailable = true,
            whatsappStatus = if (isSpam) null else "Hey there! I am using WhatsApp ($designation)",
            telegramUsername = if (!isSpam && hash % 3 != 0) "@" + name.lowercase().replace(" ", "_") else null,
            linkedinTitle = if (!isSpam) designation else null,
            linkedinCompany = if (!isSpam) company else null,
            linkedinUrl = if (!isSpam) "https://linkedin.com" else null
        )
    }
}
