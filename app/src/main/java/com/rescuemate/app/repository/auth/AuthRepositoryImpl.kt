package com.rescuemate.app.repository.auth

import android.net.Uri
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.rescuemate.app.dto.User
import com.rescuemate.app.repository.Result
import com.rescuemate.app.sharedpref.UserPreferences
import com.rescuemate.app.utils.FirebaseRef
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userPreferences: UserPreferences,
    private val databaseReference: DatabaseReference,
    private val storageReference: StorageReference
): AuthRepository {

    override fun storeUserToDatabase(user: User): Flow<Result<String>> =
        callbackFlow {
        trySend(Result.Loading)
            databaseReference.child(FirebaseRef.USERS).child(user.userId).setValue(user)
                .addOnSuccessListener {
                    trySend(Result.Success("Data inserted Successfully.."))
                }.addOnFailureListener {
                    trySend(Result.Failure(it))
                }
            awaitClose {
                close()
            }
        }

    override fun uploadUserProfilePic(uri: Uri, userId: String): Flow<Result<String>> = callbackFlow {
        trySend(Result.Loading)
        val uploadTask =
            storageReference.child(FirebaseRef.USERS).child(userId).putFile(uri)
        Tasks.whenAllSuccess<UploadTask.TaskSnapshot>(uploadTask)
            .addOnSuccessListener { imageTasks ->
                var downloadUrls = ""
                GlobalScope.launch {
                    imageTasks.forEach {
                        downloadUrls = it.storage.downloadUrl.await().toString()
                    }
                    trySend(Result.Success(downloadUrls))
                }
            }.addOnFailureListener {
            trySend(Result.Failure(it))
        }
        awaitClose {
            close()
        }
    }

    override fun signUp(user: User): Flow<Result<String>> = callbackFlow {
        trySend(Result.Loading)
        firebaseAuth.createUserWithEmailAndPassword(user.email, user.password).addOnCompleteListener { task ->
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