package com.rescuemate.app.repository.laboratory

import android.content.Context
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.rescuemate.app.dto.Laboratory
import com.rescuemate.app.dto.LaboratoryTest
import com.rescuemate.app.dto.User
import com.rescuemate.app.repository.Result
import com.rescuemate.app.utils.FirebaseRef
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class LaboratoryRepositoryImpl @Inject constructor(
    @ApplicationContext val context: Context,
    private val databaseReference: DatabaseReference,
) : LaboratoryRepository {

    override fun getLaboratories(city: String,laboratoryTest :String): Flow<Result<List<Laboratory>>> = callbackFlow {
        val laboratories = mutableListOf<Laboratory>()
        trySend(Result.Loading)
        databaseReference.child(FirebaseRef.LABORATORIES).get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                for (ds in dataSnapshot.children) {
                    val laboratory: Laboratory? = ds.getValue(Laboratory::class.java)
                    if (laboratory != null && laboratory.city == city && laboratory.tests.any { it.name == laboratoryTest }) {
                        laboratories.add(laboratory)
                    }
                }
                trySend(Result.Success(laboratories))
            } else {
                trySend(Result.Failure(Exception("No data found")))
            }
        }.addOnFailureListener {
            trySend(Result.Failure(it))
        }
        awaitClose {
            close()
        }
    }

    override fun getUserLaboratory(userId: String): Flow<Result<Laboratory>> = callbackFlow{
        trySend(Result.Loading)
        databaseReference.child(FirebaseRef.LABORATORIES).child(userId).get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()){
                val laboratory = dataSnapshot.getValue(Laboratory::class.java)
                laboratory?.let { trySend(Result.Success(laboratory)) }
            } else{
                trySend(Result.Failure(java.lang.Exception("You Don't have any Laboratory, Please add!")))
            }
        }.addOnFailureListener {
            Result.Failure(it)
        }
        awaitClose {
            close()
        }
    }

    override fun addLaboratory(laboratory: Laboratory): Flow<Result<String>> =
        callbackFlow {
            trySend(Result.Loading)
            databaseReference.child(FirebaseRef.LABORATORIES).child(laboratory.ownerId).setValue(laboratory)
                .addOnSuccessListener {
                    trySend(Result.Success("Laboratory is created.."))
                }.addOnFailureListener {
                    trySend(Result.Failure(it))
                }
            awaitClose {
                close()
            }
        }

    override fun addLaboratoryTest(laboratoryTest: LaboratoryTest, laboratoryId: String): Flow<Result<String>> = callbackFlow {
        val labRef = databaseReference.child(FirebaseRef.LABORATORIES).child(laboratoryId)
        val testsRef = labRef.child("tests")
        trySend(Result.Loading)
        testsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentTests = snapshot.children.mapNotNull { it.getValue(LaboratoryTest::class.java) }.toMutableList()
                currentTests.add(laboratoryTest)
                testsRef.setValue(currentTests)
                    .addOnSuccessListener {
                        trySend(Result.Success("Test is added.."))
                    }
                    .addOnFailureListener {
                        trySend(Result.Failure(it))
                    }
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Result.Failure(error.toException()))
            }
        })

        awaitClose {
            close()
        }
    }
}
