package net.esnir.png.callstate

import androidx.compose.runtime.Composable
import org.kodein.di.compose.localDI
import org.kodein.di.instance

interface PhoneCallStateProvider {
    @Composable
    fun rememberCallState(): CallState?
}

@Composable
fun rememberCallState(): CallState? {
    val di = localDI()
    val phoneCallStateProvider by di.instance<PhoneCallStateProvider>()
    return phoneCallStateProvider.rememberCallState()
}
