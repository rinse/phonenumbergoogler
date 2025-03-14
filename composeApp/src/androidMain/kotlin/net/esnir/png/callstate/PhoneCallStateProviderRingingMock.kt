package net.esnir.png.callstate

import androidx.compose.runtime.Composable

object PhoneCallStateProviderRingingMock : PhoneCallStateProvider {
    @Composable
    override fun rememberCallState(): CallState? {
        return CallState.RINGING
    }
}
