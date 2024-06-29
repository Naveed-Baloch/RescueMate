package com.rescuemate.app.repository.user

import android.content.Context
import com.rescuemate.app.utils.FirebaseRef
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.rescuemate.app.dto.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import com.rescuemate.app.repository.Result
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    @ApplicationContext val context: Context,
    private val databaseReference: DatabaseReference,
    private val storageReference: StorageReference
): UserRepository {
    override fun storeUserToDatabase(user: User): Flow<Result<String>> = callbackFlow {
        trySend(Result.Loading)
        databaseReference.child(FirebaseRef.CUSTOMERS).child(user.userId).setValue(user)
            .addOnSuccessListener {
                trySend(Result.Success("Data inserted Successfully.."))
            }.addOnFailureListener{
                trySend(Result.Failure(it))
            }
        awaitClose {
            close()
        }
    }

   }