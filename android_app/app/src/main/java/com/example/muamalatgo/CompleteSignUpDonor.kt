package com.example.muamalatgo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CompleteSignUpDonor : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completesignup_donor) // ✅ Make sure this matches your XML file name

        val loginButton = findViewById<Button>(R.id.button9)

        loginButton.setOnClickListener {
            val intent = Intent(this, SigninPenderma::class.java)
            startActivity(intent)
            finish()
        }
    }
}
