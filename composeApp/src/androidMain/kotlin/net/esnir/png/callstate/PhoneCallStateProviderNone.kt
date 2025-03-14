package net.esnir.png.callstate

import androidx.compose.runtime.Composable

object PhoneCallStateProviderNone : PhoneCallStateProvider {
    @Composable
    override fun rememberCallState(): CallState {
        return CallState.IDLE
    }
}
