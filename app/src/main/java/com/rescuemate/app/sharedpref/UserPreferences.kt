package com.rescuemate.app.sharedpref

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.rescuemate.app.dto.User
import com.rescuemate.app.extensions.readObject
import com.rescuemate.app.extensions.writeObject
import javax.inject.Singleton

@Singleton
class UserPreferences(context: Context) {
    companion object {
        const val USER_DATA = "user_data"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("RescueMate", Context.MODE_PRIVATE)

    fun updateUser(user: User) = sharedPreferences.writeObject(USER_DATA, user)
    fun getUser() = sharedPreferences.readObject<User>(USER_DATA)

    fun removeUserData() {
        sharedPreferences.edit().remove(USER_DATA).apply()
    }

    var payloadRequestId: String? = null

}