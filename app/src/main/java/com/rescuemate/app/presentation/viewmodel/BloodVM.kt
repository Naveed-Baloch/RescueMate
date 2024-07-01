package com.rescuemate.app.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.rescuemate.app.dto.BloodDonor
import com.rescuemate.app.repository.Result
import com.rescuemate.app.repository.blood.BloodRepository
import com.rescuemate.app.sharedpref.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class BloodVM @Inject constructor(private val userPreferences: UserPreferences, private val bloodRepository: BloodRepository) : ViewModel() {
    var bloodDonors by mutableStateOf<List<BloodDonor>>(emptyList())
    var isLoading by mutableStateOf(false)
    val user by lazy { userPreferences.getUser() }

    fun getBloodDonors(bloodGroup: String, city: String) = bloodRepository
        .getBloodDonors(bloodGroup, city)
        .onEach {
            isLoading = it is Result.Loading
            when (it) {
                is Result.Success -> { bloodDonors = it.data }
                else -> {}
            }
        }

    fun addBloodDonor(bloodDonor: BloodDonor) = bloodRepository.addDonorToDatabase(donor = bloodDonor).onEach {
        isLoading = it is Result.Loading
    }
}