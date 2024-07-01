package com.rescuemate.app.repository.user

import android.content.Context
import android.net.Uri
import com.google.android.gms.tasks.Tasks
import com.rescuemate.app.utils.FirebaseRef
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.rescuemate.app.dto.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import com.rescuemate.app.repository.Result
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    @ApplicationContext val context: Context,
    private val databaseReference: DatabaseReference,
    private val storageReference: StorageReference
): UserRepository {
    override fun storeUserToDatabase(user: User): Flow<Result<String>> = callbackFlow {
        trySend(Result.Loading)
        databaseReference.child(FirebaseRef.PATIENTS).child(user.userId).setValue(user)
            .addOnSuccessListener {
                trySend(Result.Success("Data inserted Successfully.."))
            }.addOnFailureListener{
                trySend(Result.Failure(it))
            }
        awaitClose {
            close()
        }
    }

    override fun uploadUserProfilePic(uri: Uri): Flow<Result<String>> = callbackFlow{
        trySend(Result.Loading)
        val uploadTask = storageReference.child(FirebaseRef.PATIENTS).child(uri.toString()).putFile(uri)
        Tasks.whenAllSuccess<UploadTask.TaskSnapshot>(uploadTask).addOnSuccessListener{ imageTasks ->
            var downloadUrls = ""
            GlobalScope.launch {
                imageTasks.forEach {
                    downloadUrls = it.storage.downloadUrl.await().toString()
                }
                trySend(Result.Success(downloadUrls))
            }
        }.addOnFailureListener{
            trySend(Result.Failure(it))
        }
        awaitClose {
            close()
        }
    }

}