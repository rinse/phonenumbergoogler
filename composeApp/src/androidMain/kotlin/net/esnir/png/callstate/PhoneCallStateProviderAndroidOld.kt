package net.esnir.png.callstate

import android.content.Context
import android.os.Build
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.DisposableEffectResult
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@RequiresApi(Build.VERSION_CODES.Q)
@Deprecated("Deprecated by the Android SDK")
@Suppress("DEPRECATION")
class PhoneCallStateProviderAndroidOld(private val context: Context) : PhoneCallStateProvider {
    @Composable
    override fun rememberCallState(): CallState? {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE)
                as? TelephonyManager
        if (telephonyManager == null) {
            return null
        }
        var callState: CallState? by remember {
            mutableStateOf(null)
        }
        DisposableEffect(Unit) {
            val listener = object : PhoneStateListener() {
                override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                    callState = mapCallState(state)
                }
            }
            telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
            return@DisposableEffect object : DisposableEffectResult {
                override fun dispose() {
                    telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE)
                }
            }
        }
        return callState
    }
}
