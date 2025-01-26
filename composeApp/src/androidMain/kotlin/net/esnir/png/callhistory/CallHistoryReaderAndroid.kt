package net.esnir.png.callhistory

import android.content.Context
import android.provider.CallLog
import kotlinx.datetime.Instant

class CallHistoryReaderAndroid(
    private val context: Context,
) : CallHistoryReader {
    override fun get(): Sequence<CallContent> {
        val contentResolver = context.contentResolver
        return sequence {
            contentResolver.query(
                CallLog.Calls.CONTENT_URI.buildUpon()
                    // .appendQueryParameter(CallLog.Calls.LIMIT_PARAM_KEY, "10")
                    .build(),
                null, null, null, null,
            )!!.use { cursor ->
                val cNumber = cursor.getColumnIndex(CallLog.Calls.NUMBER)
                val cType = cursor.getColumnIndex(CallLog.Calls.TYPE)
                val cDate = cursor.getColumnIndex(CallLog.Calls.DATE)
                val cDisplayName = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)
                while (cursor.moveToNext()) {
                    val number = cursor.getString(cNumber)
                    val type = cursor.getString(cType)
                    val date = cursor.getString(cDate)
                    val displayName = cursor.getString(cDisplayName)?.takeIf(String::isNotBlank)
                    val callHistory = CallContent(
                        number = number,
                        type = type.toIntOrNull()?.let(::callType)
                            ?: throw IllegalStateException("Call type is null."),
                        instant = date.toLongOrNull()?.let(Instant::fromEpochMilliseconds)
                            ?: throw IllegalStateException("Call date is not epoch milliseconds: $date"),
                        displayName = displayName,
                    )
                    this.yield(callHistory)
                }
            }
        }
    }
}

fun callType(callType: Int): CallType {
    return when (callType) {
        CallLog.Calls.OUTGOING_TYPE -> CallType.OUTGOING
        CallLog.Calls.INCOMING_TYPE -> CallType.INCOMING
        CallLog.Calls.MISSED_TYPE -> CallType.MISSED
        else -> CallType.UNKNOWN
    }
}
