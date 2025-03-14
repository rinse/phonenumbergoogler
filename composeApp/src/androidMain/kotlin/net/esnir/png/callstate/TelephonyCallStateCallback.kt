package net.esnir.png.callstate

import android.os.Build
import android.telephony.TelephonyCallback
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.S)
class TelephonyCallStateCallback(
    private val callback: (state: Int) -> Unit
) : TelephonyCallback(), TelephonyCallback.CallStateListener {
    override fun onCallStateChanged(state: Int) = callback.invoke(state)
}
