package com.example.muamalatgo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.security.MessageDigest
import java.util.Date

class PaymentPageActivity12 : AppCompatActivity() {
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

        submitButton.setOnClickListener {
            val fundType = fundTypeInput.text.toString().trim()
            val amount = amountInput.text.toString().toDoubleOrNull()

            if (fundType.isEmpty() || amount == null) {
                Toast.makeText(this, "Sila isi semua medan dengan betul", Toast.LENGTH_SHORT).show()
            } else {
                recordTransaction(fundType, amount)
            }
        }
    }

    private fun recordTransaction(fundType: String, amount: Double) {
        firestore.collection("blockchain")
            .orderBy("index", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { result ->
                val lastBlock = result.documents.firstOrNull()
                val previousHash = lastBlock?.getString("hash") ?: "0"
                val newIndex = (lastBlock?.getLong("index") ?: 0) + 1
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

                firestore.collection("blockchain").add(newBlock)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Pembayaran berjaya!", Toast.LENGTH_SHORT).show()
                        redirectToSuccessPage()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Ralat: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Ralat: ${e.message}", Toast.LENGTH_LONG).show()
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
