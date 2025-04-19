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

class Signup_receiver8_9 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup_receiver8_9)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val statusItems = listOf("Bujang", "Berkahwin", "Lain-lain")
        val kesihatanItems = listOf("Sihat", "Sakit", "Cacat")

        // Setup spinners
        setupSpinner(R.id.spinner6, statusItems, "Status 1:")
        setupSpinner(R.id.spinner7, kesihatanItems, "Kesihatan 1:")
        setupSpinner(R.id.spinner8, statusItems, "Status 2:")
        setupSpinner(R.id.spinner9, kesihatanItems, "Kesihatan 2:")
        setupSpinner(R.id.spinner10, statusItems, "Status 3:")
        setupSpinner(R.id.spinner11, kesihatanItems, "Kesihatan 3:")
    }

    private fun setupSpinner(spinnerId: Int, items: List<String>, toastPrefix: String) {
        val spinner: Spinner = findViewById(spinnerId)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selected = parent?.getItemAtPosition(position).toString()
                Toast.makeText(this@Signup_receiver8_9, "$toastPrefix $selected", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}
