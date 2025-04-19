package com.example.muamalatgo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import android.view.View
import android.widget.AdapterView
import android.content.Intent
import android.widget.TextView
import android.widget.*


class Signup_donor1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup_donor1)

        // Initialize views
        val namaEditText = findViewById<EditText>(R.id.editTextText3)
        val emelEditText = findViewById<EditText>(R.id.editTextText4)
        val nokpEditText = findViewById<EditText>(R.id.editTextText5)
        val phoneEditText = findViewById<EditText>(R.id.editTextText6)
        val pendapatanEditText = findViewById<EditText>(R.id.editTextText7)
        val kadPengenalanSpinner = findViewById<Spinner>(R.id.spinner)
        val teruskanButton = findViewById<Button>(R.id.button2)

        // Spinner setup (optional if already set in XML)
        val kadPengenalanOptions = arrayOf("Warganegara", "Bukan Warganegara")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, kadPengenalanOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        kadPengenalanSpinner.adapter = adapter

        // Button click listener
        teruskanButton.setOnClickListener {
            val nama = namaEditText.text.toString().trim()
            val emel = emelEditText.text.toString().trim()
            val nokp = nokpEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()
            val pendapatan = pendapatanEditText.text.toString().trim()
            val kadPengenalan = kadPengenalanSpinner.selectedItem.toString()

            // Basic validation (optional)
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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val spinner: Spinner = findViewById(R.id.spinner)

        val listItems = listOf("No. K/P(baru)", "No. Polis","No. Tentera","No. Passport")
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listItems)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selected = parent?.getItemAtPosition(position).toString()
                Toast.makeText(this@Signup_donor1, "Anda pilih: $selected", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        val kembaliText = findViewById<TextView>(R.id.textView17)
        val logMasukText = findViewById<TextView>(R.id.textView15)

        kembaliText.setOnClickListener {
            val intent = Intent(this, Signup_donor::class.java)
            startActivity(intent)

        }

        logMasukText.setOnClickListener {
            val intent = Intent(this, Signin::class.java)
            startActivity(intent)
        }

    }
}