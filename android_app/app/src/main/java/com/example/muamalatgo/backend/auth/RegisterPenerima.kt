package my.app.com.example.muamalatgo.backend.auth

import my.app.FirebaseInit.firestore
import my.app.FirebaseInit.auth
import com.google.firebase.auth.UserRecord

fun registerPenerima(
    email: String,
    password: String,
    penerimaData: Map<String, Any>
) {
    val request = UserRecord.CreateRequest()
        .setEmail(email)
        .setPassword(password)

    val userRecord = auth.createUser(request)
    val uid = userRecord.uid

    firestore.collection("Penerima").document(uid).set(penerimaData)
    println("Penerima registered: $uid")
}