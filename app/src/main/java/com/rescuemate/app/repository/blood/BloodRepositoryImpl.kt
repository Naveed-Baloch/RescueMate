package com.rescuemate.app.repository.blood

import android.content.Context
import com.google.firebase.database.DatabaseReference
import com.rescuemate.app.dto.BloodDonor
import com.rescuemate.app.dto.User
import com.rescuemate.app.repository.Result
import com.rescuemate.app.utils.FirebaseRef
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class BloodRepositoryImpl @Inject constructor(
    @ApplicationContext val context: Context,
    private val databaseReference: DatabaseReference,
) : BloodRepository {

    override fun getBloodDonors(bloodGroup: String, city: String): Flow<Result<List<BloodDonor>>> = callbackFlow {
        val bloodDonors = mutableListOf<BloodDonor>()
        databaseReference.child(FirebaseRef.BLOOD_DONORS).get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                for (ds in dataSnapshot.children) {
                    val donor: BloodDonor? = ds.getValue(BloodDonor::class.java)
                    if (donor != null && donor.bloodGroup == bloodGroup && donor.city == city) {
                        bloodDonors.add(donor)
                    }
                }
                trySend(Result.Success(bloodDonors))
            } else {
                trySend(Result.Failure(Exception("No Donor found with Blood Group: $bloodGroup")))
            }
        }.addOnFailureListener {
            trySend(Result.Failure(it))
        }
        awaitClose {
            close()
        }
    }

    override fun addDonorToDatabase(donor: BloodDonor): Flow<Result<String>> =
        callbackFlow {
            trySend(Result.Loading)
            databaseReference.child(FirebaseRef.BLOOD_DONORS).child(donor.userId).setValue(donor)
                .addOnSuccessListener {
                    trySend(Result.Success("Data inserted Successfully.."))
                }.addOnFailureListener {
                    trySend(Result.Failure(it))
                }
            awaitClose {
                close()
            }
        }

}