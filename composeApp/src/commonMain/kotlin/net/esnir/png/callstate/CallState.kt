package net.esnir.png.callstate

sealed interface CallState {
    data object IDLE : CallState
    data object RINGING : CallState
    data class Other(val value: Any) : CallState
}
