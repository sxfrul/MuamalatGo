package com.example.muamalatgo

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Setpassword2 : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegister: Button

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setpassword)

        etEmail = findViewById(R.id.editTextText21)
        etPassword = findViewById(R.id.editTextText22)
        btnRegister = findViewById(R.id.button7)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Cast as HashMap<String, Any> to support mixed types (String, Int, Boolean, etc)
        val receiverData = intent.getSerializableExtra("receiverData") as? HashMap<String, Any>

        if (receiverData == null || receiverData["Emel"].toString().isEmpty()) {
            Toast.makeText(this, "Data penerima tidak lengkap.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        etEmail.setText(receiverData["Emel"].toString())
        etEmail.isEnabled = false

        btnRegister.setOnClickListener {
            val password = etPassword.text.toString()

            if (password.isEmpty()) {
                Toast.makeText(this, "Sila masukkan kata laluan.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val email = receiverData["Emel"].toString()

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uid = firebaseAuth.currentUser?.uid
                        if (uid != null) {
                            firestore.collection("PenerimaAPI").document(uid).set(receiverData)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Pendaftaran berjaya!", Toast.LENGTH_LONG).show()
                                    startActivity(Intent(this, Completesignup::class.java))
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
