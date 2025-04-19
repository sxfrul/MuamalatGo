package com.example.muamalatgo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SignupRec : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup_receiver)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 🔵 Reference to the buttons
        val penerimaButton = findViewById<Button>(R.id.button18) // "Penerima"
        val teruskanButton = findViewById<Button>(R.id.button16) // "Teruskan"

        // 🔁 Switch to Signup_receiver.kt when "Penerima" is clicked
        penerimaButton.setOnClickListener {
            val intent = Intent(this, SignupRec::class.java)
            startActivity(intent)
            finish()
        }

        // ✅ Go to Signup_donor1.kt when "Teruskan" is clicked
        teruskanButton.setOnClickListener {
            val intent = Intent(this, SignupRec2::class.java)
            startActivity(intent)
        }
    }
}