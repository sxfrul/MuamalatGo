package com.example.muamalatgo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomnavigation.BottomNavigationView

class PaymentMethodActivity : AppCompatActivity() {
    private lateinit var container: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_payment)

        container = findViewById(R.id.innerConstraintLayout)
        val ewalletImage = findViewById<ImageView>(R.id.ewalletImage)
        val fpxImage = findViewById<ImageView>(R.id.fpxImage)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        // Get the fund type from previous activity (ZakatPageActivity)
        val fundType = intent.getStringExtra("FUND_TYPE") ?: "Unknown Fund"

        // Function to redirect to PaymentPageActivity12 with fund type
        val redirectToPayment12 = {
            val intent = Intent(this, PaymentPageActivity::class.java)
            intent.putExtra("FUND_TYPE", fundType)
            startActivity(intent)
            finish()
        }

        // Click listeners for payment options
        ewalletImage.setOnClickListener {
            redirectToPayment12()
        }

        fpxImage.setOnClickListener {
            redirectToPayment12()
        }

        // Bottom nav setup
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

        // Set default selected item (if needed)
        bottomNav.selectedItemId = R.id.nav_history
    }
}
