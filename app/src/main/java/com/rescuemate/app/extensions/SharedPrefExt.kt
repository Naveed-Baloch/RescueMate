package com.rescuemate.app.extensions

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken

const val TAG = "SharedPrefExt"
fun <T> SharedPreferences.writeObject( key: String, data: T) {
    val gson = Gson()
    val json = gson.toJson(data)
    edit { putString(key, json) }
}

inline fun <reified T : Any> SharedPreferences.readObject(key: String): T? {
    val gson = Gson()
    return getString(key, null)?.let {
        parseString<T>(gson, it)
    }
}

inline fun <reified T : Any> parseString(gson: Gson, data: String): T? {
    return try {
        JsonParser.parseString(data).parseV2(gson)
    } catch (e: Exception) {
        Log.e(TAG,"$e Failed to parse json string")
        null
    }
}

inline fun <reified T : Any> JsonElement.parseV2(gson: Gson): T? {
    try {
        return gson.fromJson<T>(this, object : TypeToken<T>() {}.type)
    } catch (e: Exception) {
        try {
            return gson.fromJson<T>(this.asString, object : TypeToken<T>() {}.type)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }
    return null
}