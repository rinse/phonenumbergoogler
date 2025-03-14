package net.esnir.png.di

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import io.github.aakira.napier.Napier
import net.esnir.png.blockednumber.OpenBlockedNumber
import net.esnir.png.blocknumber.OpenBlockedNumberAndroid
import net.esnir.png.callhistory.CallHistoryReader
import net.esnir.png.callhistory.CallHistoryReaderAndroid
import net.esnir.png.callhistory.CallHistoryReaderNone
import net.esnir.png.callstate.PhoneCallStateProvider
import net.esnir.png.callstate.PhoneCallStateProviderAndroid
import net.esnir.png.callstate.PhoneCallStateProviderAndroidOld
import net.esnir.png.callstate.PhoneCallStateProviderNone
import org.kodein.di.DI
import org.kodein.di.bindSingleton

fun androidModules(context: Context) = DI.Module("Android") {
    bindSingleton<CallHistoryReader> {
        val permission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.READ_CALL_LOG,
        )
        if (permission == PackageManager.PERMISSION_GRANTED) {
            CallHistoryReaderAndroid(context)
        } else {
            CallHistoryReaderNone
        }
    }

    bindSingleton<PhoneCallStateProvider> {
        val permission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.READ_PHONE_STATE,
        )
        when {
            permission == PackageManager.PERMISSION_DENIED -> {
                Napier.w("PhoneCallStateProvider is disabled because READ_PHONE_STATE is denied")
                PhoneCallStateProviderNone
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                Napier.i("PhoneCallStateProviderAndroid is selected.")
                PhoneCallStateProviderAndroid(context)
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                Napier.i("PhoneCallStateProviderAndroidOld is selected.")
                PhoneCallStateProviderAndroidOld(context)
            }

            else -> throw Exception(
                "The minimum build version is ${Build.VERSION_CODES.S}. Current SDK version: ${Build.VERSION.SDK_INT}"
            )
        }
    }

    bindSingleton<OpenBlockedNumber> {
        OpenBlockedNumberAndroid(context)
    }
}
