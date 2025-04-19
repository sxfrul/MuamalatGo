package com.example.muamalatgo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomnavigation.BottomNavigationView

class PaymentPageActivity : AppCompatActivity() {
    private lateinit var container: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_payment)

        container = findViewById(R.id.innerConstraintLayout)
        val ewalletImage = findViewById<ImageView>(R.id.ewalletImage)
        val fpxImage = findViewById<ImageView>(R.id.fpxImage)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        val inflater = LayoutInflater.from(this)

        val showPayment1AndRedirect = {
            container.removeAllViews()
            val payment1 = inflater.inflate(R.layout.fragment_payment1, container, false)
            container.addView(payment1)

            // Immediately go to PaymentPageActivity12 after showing payment1
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, PaymentPageActivity12::class.java))
                finish() // Optional: remove this activity from the back stack
            }, 1500)
        }

        ewalletImage.setOnClickListener {
            showPayment1AndRedirect()
        }

        fpxImage.setOnClickListener {
            showPayment1AndRedirect()
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomepageDonorActivity::class.java))
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this, BlockRantaiDonorActivity::class.java))
                    true
                }
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

