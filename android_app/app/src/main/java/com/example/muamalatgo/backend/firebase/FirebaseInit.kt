package com.example.muamalatgo.backend.com.example.muamalatgo.backend.firebase

import android.content.Context
import com.google.firebase.FirebaseApp

object FirebaseInit {
    fun initialize(context: Context) {
        if (FirebaseApp.getApps(context).isEmpty()) {
            FirebaseApp.initializeApp(context)
        }
    }
}
