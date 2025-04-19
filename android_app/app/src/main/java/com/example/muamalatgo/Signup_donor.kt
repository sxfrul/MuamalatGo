package com.example.muamalatgo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Signup_donor : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup_donor)

        // Handle window insets for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 🔵 Reference to the buttons
        val penerimaButton = findViewById<Button>(R.id.button15) // "Penerima"
        val teruskanButton = findViewById<Button>(R.id.button3) // "Teruskan"

        // 🔁 Switch to Signup_receiver.kt when "Penerima" is clicked
        penerimaButton.setOnClickListener {
            val intent = Intent(this, Signup_receiver::class.java)
            startActivity(intent)
            finish()
        }

        // ✅ Go to Signup_donor1.kt when "Teruskan" is clicked
        teruskanButton.setOnClickListener {
            val intent = Intent(this, Signup_donor1::class.java)
            startActivity(intent)
        }
    }
}
