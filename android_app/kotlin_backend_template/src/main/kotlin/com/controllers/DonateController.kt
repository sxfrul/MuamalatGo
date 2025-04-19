package com.muamalatgo.controllers

import com.muamalatgo.models.Donation
import kotlinx.serialization.Serializable

@Serializable
data class DonationResponse(val success: Boolean, val message: String)

fun handleDonation(donation: Donation): DonationResponse {
    println("Received donation: $donation")
    return DonationResponse(true, "Donation received successfully!")
}
