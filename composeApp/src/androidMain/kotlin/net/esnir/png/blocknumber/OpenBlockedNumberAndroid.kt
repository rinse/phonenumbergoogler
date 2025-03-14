package net.esnir.png.blocknumber

import android.content.Context
import android.os.Build
import android.telecom.TelecomManager
import androidx.annotation.RequiresApi
import io.github.aakira.napier.Napier
import net.esnir.png.blockednumber.OpenBlockedNumber

@RequiresApi(Build.VERSION_CODES.N)
class OpenBlockedNumberAndroid(private val context: Context) : OpenBlockedNumber {
    override fun open() {
        val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as? TelecomManager
        if (telecomManager == null) {
            Napier.w("Failed to obtain TelecomManager")
            return
        }
        context.startActivity(
            telecomManager.createManageBlockedNumbersIntent()
        )
    }
}
