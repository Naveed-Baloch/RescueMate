package com.rescuemate.app.repository.blood

import com.rescuemate.app.dto.BloodDonor
import com.rescuemate.app.dto.User
import com.rescuemate.app.repository.Result
import kotlinx.coroutines.flow.Flow

interface BloodRepository {
    fun getBloodDonors(bloodGroup: String, city: String): Flow<Result<List<BloodDonor>>>
    fun addDonorToDatabase(donor: BloodDonor): Flow<Result<String>>
}