package com.example.muamalatgo.backend

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.muamalatgo.Completesignup
import com.example.muamalatgo.MainActivity
import com.example.muamalatgo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Setpassword : AppCompatActivity() {

    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setpassword)

        etPassword = findViewById(R.id.editTextText21)
        etConfirmPassword = findViewById(R.id.editTextText22)
        btnRegister = findViewById(R.id.button7)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val donorData = intent.getSerializableExtra("donorData") as? HashMap<String, String>
            ?: run {
                Toast.makeText(this, "Data pengguna tidak lengkap.", Toast.LENGTH_SHORT).show()
                finish()
                return
            }

        btnRegister.setOnClickListener {
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            if (password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Sila isi semua kata laluan.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Kata laluan tidak sepadan.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val email = donorData["emel"] ?: run {
                Toast.makeText(this, "Emel tidak dijumpai.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uid = firebaseAuth.currentUser?.uid
                        if (uid != null) {
                            firestore.collection("Penderma").document(uid).set(donorData)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Pendaftaran berjaya!", Toast.LENGTH_LONG).show()

                                    // After successful registration, navigate to Completesignup activity
                                    val intent = Intent(this, Completesignup::class.java)
                                    startActivity(intent)
                                    finish()  // Close the current activity (Setpassword)
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
