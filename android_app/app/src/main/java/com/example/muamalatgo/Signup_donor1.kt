package com.example.muamalatgo

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.View
import androidx.core.widget.addTextChangedListener

class Signup_donor1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup_donor1)

        // Set up edge-to-edge padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize UI components
        val namaEditText = findViewById<EditText>(R.id.editTextText3)
        val emelEditText = findViewById<EditText>(R.id.editTextText4)
        val nokpEditText = findViewById<EditText>(R.id.editTextText5)
        val phoneEditText = findViewById<EditText>(R.id.editTextText6)
        val pendapatanEditText = findViewById<EditText>(R.id.editTextText7)
        val kadPengenalanSpinner = findViewById<Spinner>(R.id.spinner)
        val teruskanButton = findViewById<Button>(R.id.button2)

        // Spinner setup
        val kadPengenalanOptions = arrayOf("Warganegara", "Bukan Warganegara")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, kadPengenalanOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        kadPengenalanSpinner.adapter = adapter

        // Back navigation to Signup_donor
        val kembaliText = findViewById<TextView>(R.id.textView17)
        kembaliText.setOnClickListener {
            val intent = Intent(this, Signup_donor::class.java)
            startActivity(intent)
        }

        // Navigation to Signin
        val logMasukText = findViewById<TextView>(R.id.textView15)
        logMasukText.setOnClickListener {
            val intent = Intent(this, Signin::class.java)
            startActivity(intent)
        }

        // "Teruskan" button logic (to proceed to Setpassword)
        teruskanButton.setOnClickListener {
            val nama = namaEditText.text.toString().trim()
            val emel = emelEditText.text.toString().trim()
            val nokp = nokpEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()
            val pendapatan = pendapatanEditText.text.toString().trim()
            val kadPengenalan = kadPengenalanSpinner.selectedItem.toString()

            // Basic validation
            if (nama.isEmpty() || emel.isEmpty() || nokp.isEmpty() || phone.isEmpty() || pendapatan.isEmpty()) {
                Toast.makeText(this, "Sila isi semua maklumat.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Pass data to Setpassword.kt
            val intent = Intent(this, Setpassword::class.java).apply {
                putExtra("nama", nama)
                putExtra("emel", emel)
                putExtra("nokp", nokp)
                putExtra("phone", phone)
                putExtra("pendapatan", pendapatan)
                putExtra("kadPengenalan", kadPengenalan)
            }
            startActivity(intent)
        }

        // Optional: Auto-validation for each field (e.g., enable/disable buttons based on input)
        namaEditText.addTextChangedListener { validateForm() }
        emelEditText.addTextChangedListener { validateForm() }
        nokpEditText.addTextChangedListener { validateForm() }
        phoneEditText.addTextChangedListener { validateForm() }
        pendapatanEditText.addTextChangedListener { validateForm() }
    }

    // Validate if all fields are filled
    private fun validateForm() {
        val nama = findViewById<EditText>(R.id.editTextText3).text.toString().trim()
        val emel = findViewById<EditText>(R.id.editTextText4).text.toString().trim()
        val nokp = findViewById<EditText>(R.id.editTextText5).text.toString().trim()
        val phone = findViewById<EditText>(R.id.editTextText6).text.toString().trim()
        val pendapatan = findViewById<EditText>(R.id.editTextText7).text.toString().trim()

        // Enable or disable the "teruskanButton" based on form completion
        findViewById<Button>(R.id.button2).isEnabled = nome.isNotEmpty() && emel.isNotEmpty() && nokp.isNotEmpty() && phone.isNotEmpty() && pendapatan.isNotEmpty()
    }
}
