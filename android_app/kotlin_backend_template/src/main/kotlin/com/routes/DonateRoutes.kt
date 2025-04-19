package com.muamalatgo.routes

import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import com.muamalatgo.controllers.handleDonation
import com.muamalatgo.models.Donation

fun Route.donateRoutes() {
    route("/donate") {
        post {
            val donation = call.receive<Donation>()
            val response = handleDonation(donation)
            call.respond(response)
        }
    }
}
