package com.example.muamalatgo.backend.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log

object RegisterPenderma {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun registerPendermaUser(
        email: String,
        password: String,
        pendermaData: Map<String, Any>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val uid = authResult.user?.uid ?: return@addOnSuccessListener

                db.collection("Penderma").document(uid)
                    .set(pendermaData)
                    .addOnSuccessListener {
                        Log.d("RegisterPenderma", "Penderma data saved successfully.")
                        onSuccess()
                    }
                    .addOnFailureListener { exception ->
                        Log.e("RegisterPenderma", "Failed to save Penderma data: ${exception.message}")
                        onFailure(exception)
                    }

            }.addOnFailureListener { exception ->
                Log.e("RegisterPenderma", "Failed to register: ${exception.message}")
                onFailure(exception)
            }
    }
}
