package net.esnir.png.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallMade
import androidx.compose.material.icons.automirrored.filled.CallMissed
import androidx.compose.material.icons.automirrored.filled.CallReceived
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextDecoration
import kotlinx.datetime.Instant
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import net.esnir.png.callhistory.CallContent
import net.esnir.png.callhistory.CallType

@Composable
fun CallHistory(
    callHistory: List<CallContent>,
    onClickItem: (CallContent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        for (content in callHistory) {
            this.item(contentType = CallContent::class) {
                ListItem(
                    headlineContent = { CallTitle(content.number, content.displayName) },
                    leadingContent = {
                        Icon(CallTypeIcon(content.type), "IconCallType ${content.type}")
                    },
                    supportingContent = { CallInstant(content.instant) },
                    trailingContent = { CallNumberSearch(content.number) },
                    modifier = Modifier.clickable { onClickItem(content) },
                )
            }
        }
    }
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
