package net.esnir.png.navigation

import androidx.core.bundle.Bundle
import androidx.navigation.NavType
import io.ktor.util.decodeBase64String
import io.ktor.util.encodeBase64
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.esnir.png.callhistory.CallContent

object Navigation {
    @Serializable
    data object List

    @Serializable
    data class Item(
        val content: CallContent,
    )
}

inline fun <reified T> navTypeOf(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {
    override fun get(bundle: Bundle, key: String): T? =
        bundle.getString(key)?.let(json::decodeFromString)

    override fun parseValue(value: String): T =
        json.decodeFromString(value.decodeBase64UrlString())

    override fun serializeAsValue(value: T): String =
        json.encodeToString(value).encodeBase64UrlString()

    override fun put(bundle: Bundle, key: String, value: T) =
        bundle.putString(key, json.encodeToString(value))
}

fun String.decodeBase64UrlString(): String {
    return this.replace('-', '+').replace('_', '/')
        .decodeBase64String()
}

fun String.encodeBase64UrlString(): String {
    val s = this.encodeBase64()
    return s.replace('+', '-').replace('/', '_')
}
