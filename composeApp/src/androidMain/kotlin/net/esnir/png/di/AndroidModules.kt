package net.esnir.png.di

import android.content.Context
import net.esnir.png.callhistory.CallHistoryReader
import net.esnir.png.callhistory.CallHistoryReaderAndroid
import org.kodein.di.DI
import org.kodein.di.bindSingleton

fun androidModules(context: Context) = DI.Module("Android") {
    bindSingleton<CallHistoryReader> {
        CallHistoryReaderAndroid(context)
    }
}
