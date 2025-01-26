package net.esnir.png.callhistory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.ensureActive
import org.kodein.di.compose.localDI
import org.kodein.di.instance

interface CallHistoryManager {
    val value: List<CallContent>
    val isLoading: Boolean
    fun refresh()
}

class CallHistoryManagerImpl(
    override val value: List<CallContent>,
    private val isLoadingState: MutableState<Boolean>,
    private val refresh: () -> Unit,
) : CallHistoryManager {
    override val isLoading: Boolean
        get() = this.isLoadingState.value

    override fun refresh() {
        this.refresh.invoke()
    }
}

@Composable
fun rememberCallHistoryManager(): CallHistoryManager {
    val di = localDI()
    val callHistoryReader: CallHistoryReader by di.instance()
    val callHistory = remember { mutableStateListOf<CallContent>() }
    val isLoading = remember { mutableStateOf(true) }
    var refreshCount by remember { mutableStateOf(0) }
    LaunchedEffect(refreshCount) {
        val history = callHistoryReader.get()
        try {
            for (content in history) {
                this.ensureActive()
                callHistory.add(content)
            }
        } finally {
            isLoading.value = false
        }
    }
    val manager by remember {
        derivedStateOf {
            CallHistoryManagerImpl(
                value = callHistory,
                isLoadingState = isLoading,
                refresh = {
                    isLoading.value = true
                    callHistory.clear()
                    ++refreshCount
                },
            )
        }
    }
    return manager
}
