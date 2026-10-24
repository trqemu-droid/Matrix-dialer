package com.tarequlislam.matrixdialer.telephony

import android.content.Context
import android.provider.CallLog
import android.provider.ContactsContract
import com.tarequlislam.matrixdialer.eyecon.EyeconIntelligence
import com.tarequlislam.matrixdialer.model.*

object PhoneDataRepository {

    fun loadDeviceContacts(context: Context): List<Contact> {
        val list = mutableListOf<Contact>()

        // ডেভেলপার তারেকুল ইসলামের প্রোফাইল সবার উপরে ফিক্সড থাকবে
        list.add(
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
            )
        )

        try {
            val cursor = context.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                arrayOf(
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
                    ContactsContract.CommonDataKinds.Phone.STARRED
                ),
                null,
                null,
                "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"
            )

            cursor?.use {
                val idIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
                val nameIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val numIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val photoIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)
                val starIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STARRED)

                val seenNumbers = mutableSetOf<String>()

                while (it.moveToNext()) {
                    val id = if (idIdx != -1) it.getString(idIdx) else ""
                    val name = if (nameIdx != -1) it.getString(nameIdx) ?: "No Name" else "No Name"
                    val rawNum = if (numIdx != -1) it.getString(numIdx) ?: "" else ""
                    val photo = if (photoIdx != -1) it.getString(photoIdx) else null
                    val isFav = if (starIdx != -1) it.getInt(starIdx) == 1 else false

                    val cleanNum = rawNum.replace("[^0-9+]".toRegex(), "")
                    if (cleanNum.isNotEmpty() && cleanNum !in seenNumbers) {
                        seenNumbers.add(cleanNum)
                        list.add(
                            Contact(
                                id = id,
                                name = name,
                                number = rawNum,
                                photoUrl = photo,
                                isFavorite = isFav,
                                isLocked = false
                            )
                        )
                    }
                }
            }
        } catch (e: SecurityException) {
            // পারমিশন না থাকলে হ্যান্ডেল করবে
        }

        return list
    }

    fun loadDeviceCallLogs(context: Context): List<CallRecord> {
        val list = mutableListOf<CallRecord>()

        try {
            val cursor = context.contentResolver.query(
                CallLog.Calls.CONTENT_URI,
                arrayOf(
                    CallLog.Calls._ID,
                    CallLog.Calls.CACHED_NAME,
                    CallLog.Calls.NUMBER,
                    CallLog.Calls.TYPE,
                    CallLog.Calls.DATE,
                    CallLog.Calls.DURATION
                ),
                null,
                null,
                "${CallLog.Calls.DATE} DESC LIMIT 100"
            )

            cursor?.use {
                val idIdx = it.getColumnIndex(CallLog.Calls._ID)
                val nameIdx = it.getColumnIndex(CallLog.Calls.CACHED_NAME)
                val numIdx = it.getColumnIndex(CallLog.Calls.NUMBER)
                val typeIdx = it.getColumnIndex(CallLog.Calls.TYPE)
                val dateIdx = it.getColumnIndex(CallLog.Calls.DATE)
                val durIdx = it.getColumnIndex(CallLog.Calls.DURATION)

                while (it.moveToNext()) {
                    val id = if (idIdx != -1) it.getString(idIdx) else ""
                    val name = if (nameIdx != -1) it.getString(nameIdx) else null
                    val number = if (numIdx != -1) it.getString(numIdx) ?: "Unknown" else "Unknown"
                    val callType = if (typeIdx != -1) it.getInt(typeIdx) else CallLog.Calls.INCOMING_TYPE
                    val date = if (dateIdx != -1) it.getLong(dateIdx) else System.currentTimeMillis()
                    val duration = if (durIdx != -1) it.getInt(durIdx) else 0

                    val type = when (callType) {
                        CallLog.Calls.MISSED_TYPE, CallLog.Calls.REJECTED_TYPE -> CallType.MISSED
                        CallLog.Calls.OUTGOING_TYPE -> CallType.OUTGOING
                        else -> CallType.INCOMING
                    }

                    list.add(
                        CallRecord(
                            id = id,
                            contactName = if (name.isNullOrBlank() || name == "null") number else name,
                            number = number,
                            type = type,
                            method = CallMethod.SIM_1,
                            timestamp = date,
                            durationSeconds = duration
                        )
                    )
                }
            }
        } catch (e: SecurityException) {
            // পারমিশন না থাকলে হ্যান্ডেল করবে
        }

        return list
    }
}
