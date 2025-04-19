package com.example.muamalatgo

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
        // Register user with email and password using Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                // Get user ID (UID) from the authentication result
                val uid = authResult.user?.uid ?: return@addOnSuccessListener

                // Save additional user data to Firestore
                db.collection("Penderma").document(uid)
                    .set(pendermaData)
                    .addOnSuccessListener {
                        Log.d("RegisterPenderma", "Penderma data saved successfully.")
                        onSuccess() // Call onSuccess callback if successful
                    }
                    .addOnFailureListener { exception ->
                        Log.e("RegisterPenderma", "Failed to save Penderma data: ${exception.message}")
                        onFailure(exception) // Call onFailure callback if saving fails
                    }

            }.addOnFailureListener { exception ->
                Log.e("RegisterPenderma", "Failed to register: ${exception.message}")
                onFailure(exception) // Call onFailure callback if registration fails
            }
    }
}
