<<<<<<< Updated upstream
package com.example.muamalatgo.utils
=======
package com.example.muamalatgo.backend.com.example.muamalatgo.utils
>>>>>>> Stashed changes

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

object ApiClient {
    private val client = OkHttpClient()

    fun postJson(url: String, jsonBody: JSONObject, callback: Callback) {
        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), jsonBody.toString())

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(callback)
    }
<<<<<<< Updated upstream
}
=======
}
>>>>>>> Stashed changes
