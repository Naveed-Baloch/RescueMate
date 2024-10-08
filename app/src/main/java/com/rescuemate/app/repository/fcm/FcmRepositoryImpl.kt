package com.rescuemate.app.repository.fcm

import android.content.Context
import android.util.Log
import com.rescuemate.app.network.ApiService
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.database.DatabaseReference
import com.google.firebase.messaging.FirebaseMessaging
import com.rescuemate.app.R
import com.rescuemate.app.messaging.NotificationReq
import com.rescuemate.app.sharedpref.UserPreferences
import com.rescuemate.app.utils.FirebaseRef
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.charset.StandardCharsets
import javax.inject.Inject

private const val TAG = "FcmRepositoryImpl"

class FcmRepositoryImpl @Inject constructor(
    private val userPreferences: UserPreferences,
    private val firebaseMessaging: FirebaseMessaging,
    private val databaseReference: DatabaseReference,
    private val apiService: ApiService,
    @ApplicationContext private val context: Context
) : FcmRepository {
    override suspend fun sendPushNotification(notificationReq: NotificationReq) =
        withContext(Dispatchers.IO) {
            val oAuth2Token = getAccessToken() ?: return@withContext
            try {
                apiService.sendPushNotification(
                    firebaseProjectId = FirebaseRef.PROJECT_ID,
                    authHeader = "Bearer $oAuth2Token",
                    notificationReq = notificationReq
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    override fun refreshFcmToken() {
        firebaseMessaging.token.addOnCompleteListener {
            if (it.isComplete) {
                val token = it.result.toString()
                updateFcmToken(token)
                Log.d(TAG, "refreshFcmToken: $token")
            }
        }
    }

    override fun updateFcmToken(token: String) {
        val user = userPreferences.getUser() ?: return
        val userDbRef = FirebaseRef.USERS
        databaseReference.child(userDbRef).child(user.userId).child(FirebaseRef.TOKEN).setValue(token)
    }

    private fun getAccessToken() = try {
        val jsonString = context.applicationContext.resources.openRawResource(
            R.raw.service_account
        ).bufferedReader().use { it.readText() }

        val firebaseMessagingScope = "https://www.googleapis.com/auth/firebase.messaging"
        val googleCredentials = GoogleCredentials
            .fromStream(jsonString.byteInputStream(StandardCharsets.UTF_8))
            .createScoped(listOf(firebaseMessagingScope))
        googleCredentials.refresh()
        googleCredentials.accessToken.tokenValue
    } catch (e: Exception) {
        Log.e(TAG, e.message.toString())
        null
    }
}