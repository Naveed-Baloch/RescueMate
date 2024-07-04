package com.rescuemate.app.repository.ambulance

import android.content.Context
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.rescuemate.app.dto.Ambulance
import com.rescuemate.app.dto.AmbulanceRequest
import com.rescuemate.app.dto.Laboratory
import com.rescuemate.app.dto.LaboratoryTest
import com.rescuemate.app.repository.Result
import com.rescuemate.app.utils.FirebaseRef
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.UUID
import javax.inject.Inject

class AmbulanceRepositoryImpl @Inject constructor(
    @ApplicationContext val context: Context,
    private val databaseReference: DatabaseReference,
) : AmbulanceRepository {

    override fun getFirstAvailableAmbulance(city: String): Flow<Result<Ambulance>> = callbackFlow {
        databaseReference.child(FirebaseRef.AMBULANCES).get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                var isAmbulanceAvailable = false
                for (ds in dataSnapshot.children) {
                    val ambulance: Ambulance? = ds.getValue(Ambulance::class.java)
                    if (ambulance != null && ambulance.city == city && ambulance.isAvailable) {
                        trySend(Result.Success(ambulance))
                        isAmbulanceAvailable = true
                        break
                    }
                }
                if (!isAmbulanceAvailable) {
                    trySend(Result.Failure(Exception("No Ambulance found!")))
                }
            } else {
                trySend(Result.Failure(Exception("No Ambulance found!")))
            }
        }.addOnFailureListener {
            trySend(Result.Failure(it))
        }
        awaitClose {
            close()
        }
    }

    override fun getUserAmbulance(userId: String): Flow<Result<Ambulance>> = callbackFlow {
        databaseReference.child(FirebaseRef.AMBULANCES).child(userId).get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                val ambulance = dataSnapshot.getValue(Ambulance::class.java)
                ambulance?.let { trySend(Result.Success(ambulance)) }
            } else {
                trySend(Result.Failure(java.lang.Exception("You Don't have any Ambulance, Please add!")))
            }
        }.addOnFailureListener {
            Result.Failure(it)
        }
        awaitClose {
            close()
        }
    }

    override fun addAmbulance(ambulance: Ambulance): Flow<Result<String>> =
        callbackFlow {
            trySend(Result.Loading)
            databaseReference.child(FirebaseRef.AMBULANCES).child(ambulance.ownerId).setValue(ambulance)
                .addOnSuccessListener {
                    trySend(Result.Success("Ambulance is created.."))
                }.addOnFailureListener {
                    trySend(Result.Failure(it))
                }
            awaitClose {
                close()
            }
        }

    override fun addAmbulanceRequest(ambulanceRequest: AmbulanceRequest) = callbackFlow {
        trySend(Result.Loading)
        databaseReference.child(FirebaseRef.AMBULANCE_REQUESTS).child(UUID.randomUUID().toString()).setValue(ambulanceRequest)
            .addOnSuccessListener {
                trySend(Result.Success("Request is Submitted!"))
            }.addOnFailureListener {
                trySend(Result.Failure(it))
            }
        awaitClose {
            close()
        }
    }

}
