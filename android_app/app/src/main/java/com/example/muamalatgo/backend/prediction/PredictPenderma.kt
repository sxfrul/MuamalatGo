package my.app.com.example.muamalatgo.backend.prediction

import my.app.FirebaseInit.firestore
import okhttp3.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.MediaType.Companion.toMediaType


val client = OkHttpClient()
val mapper = jacksonObjectMapper()

fun predictPenderma(hfToken: String, apiUrl: String) {
    val pendermaRef = firestore.collection("Penderma")
    val documents = pendermaRef.get().get().documents

    for (doc in documents) {
        val data = doc.data ?: continue
        val uid = doc.id

        val bodyJson = mapper.writeValueAsString(mapOf("inputs" to data))
        val request = Request.Builder()

            .url(apiUrl)
            .addHeader("Authorization", "Bearer $hfToken")
            .post(RequestBody.create("application/json".toMediaType(), bodyJson))
            .build()

        val response = client.newCall(request).execute()
        val result = mapper.readTree(response.body?.string() ?: "{}")

        val label = result["fraud_label"]?.asText() ?: "unknown"
        val collection = if (label == "fraud") "Penderma-fraud" else "Penderma-not-fraud"

        firestore.collection(collection).document(uid).set(data)
        println("Penderma $uid classified as $label")
    }
}