package net.esnir.png.callstate

import android.telephony.TelephonyManager

fun mapCallState(state: Int): CallState = when (state) {
    TelephonyManager.CALL_STATE_IDLE -> CallState.IDLE
    TelephonyManager.CALL_STATE_RINGING -> CallState.RINGING
    TelephonyManager.CALL_STATE_OFFHOOK -> CallState.Other(state)
    else -> CallState.Other(state)
}
