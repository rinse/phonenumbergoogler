package net.esnir.png.callhistory

object CallHistoryReaderNone : CallHistoryReader {
    override fun get(): Sequence<CallContent> {
        return emptySequence()
    }
}
