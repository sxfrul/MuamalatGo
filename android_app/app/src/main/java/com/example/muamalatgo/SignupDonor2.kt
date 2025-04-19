package com.example.muamalatgo

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener

class SignupDonor2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup_donor1)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val namaEditText = findViewById<EditText>(R.id.editTextText3)
        val emelEditText = findViewById<EditText>(R.id.editTextText4)
        val nokpEditText = findViewById<EditText>(R.id.editTextText5)
        val phoneEditText = findViewById<EditText>(R.id.editTextText6)
        val pendapatanEditText = findViewById<EditText>(R.id.editTextText7)
        val kadPengenalanSpinner = findViewById<Spinner>(R.id.spinner)
        val teruskanButton = findViewById<Button>(R.id.button2)

        val kadPengenalanOptions = arrayOf("Warganegara", "Bukan Warganegara")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, kadPengenalanOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        kadPengenalanSpinner.adapter = adapter

        findViewById<TextView>(R.id.textView17).setOnClickListener {
            startActivity(Intent(this, SigninPenderma::class.java))
        }

        findViewById<TextView>(R.id.textView15).setOnClickListener {
            startActivity(Intent(this, SigninPenderma::class.java))
        }

        teruskanButton.setOnClickListener {
            val donorData = hashMapOf(
                "nama" to namaEditText.text.toString().trim(),
                "emel" to emelEditText.text.toString().trim(),
                "nokp" to nokpEditText.text.toString().trim(),
                "phone" to phoneEditText.text.toString().trim(),
                "pendapatan" to pendapatanEditText.text.toString().trim(),
                "kadPengenalan" to kadPengenalanSpinner.selectedItem.toString()
            )

            if (donorData.values.any { it.isEmpty() }) {
                Toast.makeText(this, "Sila isi semua maklumat.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, SetPasswordDonor::class.java)
            intent.putExtra("donorData", donorData)
            startActivity(intent)
        }

        namaEditText.addTextChangedListener { validateForm() }
        emelEditText.addTextChangedListener { validateForm() }
        nokpEditText.addTextChangedListener { validateForm() }
        phoneEditText.addTextChangedListener { validateForm() }
        pendapatanEditText.addTextChangedListener { validateForm() }
    }

    private fun validateForm() {
        val isFilled = listOf(
            R.id.editTextText3,
            R.id.editTextText4,
            R.id.editTextText5,
            R.id.editTextText6,
            R.id.editTextText7
        ).all { id ->
            findViewById<EditText>(id).text.toString().trim().isNotEmpty()
        }
        findViewById<Button>(R.id.button2).isEnabled = isFilled
    }
}
