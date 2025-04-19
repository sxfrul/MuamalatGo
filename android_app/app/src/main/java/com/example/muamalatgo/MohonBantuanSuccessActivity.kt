package com.example.muamalatgo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MohonBantuanActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_bantuan2)

        // Close button logic
        val closeButton: ImageView = findViewById(R.id.closeButton)
        closeButton.setOnClickListener {
            val intent = Intent(this, HomepageReceiverActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish() // kill this activity
        }
    }
}