package my.app.com.example.muamalatgo.backend.prediction

import my.app.FirebaseInit.firestore
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType

fun runFinalEligibilityPrediction(apiUrl: String, apiKey: String) {
    val notFraudRef = firestore.collection("Penerima-not-fraud")
    val documents = notFraudRef.get().get().documents

    for (doc in documents) {
        val uid = doc.id
        val data = doc.data ?: continue

        val payload = mapper.writeValueAsString(mapOf("inputs" to data))
        val request = Request.Builder()
            .url(apiUrl)
            .addHeader("Authorization", "Bearer $apiKey")
            .post(RequestBody.create("application/json".toMediaType(), payload))
            .build()

        val response = client.newCall(request).execute()
        val result = mapper.readTree(response.body?.string() ?: "{}")

        val eligibility = result["eligibility"]?.asText() ?: "unknown"
        val asnafGroup = result["asnaf_group"]?.asText() ?: "unspecified"

        val finalResult = mapOf(
            "uid" to uid,
            "eligibility" to eligibility,
            "asnaf_group" to asnafGroup
        )

        firestore.collection("Penerima-final").document(uid).set(finalResult)
        println("Stored eligibility result for $uid")
    }
}
