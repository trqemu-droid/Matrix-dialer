package com.tarequlislam.matrixdialer.telephony

import android.telecom.Call
import android.telecom.InCallService

class MatrixInCallService : InCallService() {
    override fun onCallAdded(call: Call) {
        super.onCallAdded(call)
        // সক্রিয় কল হ্যান্ডেল করার ব্যাকগ্রাউন্ড সার্ভিস
    }

    override fun onCallRemoved(call: Call) {
        super.onCallRemoved(call)
    }
}
