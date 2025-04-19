package com.example.muamalatgo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.Intent
import android.widget.Button

class SadaqahPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.fragment_sadaqahpage)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        val dermaButton1 = findViewById<Button>(R.id.buttonDerma1)
        val dermaButton2 = findViewById<Button>(R.id.buttonDerma2)
        val dermaButton3 = findViewById<Button>(R.id.buttonDerma3)
        val dermaButton4 = findViewById<Button>(R.id.buttonDerma4)

        fun goToPaymentWithType(fundType: String) {
            val intent = Intent(this, PaymentMethodActivity::class.java)
            intent.putExtra("FUND_TYPE", fundType)
            startActivity(intent)
            finish()
        }

        dermaButton1.setOnClickListener {
            goToPaymentWithType("sadaqah")
        }

        dermaButton2.setOnClickListener {
            goToPaymentWithType("sadaqah")
        }

        dermaButton3.setOnClickListener {
            goToPaymentWithType("sadaqah")
        }

        dermaButton4.setOnClickListener {
            goToPaymentWithType("sadaqah")
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
                    startActivity(Intent(this, SettingsDonorActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
