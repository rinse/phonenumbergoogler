package net.esnir.png.compose

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi
import phonenumbergoogler.composeapp.generated.resources.Res

@Composable
fun LicenceContent(modifier: Modifier) {
    val artifacts = rememberLicenseeArtifacts()
    LazyColumn(modifier = modifier) {
        items(artifacts) { artifact ->
            ListItem(
                headlineContent = {
                    Text("${artifact.groupId}:${artifact.name}:${artifact.version}")
                },
                supportingContent = {
                    Text(artifact.spdxLicenses.joinToString(separator = ", ") { it.identifier })
                }
            )
            HorizontalDivider()
        }
    }
}


@Composable
fun rememberLicenseeArtifacts(): List<LicenseeArtifact> {
    val licenseeArtifacts = remember { mutableStateListOf<LicenseeArtifact>() }
    LaunchedEffect(Unit) {
        @OptIn(ExperimentalResourceApi::class)
        val bytes = Res.readBytes("files/licenseeArtifacts.json")
        val string = bytes.decodeToString() // utf-8
        val artifacts: List<LicenseeArtifact> = LicenseeArtifacts.json.decodeFromString(string)
        for (artifact in artifacts) {
            licenseeArtifacts.add(artifact)
        }
    }
    return licenseeArtifacts
}

object LicenseeArtifacts {
    val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

}

@Serializable
data class LicenseeArtifact(
    val groupId: String,
    val artifactId: String,
    val version: String,
    val name: String,
    val spdxLicenses: List<SpdxLicence> = listOf(),
    val scm: Scm?,
)

@Serializable
data class SpdxLicence(
    val identifier: String,
    val name: String,
    val url: String,
)

@Serializable
data class Scm(
    val url: String,
)
