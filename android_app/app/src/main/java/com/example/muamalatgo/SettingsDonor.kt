package com.example.muamalatgo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsDonorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_settings_donor)

        val editProfileButton: Button = findViewById(R.id.zakatPendapatanButton)
        val bahasaButton: Button = findViewById(R.id.zakatPerniagaanButton)
        val tukarPasswordButton: Button = findViewById(R.id.zakatPertanianButton)
        val aboutUsButton: Button = findViewById(R.id.zakatSimpananButton)
        val privacyPolicyButton: Button = findViewById(R.id.zakatEmasPerakButton)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        editProfileButton.setOnClickListener {
            Toast.makeText(this, "Edit Profile clicked", Toast.LENGTH_SHORT).show()
        }

        bahasaButton.setOnClickListener {
            Toast.makeText(this, "Bahasa clicked", Toast.LENGTH_SHORT).show()
        }

        tukarPasswordButton.setOnClickListener {
            Toast.makeText(this, "Tukar Kata Laluan clicked", Toast.LENGTH_SHORT).show()
        }

        aboutUsButton.setOnClickListener {
            Toast.makeText(this, "Mengenai Kami clicked", Toast.LENGTH_SHORT).show()
        }

        privacyPolicyButton.setOnClickListener {
            Toast.makeText(this, "Dasar Privasi clicked", Toast.LENGTH_SHORT).show()
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "Home clicked", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomepageDonorActivity::class.java))
                    true
                }
                R.id.nav_history -> {
                    Toast.makeText(this, "History clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_settings -> {
                    Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        bottomNav.selectedItemId = R.id.nav_settings
    }
}
