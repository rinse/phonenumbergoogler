package net.esnir.png.callhistory

import kotlinx.datetime.Instant

object CallHistoryReaderMock : CallHistoryReader {
    override fun get(): Sequence<CallContent> {
        return sequenceOf(
            CallContent(
                number = "012-345-6789",
                type = CallType.INCOMING,
                instant = Instant.fromEpochMilliseconds(0),
                displayName = null,
            ),
        )
    }
}
