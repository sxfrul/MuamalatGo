package com.muamalatgo.models

import kotlinx.serialization.Serializable

@Serializable
data class Donation(
    val userId: String,
    val amount: Double,
    val fundType: String
)
