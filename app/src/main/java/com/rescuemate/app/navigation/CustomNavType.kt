package com.rescuemate.app.navigation

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

class CustomNavType<T : Parcelable>(
    override val isNullableAllowed: Boolean = false,
    private val clazz: Class<T>,
    private val serializer: KSerializer<T>,
) : NavType<T>(isNullableAllowed = isNullableAllowed) {

    override fun get(bundle: Bundle, key: String): T? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, clazz) as T
        } else {
            @Suppress("DEPRECATION") // for backwards compatibility
            bundle.getParcelable(key)
        }

    override fun put(bundle: Bundle, key: String, value: T) = bundle.putParcelable(key, value)

    override fun parseValue(value: String): T = Json.decodeFromString(serializer, Uri.decode(value))

    override fun serializeAsValue(value: T): String = Uri.encode(Json.encodeToString(serializer, value))

    override val name: String = clazz.name
}