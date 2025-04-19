package com.example.muamalatgo

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SetPasswordDonor : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegister: Button

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setpassword)

        etEmail = findViewById(R.id.editTextText21)       // Now correctly treated as email field
        etPassword = findViewById(R.id.editTextText22)    // Now treated as password
        btnRegister = findViewById(R.id.button7)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val donorData = intent.getSerializableExtra("donorData") as? HashMap<String, String>
        if (donorData == null || donorData["emel"].isNullOrEmpty()) {
            Toast.makeText(this, "Data pengguna tidak lengkap.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Pre-fill the email field
        etEmail.setText(donorData["emel"])
        etEmail.isEnabled = false  // Lock email input

        btnRegister.setOnClickListener {
            val password = etPassword.text.toString()

            if (password.isEmpty()) {
                Toast.makeText(this, "Sila masukkan kata laluan.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val email = donorData["emel"]!!

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uid = firebaseAuth.currentUser?.uid
                        if (uid != null) {
                            firestore.collection("Penderma").document(uid).set(donorData)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Pendaftaran berjaya!", Toast.LENGTH_LONG).show()
                                    startActivity(Intent(this, CompleteSignUpDonor::class.java))
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Gagal simpan ke Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Toast.makeText(this, "Ralat UID pengguna.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Pendaftaran gagal: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}
