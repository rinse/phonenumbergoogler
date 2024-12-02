package net.esnir.png.callhistory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import org.kodein.di.compose.localDI
import org.kodein.di.instance

@Composable
fun rememberCallHistory(): List<CallContent> {
    val di = localDI()
    val callHistoryReader: CallHistoryReader by di.instance()
    val callHistory = remember { mutableStateListOf<CallContent>() }
    LaunchedEffect(Unit) {
        val sequence = callHistoryReader.get()
        for (e in sequence) {
            callHistory.add(e)
        }
    }
    return callHistory
}
