package com.example.muamalatgo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ZakatPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_zakatpage)

        val zakatPendapatanButton = findViewById<Button>(R.id.zakatPendapatanButton)
        val zakatPerniagaanButton = findViewById<Button>(R.id.zakatPerniagaanButton)
        val zakatPertanianButton = findViewById<Button>(R.id.zakatPertanianButton)
        val zakatSimpananButton = findViewById<Button>(R.id.zakatSimpananButton)
        val zakatEmasPerakButton = findViewById<Button>(R.id.zakatEmasPerakButton)
        val zakatTernakanButton = findViewById<Button>(R.id.zakatTernakanButton)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        // Shortcut method to create and send intent
        fun goToPaymentWithType(fundType: String) {
            val intent = Intent(this, PaymentMethodActivity::class.java)
            intent.putExtra("FUND_TYPE", fundType)
            startActivity(intent)
            finish()
        }

        zakatPendapatanButton.setOnClickListener {
            goToPaymentWithType("zakat")
        }

        zakatPerniagaanButton.setOnClickListener {
            goToPaymentWithType("zakat")
        }

        zakatPertanianButton.setOnClickListener {
            goToPaymentWithType("zakat")
        }

        zakatSimpananButton.setOnClickListener {
            goToPaymentWithType("zakat")
        }

        zakatEmasPerakButton.setOnClickListener {
            goToPaymentWithType("zakat")
        }

        zakatTernakanButton.setOnClickListener {
            goToPaymentWithType("zakat")
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomepageDonorActivity::class.java))
                    true
                }
//                R.id.nav_history -> {
//                    startActivity(Intent(this, BlockRantaiDonorActivity::class.java))
//                    true
//                }
                R.id.nav_settings -> {
                    startActivity(Intent(this, SettingsDonorActivity::class.java))
                    true
                }
                else -> false
            }
        }

        bottomNav.selectedItemId = R.id.nav_history
    }
}
