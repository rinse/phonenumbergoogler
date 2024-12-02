package net.esnir.png.di

import net.esnir.png.callhistory.CallHistoryReader
import net.esnir.png.callhistory.CallHistoryReaderMock
import org.kodein.di.DI
import org.kodein.di.bindSingleton

fun mockModules() = DI.Module("Mock") {
    bindSingleton<CallHistoryReader> { CallHistoryReaderMock }
}
