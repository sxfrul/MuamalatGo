package my.app.com.example.muamalatgo.backend.prediction

import my.app.FirebaseInit.firestore
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType

fun predictPenerima(hfToken: String, apiUrl: String) {
    val penerimaRef = firestore.collection("Penerima")
    val documents = penerimaRef.get().get().documents

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
        val collection = if (label == "fraud") "Penerima-fraud" else "Penerima-not-fraud"

        firestore.collection(collection).document(uid).set(data)
        println("Penerima $uid classified as $label")
    }
}