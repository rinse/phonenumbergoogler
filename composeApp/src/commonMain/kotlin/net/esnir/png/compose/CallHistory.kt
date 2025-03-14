package net.esnir.png.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallMade
import androidx.compose.material.icons.automirrored.filled.CallMissed
import androidx.compose.material.icons.automirrored.filled.CallReceived
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Instant
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import net.esnir.png.callhistory.CallContent
import net.esnir.png.callhistory.CallHistoryManager
import net.esnir.png.callhistory.CallType
import net.esnir.png.callstate.CallState
import net.esnir.png.callstate.rememberCallState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CallHistoryControl(
    manager: CallHistoryManager,
    onClickItem: (CallContent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val progressBarGapSize = ProgressIndicatorDefaults.LinearIndicatorTrackGapSize
        if (manager.isLoading) {
            LinearProgressIndicator(gapSize = progressBarGapSize)
        } else {
            Spacer(modifier = Modifier.height(progressBarGapSize))
        }
        // 着信中、UIを下にずらす
        val callState = rememberCallState()
        if (callState == CallState.RINGING) {
            Spacer(modifier = Modifier.fillMaxHeight(0.5f))
        }
        CallHistorySpacer(
            manager.value.firstOrNull(),
            Modifier.fillMaxWidth(),
        )
        @OptIn(ExperimentalMaterial3Api::class)
        PullToRefreshBox(
            isRefreshing = manager.isLoading,
            onRefresh = manager::refresh,
        ) {
            CallHistory(
                manager.value,
                onClickItem = onClickItem,
            )
        }
    }
}

@Composable
fun CallHistorySpacer(
    callContent: CallContent?,
    modifier: Modifier = Modifier,
) {
    Card(modifier) {
        Column(
            Modifier.fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (callContent != null) {
                CallItemDetails(callContent)
            } else {
                Text("No items")
            }
        }
    }
}

@Composable
fun CallHistory(
    callHistory: List<CallContent>,
    onClickItem: (CallContent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        this.items(callHistory, contentType = { CallContent::class }) {
            CallHistoryItem(it, onClick = { onClickItem(it) })
        }
    }
}

@Composable
fun CallHistoryItem(
    content: CallContent,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        headlineContent = { CallTitle(content.number, content.displayName) },
        leadingContent = {
            Icon(CallTypeIcon(content.type), "IconCallType ${content.type}")
        },
        supportingContent = { CallInstant(content.instant) },
        trailingContent = { CallNumberSearch(content.number) },
        modifier = Modifier.clickable { onClick() }.then(modifier),
    )
}

@Composable
fun CallTitle(number: String, displayName: String?, modifier: Modifier = Modifier) {
    val isPrivate = number.isBlank()
    if (isPrivate) {
        Text("Private number", modifier = modifier)
    } else {
        Text(
            displayName ?: number,
            modifier = modifier,
            textDecoration = TextDecoration.Underline,
        )
    }
}

@Composable
fun CallTypeIcon(callType: CallType): ImageVector {
    return when (callType) {
        CallType.OUTGOING -> Icons.AutoMirrored.Filled.CallMade
        CallType.INCOMING -> Icons.AutoMirrored.Filled.CallReceived
        CallType.MISSED -> Icons.AutoMirrored.Filled.CallMissed
        CallType.UNKNOWN -> Icons.Default.QuestionMark
    }
}

@Composable
fun CallInstant(instant: Instant, modifier: Modifier = Modifier) {
    Text(
        instant.format(
            DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET,
            offset = UtcOffset(hours = 9),
        ),
        modifier = modifier,
    )
}

@Composable
fun CallNumberSearch(number: String, modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current
    val isPrivate = number.isBlank()
    IconButton(
        onClick = { uriHandler.openUri("https://www.google.co.jp/search?q=${number}") },
        enabled = !isPrivate,
        modifier = modifier,
    ) {
        Icon(Icons.Default.Search, "Google the number")
    }
}
