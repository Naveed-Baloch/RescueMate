package com.rescuemate.app.dto

data class Laboratory(
    val ownerId: String = "",
    val city: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val isAvailable:Boolean = true,
    val address: String = ""  ,
    val tests: List<LaboratoryTest> = emptyList()
)

data class LaboratoryTest(
    val name: String = "",
    val price: String = ""
)