package net.esnir.png.di

import net.esnir.png.callhistory.CallHistoryReader
import net.esnir.png.callhistory.CallHistoryReaderMock
import net.esnir.png.callstate.PhoneCallStateProvider
import net.esnir.png.callstate.PhoneCallStateProviderRingingMock
import org.kodein.di.DI
import org.kodein.di.bindSingleton

fun mockModules() = DI.Module("Mock") {
    bindSingleton<CallHistoryReader> { CallHistoryReaderMock }
    bindSingleton<PhoneCallStateProvider> { PhoneCallStateProviderRingingMock }
}
