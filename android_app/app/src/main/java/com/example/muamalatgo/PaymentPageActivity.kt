package com.example.muamalatgo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import java.security.MessageDigest
import java.util.Date

class PaymentPageActivity : AppCompatActivity() {

    private lateinit var fundTypeInput: EditText
    private lateinit var amountInput: EditText
    private lateinit var submitButton: Button
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_payment12)

        fundTypeInput = findViewById(R.id.PaymentFundType)
        amountInput = findViewById(R.id.editTextJumlah)
        submitButton = findViewById(R.id.zakatTernakanButton)
        firestore = FirebaseFirestore.getInstance()

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        // Autofill fund type from intent
        val fundTypeFromIntent = intent.getStringExtra("FUND_TYPE") ?: "Unknown Fund"
        fundTypeInput.setText(fundTypeFromIntent)
        fundTypeInput.isEnabled = false // Optional: make it readonly
        Log.d("FUND_TYPE_LOG", "Received fund type: $fundTypeFromIntent")

        submitButton.setOnClickListener {
            val fundType = fundTypeInput.text.toString().trim()
            val amount = amountInput.text.toString().toDoubleOrNull()

            if (fundType.isEmpty() || amount == null) {
                Toast.makeText(this, "Sila isi semua medan dengan betul", Toast.LENGTH_SHORT).show()
            } else {
                recordTransaction(fundType, amount)
            }
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomepageDonorActivity::class.java))
                    true
                }
                R.id.nav_history -> {
                    true
                }
                R.id.nav_settings -> {
                    startActivity(Intent(this, SettingsDonorActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun recordTransaction(fundType: String, amount: Double) {
        val blockchainRef = firestore.collection("blockchain")

        // Step 1: Get the latest block
        blockchainRef
            .orderBy("index", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { result ->
                val lastBlock = result.documents.firstOrNull()
                val newIndex = (lastBlock?.getLong("index") ?: 0) + 1
                val previousHash = lastBlock?.getString("hash") ?: "0"
                val timestamp = Date().time.toString()
                val dataToHash = "$newIndex$timestamp$fundType$amount$previousHash"
                val hash = sha256(dataToHash)

                val newBlock = hashMapOf(
                    "index" to newIndex,
                    "timestamp" to timestamp,
                    "fundType" to fundType,
                    "amount" to amount,
                    "previousHash" to previousHash,
                    "hash" to hash
                )

                // Step 2: Add the new block to Firestore
                blockchainRef.add(newBlock)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Pembayaran berjaya!", Toast.LENGTH_SHORT).show()
                        redirectToSuccessPage()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Ralat ketika menyimpan block: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Ralat ketika mendapatkan block terakhir: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }


    private fun redirectToSuccessPage() {
        val intent = Intent(this, PaymentPageActivity2::class.java)
        startActivity(intent)
        finish()
    }

    private fun sha256(input: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(input.toByteArray())
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
}
