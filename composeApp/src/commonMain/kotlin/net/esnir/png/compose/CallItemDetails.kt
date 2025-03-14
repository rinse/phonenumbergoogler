package net.esnir.png.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import net.esnir.png.blockednumber.OpenBlockedNumber
import net.esnir.png.callhistory.CallContent
import org.kodein.di.compose.localDI
import org.kodein.di.instance

@Composable
fun CallItemDetails(item: CallContent, modifier: Modifier = Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        DisplayName(item.displayName, item.number)
        Row {
            Icon(CallTypeIcon(item.type), null)
            Text(item.type.name)
        }
        ActionList(item.number)
    }
}

@Composable
fun DisplayName(displayName: String?, number: String, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        val displayNumber = number.takeIf(String::isNotBlank) ?: "Private number"
        Text(
            displayName ?: displayNumber,
            style = MaterialTheme.typography.titleLarge,
        )
        if (displayName != null) {
            Text(
                displayNumber,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
fun ActionList(number: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        CopyToClipBoard(number)
        HorizontalDivider()
        OpenBlockedNumbers()
        HorizontalDivider()
        SearchItem("電話番号でググる", "https://www.google.co.jp/search?q=${number}")
        HorizontalDivider()
        SearchItem("電話帳ナビで検索する", "https://www.telnavi.jp/phone/${number}")
    }
}

@Composable
fun CopyToClipBoard(number: String, modifier: Modifier = Modifier) {
    val clipboardManager = LocalClipboardManager.current
    var text by remember {
        mutableStateOf("電話番号をクリップボードにコピー")
    }
    DropdownMenuItem(
        text = { Text(text) },
        onClick = {
            clipboardManager.setText(AnnotatedString(number))
            text = "電話番号をクリップボードにコピー（コピーしました！）"
        },
        modifier = modifier
    )
}

@Composable
fun OpenBlockedNumbers(modifier: Modifier = Modifier) {
    val di = localDI()
    val openBlockedNumber by di.instance<OpenBlockedNumber>()
    DropdownMenuItem(
        text = { Text("着信拒否リストを開く") },
        onClick = { openBlockedNumber.open() },
        modifier = modifier
    )
}

@Composable
fun SearchItem(
    title: String,
    uri: String,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current
    DropdownMenuItem(
        text = { Text(title, style = MaterialTheme.typography.bodyLarge) },
        onClick = { uriHandler.openUri(uri) },
        modifier = modifier
    )
}
