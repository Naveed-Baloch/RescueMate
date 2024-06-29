package com.rescuemate.app.repository.auth

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import com.google.firebase.FirebaseException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.firestore.auth.User
import com.rescuemate.app.sharedpref.UserPreferences
import kotlinx.coroutines.channels.awaitClose
import java.lang.Exception
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import com.rescuemate.app.repository.Result

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userPreferences: UserPreferences
): AuthRepository {
    private val testPhoneNumber = "3001234567"
    private lateinit var verificationId: String
    private var forceResendToken: ForceResendingToken? = null
    private var lastOtpSentTime = 0L
//    override fun sendOtp(phone: String, activity: Activity?): Flow<Result<String>> = callbackFlow {
//        trySend(Result.Loading)
//        val verificationCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
//            }
//
//            override fun onVerificationFailed(excpetion: FirebaseException) {
//                trySend(Result.Failure(excpetion))
//            }
//
//            override fun onCodeSent(verificationCode: String, forceResendToken: ForceResendingToken) {
//                super.onCodeSent(verificationCode, forceResendToken)
//                this@AuthRepositoryImpl.verificationId = verificationCode
//                this@AuthRepositoryImpl.forceResendToken = forceResendToken
//                lastOtpSentTime = System.currentTimeMillis()
//                trySend(Result.Success("A verification code has been sent to your phone"))
//            }
//        }
//
//        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
//            .setPhoneNumber("+92$phone")
//            .setTimeout(60L, TimeUnit.SECONDS)
//            .apply { activity?.let { setActivity(it) } }
//            .apply { forceResendToken?.let { setForceResendingToken(it) } }
//            .setCallbacks(verificationCallbacks)
//            .build()
//
//        if (System.currentTimeMillis() - lastOtpSentTime < 60_000L){
//            trySend(Result.Failure(Exception("Please wait 60 secs before requesting an OTP again")))
//        } else {
//            PhoneAuthProvider.verifyPhoneNumber(options)
//        }
//
//        awaitClose {
//            close()
//        }
//    }

    override fun signUp(otp: String, user: com.rescuemate.app.dto.User): Flow<Result<String>> = callbackFlow {
        trySend(Result.Loading)
        val credential = PhoneAuthProvider.getCredential(verificationId, otp)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val emailCredential = EmailAuthProvider.getCredential(user.email, user.password)
                    firebaseAuth.currentUser!!.linkWithCredential(emailCredential)
                        .addOnCompleteListener { authResultTask ->
                            if (authResultTask.isSuccessful) {
                                trySend(Result.Success(authResultTask.result.user?.uid.orEmpty()))
                            } else {
                                trySend(Result.Failure(authResultTask.exception ?: Exception("Unable to create user")))
                            }
                        }
                }
            }.addOnFailureListener {
                trySend(Result.Failure(it))
            }
        awaitClose {
            close()
        }
    }

    override fun login(email: String, password: String): Flow<Result<String>> = callbackFlow {
        trySend(Result.Loading)
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                trySend(Result.Success(task.result.user?.uid.orEmpty()))
            } else {
                task.exception?.let { trySend(Result.Failure(it)) } ?: trySend(Result.Failure(Exception("Unknown Error")))
            }
        }.addOnFailureListener {
            trySend(Result.Failure(it))
        }
        awaitClose {
            close()
        }
    }

    override fun signOutFirebaseUser() {
        firebaseAuth.signOut()
        userPreferences.signOut()
    }
}