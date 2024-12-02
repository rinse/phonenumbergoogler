package net.esnir.png.callhistory

interface CallHistoryReader {
     fun get(): Sequence<CallContent>
}
