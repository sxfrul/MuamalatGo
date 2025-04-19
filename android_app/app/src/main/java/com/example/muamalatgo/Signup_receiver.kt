package com.example.muamalatgo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Signup_receiver : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup_receiver)

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 🔵 Add these lines for button logic
        val pendermaButton = findViewById<Button>(R.id.button18) // "Penderma"
        val teruskanButton = findViewById<Button>(R.id.button16) // "Teruskan"

        // 🔁 Switch to Signup_donor.kt when "Penderma" is clicked
        pendermaButton.setOnClickListener {
            val intent = Intent(this, Signup_donor::class.java)
            startActivity(intent)
            finish()
        }

        // ✅ Go to Signup_receiver1.kt when "Teruskan" is clicked
        teruskanButton.setOnClickListener {
            val intent = Intent(this, Signup_receiver1::class.java)
            startActivity(intent)
        }
    }
}
