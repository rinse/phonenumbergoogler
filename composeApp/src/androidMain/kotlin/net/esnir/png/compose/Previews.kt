package net.esnir.png.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.datetime.Instant
import net.esnir.png.callhistory.CallContent
import net.esnir.png.callhistory.CallHistoryManager
import net.esnir.png.callhistory.CallType

@Preview
@Composable
fun CallHistoryListTopBarPreview() {
    CallHistoryListTopAppBar(refresh = {})
}

@Preview
@Composable
fun CallHistoryItemTopBarPreview() {
    CallHistoryItemTopAppBar(goBack = {})
}

@Preview
@Composable
fun CallHistoryControlPreview() {
    CallHistoryControl(
        manager = CallHistoryManagerMock(
            value = listOf(
                CallContent(
                    number = "012-345-6789",
                    type = CallType.INCOMING,
                    instant = Instant.fromEpochMilliseconds(0),
                    displayName = null,
                ),
            ),
            isLoading = false,
        ),
        onClickItem = {}
    )
}

@Preview
@Composable
fun CallHistoryControlNoItemPreview() {
    CallHistoryControl(
        manager = CallHistoryManagerMock(
            value = listOf(),
            isLoading = false,
        ),
        onClickItem = {}
    )
}

@Preview
@Composable
fun CallHistoryControlLoadingPreview() {
    CallHistoryControl(
        manager = CallHistoryManagerMock(
            value = listOf(
                CallContent(
                    number = "012-345-6789",
                    type = CallType.INCOMING,
                    instant = Instant.fromEpochMilliseconds(0),
                    displayName = null,
                ),
            ),
            isLoading = true,
        ),
        onClickItem = {}
    )
}

class CallHistoryManagerMock(
    override val value: List<CallContent>,
    override val isLoading: Boolean,
) : CallHistoryManager {
    override fun refresh() {
    }
}

@Preview
@Composable
fun CallHistoryPreview() {
    val history = listOf(
        CallContent(
            number = "012-345-6789",
            type = CallType.INCOMING,
            instant = Instant.fromEpochMilliseconds(0),
            displayName = null,
        ),
        CallContent(
            number = "012-345-6789",
            type = CallType.OUTGOING,
            instant = Instant.fromEpochMilliseconds(0),
            displayName = null,
        ),
        CallContent(
            number = "012-345-6789",
            type = CallType.MISSED,
            instant = Instant.fromEpochMilliseconds(0),
            displayName = null,
        ),
        CallContent(
            number = "012-345-6789",
            type = CallType.UNKNOWN,
            instant = Instant.fromEpochMilliseconds(0),
            displayName = null,
        ),
        CallContent(
            number = "",
            type = CallType.INCOMING,
            instant = Instant.fromEpochMilliseconds(0),
            displayName = null,
        ),
        CallContent(
            number = "012-345-6789",
            type = CallType.INCOMING,
            instant = Instant.fromEpochMilliseconds(0),
            displayName = "John",
        ),
    )
    CallHistory(history, onClickItem = {})
}

@Preview
@Composable
fun CallItemDetailsPreview(modifier: Modifier = Modifier) {
    val item = CallContent(
        number = "012-345-6789",
        type = CallType.INCOMING,
        instant = Instant.fromEpochMilliseconds(0),
        displayName = "John",
    )
    CallItemDetails(item)
}

@Preview
@Composable
fun CallItemDetailsUnknownPreview(modifier: Modifier = Modifier) {
    val item = CallContent(
        number = "012-345-6789",
        type = CallType.INCOMING,
        instant = Instant.fromEpochMilliseconds(0),
        displayName = null,
    )
    CallItemDetails(item)
}

@Preview
@Composable
fun CallItemDetailsPrivateNumberPreview(modifier: Modifier = Modifier) {
    val item = CallContent(
        number = "",
        type = CallType.INCOMING,
        instant = Instant.fromEpochMilliseconds(0),
        displayName = null,
    )
    CallItemDetails(item)
}
