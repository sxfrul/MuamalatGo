package com.example.muamalatgo.backend

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object RegisterPenderma {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun registerPendermaUser(
        email: String,
        password: String,
        pendermaData: Map<String, String>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val uid = authResult.user?.uid
                if (uid != null) {
                    firestore.collection("Penderma").document(uid)
                        .set(pendermaData)
                        .addOnSuccessListener {
                            onSuccess()
                        }
                        .addOnFailureListener { error ->
                            onFailure(error)
                        }
                } else {
                    onFailure(Exception("User ID not found after registration"))
                }
            }
            .addOnFailureListener { error ->
                onFailure(error)
            }
    }
}
