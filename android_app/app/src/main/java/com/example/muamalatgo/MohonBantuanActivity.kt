package com.example.muamalatgo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONArray
import org.json.JSONObject

class MohonBantuanActivity : AppCompatActivity() {

    private val hfToken = "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"
    private val eligibilityUrl = "https://yxho-distribution-ai.hf.space/predict"
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_bantuan)

        val editTextKegunaan = findViewById<EditText>(R.id.editTextKegunaan)
        val editTextJumlah = findViewById<EditText>(R.id.editTextJumlah)
        val buttonHantar = findViewById<Button>(R.id.zakatTernakanButton)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        buttonHantar.setOnClickListener {
            val kegunaan = editTextKegunaan.text.toString()
            val jumlah = editTextJumlah.text.toString()

            if (kegunaan.isBlank() || jumlah.isBlank()) {
                Toast.makeText(this, "Sila isi semua medan", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            submitBantuanRequest(kegunaan, jumlah)
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomepageReceiverActivity::class.java))
                    true
                }
                R.id.nav_settings -> {
                    startActivity(Intent(this, SettingsRecActivity::class.java))
                    true
                }
                else -> false
            }
        }
        bottomNav.selectedItemId = R.id.nav_history
    }

    private fun submitBantuanRequest(kegunaan: String, jumlah: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val uid = auth.currentUser?.uid ?: throw Exception("Pengguna tidak dijumpai")

                val docSnapshot = firestore.collection("PenerimaAPI").document(uid).get().await()
                if (!docSnapshot.exists()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MohonBantuanActivity, "Maklumat pengguna tiada dalam Firestore.", Toast.LENGTH_LONG).show()
                    }
                    return@launch
                }

                val rawData = docSnapshot.data ?: throw Exception("Data kosong dalam dokumen Firestore.")
                val userData = JSONObject()

                for ((key, value) in rawData) {
                    try {
                        Log.d("MohonBantuan", "Processing key: $key => $value")

                        when (key) {
                            "Sumber Pendapatan Bulanan", "Perbelanjaan Bulanan" -> {
                                val jsonStr = when (value) {
                                    is Map<*, *> -> JSONObject(value).toString()
                                    is JSONObject -> value.toString()
                                    is String -> value
                                    else -> JSONObject().toString()
                                }
                                userData.put(key, jsonStr)
                                Log.d("MohonBantuan", "✅ Forced JSON string for $key: $jsonStr")
                            }

                            "Poligami" -> {
                                val number = when (value) {
                                    is Number -> value.toInt()
                                    is String -> value.toIntOrNull() ?: 0
                                    else -> 0
                                }
                                userData.put(key, number)
                                Log.d("MohonBantuan", "✅ Coerced Poligami into int: $number")
                            }

                            else -> {
                                if (value is String) {
                                    val trimmed = value.trim()
                                    if ((trimmed.startsWith("{") && trimmed.endsWith("}")) ||
                                        (trimmed.startsWith("[") && trimmed.endsWith("]"))) {
                                        try {
                                            val parsed = if (trimmed.startsWith("{")) JSONObject(trimmed) else JSONArray(trimmed)
                                            userData.put(key, parsed)
                                        } catch (e: Exception) {
                                            Log.w("MohonBantuan", "Invalid JSON for key: $key, using as raw string.")
                                            userData.put(key, value)
                                        }
                                    } else {
                                        userData.put(key, value)
                                    }
                                } else {
                                    userData.put(key, value)
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Log.w("MohonBantuan", "Skipping key: $key due to error", e)
                    }
                }

                userData.put("Kegunaan", kegunaan)
                userData.put("JumlahMataWang", jumlah)

                val client = OkHttpClient()
                val body = RequestBody.create(
                    "application/json".toMediaTypeOrNull(),
                    userData.toString()
                )

                // --- First API call: eligibility + asnaf ---
                val eligibilityRequest = Request.Builder()
                    .url(eligibilityUrl)
                    .addHeader("Authorization", "Bearer $hfToken")
                    .post(body)
                    .build()

                val response1 = client.newCall(eligibilityRequest).execute()
                val responseString1 = response1.body?.string()?.trim() ?: ""
                Log.d("MohonBantuan", "📩 Raw response from eligibility API: $responseString1")

                try {
                    val json1 = JSONObject(responseString1)
                    val eligibility = json1.optInt("eligibility", -1)
                    val asnaf = json1.optString("asnaf", "Unknown")

                    userData.put("Kelayakan Ramalan", eligibility)
                    userData.put("Asnaf Ramalan", asnaf)

                    Log.d("MohonBantuan", "✅ Stored eligibility=$eligibility, asnaf=$asnaf")
                } catch (e: Exception) {
                    Log.e("MohonBantuan", "❌ Failed to parse eligibility API", e)
                    userData.put("Eligibility API Raw", responseString1)
                }

                // --- Second API call: fraud prediction ---
                val fraudRequest = Request.Builder()
                    .url("https://yxho-muamalatgo-fraud-ai.hf.space/predict")
                    .addHeader("Authorization", "Bearer $hfToken")
                    .post(body)
                    .build()

                val response2 = client.newCall(fraudRequest).execute()
                val responseString2 = response2.body?.string()?.trim() ?: ""
                Log.d("MohonBantuan", "📩 Raw response from fraud API: $responseString2")

                try {
                    val json2 = JSONObject(responseString2)
                    val fraud = try {
                        json2.getInt("fraud")
                    } catch (e: Exception) {
                        Log.e("MohonBantuan", "❌ Invalid fraud value format", e)
                        -1
                    }
                    userData.put("Penipuan Ramalan", fraud)  // ✅ Explicitly add clean int

                    Log.d("MohonBantuan", "✅ Stored fraud prediction: $fraud")
                } catch (e: Exception) {
                    Log.e("MohonBantuan", "❌ Failed to parse fraud API", e)
                    userData.put("Fraud API Raw", responseString2)
                }

                userData.put("uid", uid)
                userData.put("timestamp", System.currentTimeMillis())

                firestore.collection("PenerimaAPI").add(userData.toMap())
                    .addOnSuccessListener {
                        runOnUiThread {
                            Toast.makeText(this@MohonBantuanActivity, "Permohonan berjaya dihantar.", Toast.LENGTH_LONG).show()
                            val intent = Intent(this@MohonBantuanActivity, MohonBantuanActivity2::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    .addOnFailureListener { e ->
                        runOnUiThread {
                            Toast.makeText(this@MohonBantuanActivity, "Gagal simpan data: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }

            } catch (e: Exception) {
                Log.e("MohonBantuan", "Exception occurred", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MohonBantuanActivity, "Ralat: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    // ✅ Safely convert nested JSON structures to Map<String, Any> or List<Any>
    private fun JSONObject.toMap(): Map<String, Any?> {
        val map = mutableMapOf<String, Any?>()
        val keys = keys()
        while (keys.hasNext()) {
            val key = keys.next()
            val value = get(key)
            map[key] = when (value) {
                is JSONObject -> value.toMap()
                is JSONArray -> value.toList()
                else -> value
            }
        }
        return map
    }

    private fun JSONArray.toList(): List<Any?> {
        val list = mutableListOf<Any?>()
        for (i in 0 until length()) {
            val value = get(i)
            list.add(
                when (value) {
                    is JSONObject -> value.toMap()
                    is JSONArray -> value.toList()
                    else -> value
                }
            )
        }
        return list
    }
}
