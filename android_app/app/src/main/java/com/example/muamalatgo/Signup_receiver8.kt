package com.example.muamalatgo

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import android.view.View
import android.widget.AdapterView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Signup_receiver8 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup_receiver8)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val spinnerStatus = findViewById<Spinner>(R.id.spinnerStatus)
        val listItems6 = listOf("Bujang", "Berkahwin", "Lain-lain")
        val adapter6 = ArrayAdapter(this, android.R.layout.simple_spinner_item, listItems6)
        adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStatus.adapter = adapter6

        spinnerStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selected = parent?.getItemAtPosition(position).toString()
                Toast.makeText(this@Signup_receiver8, "Anda pilih status: $selected", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val spinnerHealth = findViewById<Spinner>(R.id.spinnerHealth)
        val listItems7 = listOf("Sihat", "Sakit", "Cacat")
        val adapter7 = ArrayAdapter(this, android.R.layout.simple_spinner_item, listItems7)
        adapter7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerHealth.adapter = adapter7

        spinnerHealth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selected = parent?.getItemAtPosition(position).toString()
                Toast.makeText(this@Signup_receiver8, "Anda pilih kesihatan: $selected", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}