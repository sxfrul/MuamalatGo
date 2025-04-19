package com.example.muamalatgo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.Intent
import android.widget.Button

class WaqfPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.fragment_waqfpage)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        val dermaButton1 = findViewById<Button>(R.id.buttonDerma1)
        val dermaButton2 = findViewById<Button>(R.id.buttonDerma2)
        val dermaButton3 = findViewById<Button>(R.id.buttonDerma3)
        val dermaButton4 = findViewById<Button>(R.id.buttonDerma4)

        val paymentIntent = Intent(this, PaymentPageActivity::class.java)

        dermaButton1.setOnClickListener {
            startActivity(paymentIntent)
            finish()
        }

        dermaButton2.setOnClickListener {
            startActivity(paymentIntent)
            finish()
        }

        dermaButton3.setOnClickListener {
            startActivity(paymentIntent)
            finish()
        }

        dermaButton4.setOnClickListener {
            startActivity(paymentIntent)
            finish()
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomepageDonorActivity::class.java))
                    true
                }
                R.id.nav_history -> {
                    true
                }
                R.id.nav_settings -> {
                    true
                }
                else -> false
            }
        }
    }

}