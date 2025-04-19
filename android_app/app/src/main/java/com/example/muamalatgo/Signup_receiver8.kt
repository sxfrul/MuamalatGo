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

        val spinner6: Spinner = findViewById(R.id.spinner6)
        val listItems6 = listOf("Bujang", "Berkahwin", "Lain-lain")
        val adapter6 = ArrayAdapter(this, android.R.layout.simple_spinner_item, listItems6)
        adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner6.adapter = adapter6

        spinner6.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selected = parent?.getItemAtPosition(position).toString()
                Toast.makeText(this@Signup_receiver8, "Anda pilih status: $selected", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val spinner7: Spinner = findViewById(R.id.spinner7)
        val listItems7 = listOf("Sihat", "Sakit", "Cacat")
        val adapter7 = ArrayAdapter(this, android.R.layout.simple_spinner_item, listItems7)
        adapter7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner7.adapter = adapter7

        spinner7.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selected = parent?.getItemAtPosition(position).toString()
                Toast.makeText(this@Signup_receiver8, "Anda pilih kesihatan: $selected", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}