package net.esnir.png.callhistory

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class CallContent(
    val number: String,
    val type: CallType,
    val instant: Instant,
    val displayName: String?,
)

@Serializable
enum class CallType {
    OUTGOING, INCOMING, MISSED, UNKNOWN,
    ;
}
