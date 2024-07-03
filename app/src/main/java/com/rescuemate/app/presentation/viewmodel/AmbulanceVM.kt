package com.rescuemate.app.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.rescuemate.app.dto.Ambulance
import com.rescuemate.app.repository.Result
import com.rescuemate.app.repository.ambulance.AmbulanceRepository
import com.rescuemate.app.sharedpref.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AmbulanceVM @Inject constructor(private val userPreferences: UserPreferences, private val ambulanceRepository: AmbulanceRepository) : ViewModel() {
    var ambulances by mutableStateOf<List<Ambulance>>(emptyList())
    var isLoading by mutableStateOf(false)
    var userAmbulance by mutableStateOf<Ambulance?>(null)
    val user by lazy { userPreferences.getUser() }

    fun getAmbulances(city: String) = ambulanceRepository
        .getAmbulances(city)
        .onEach {
            isLoading = it is Result.Loading
            when (it) {
                is Result.Success -> {
                    ambulances = it.data
                }

                else -> {}
            }
        }

    fun addAmbulance(ambulance: Ambulance) = ambulanceRepository.addAmbulance(ambulance).onEach {
        isLoading = it is Result.Loading
    }

    fun getUserAmbulance() = ambulanceRepository.getUserAmbulance(user?.userId ?: "").onEach {
        isLoading = it is Result.Loading
        if(it is Result.Success) {
            userAmbulance = it.data
        }
    }
}