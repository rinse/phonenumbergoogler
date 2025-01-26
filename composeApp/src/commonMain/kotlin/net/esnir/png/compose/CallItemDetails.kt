package net.esnir.png.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import net.esnir.png.callhistory.CallContent

@Composable
fun CallItemDetails(item: CallContent, modifier: Modifier = Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        DisplayName(item.displayName, item.number)
        Row {
            Icon(CallTypeIcon(item.type), null)
            Text(item.type.name)
        }
        SearchList(item.number)
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
fun SearchList(number: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        SearchItem("ググる", "https://www.google.co.jp/search?q=${number}")
        HorizontalDivider()
        SearchItem("電話帳ナビ", "https://www.telnavi.jp/phone/${number}")
    }
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
