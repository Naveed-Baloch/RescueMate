package com.rescuemate.app.dto

data class Laboratory(
    val ownerId: String,
    val lat: String,
    val lng: String,
    val city: String,
    val image: String,
    val tests: List<LaboratoryTest>
)


data class LaboratoryTest(
    val id: String,
    val name: String,
    val price: String
)