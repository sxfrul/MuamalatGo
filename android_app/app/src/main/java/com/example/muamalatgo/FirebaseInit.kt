package my.app

import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.cloud.FirestoreClient
import com.google.firebase.auth.FirebaseAuth
import java.io.FileInputStream

object FirebaseInit {
    init {
        val serviceAccount = FileInputStream("umhackathon-zakatapp-mvp-firebase-adminsdk-fbsvc-cb4cb7e7f6.json")

        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build()

        FirebaseApp.initializeApp(options)
    }

    val firestore by lazy { FirestoreClient.getFirestore() }
    val auth by lazy { FirebaseAuth.getInstance() }
}