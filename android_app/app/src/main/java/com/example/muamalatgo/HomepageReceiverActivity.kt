package com.example.muamalatgo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomepageReceiverActivity : AppCompatActivity() {

    private lateinit var fullnameText: TextView
    private lateinit var nokpText: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_homepage_rec)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        fullnameText = findViewById(R.id.headerText)
        nokpText = findViewById(R.id.descriptionText)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser == null) {
            Toast.makeText(this, "Pengguna tidak dijumpai. Sila log masuk semula.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, SigninPenerima::class.java))
            finish()
            return
        }

        val userId = currentUser.uid
        firestore.collection("PenerimaAPI").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {

                    val rawData = document.data!!
                    val nokp = rawData["No. K/P (baru)/Polis/Tentera/No. Pasport"] as? String ?: "No KP Tidak Ditemui"

                    val nama = rawData["Nama Pemohon/Institusi"] as? String ?: "Nama Tidak Ditemui"

                    fullnameText.text = nama
                    nokpText.text = nokp
                } else {
                    Toast.makeText(this, "Maklumat tidak dijumpai", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Ralat semasa mendapatkan data: ${e.message}", Toast.LENGTH_LONG).show()
            }

        val blockchainIcon: ImageView = findViewById(R.id.icon1)
        val bantuanIcon: ImageView = findViewById(R.id.icon2)

        blockchainIcon.setOnClickListener {
            val intent = Intent(this, BlockRantaiRecActivity::class.java)
            startActivity(intent)
        }

        bantuanIcon.setOnClickListener {
            val intent = Intent(this, MohonBantuanActivity::class.java)
            startActivity(intent)
        }

        val statusButton: Button = findViewById(R.id.statusButton)
        statusButton.setOnClickListener {
            Toast.makeText(this, "Not Implemented", Toast.LENGTH_SHORT).show()
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomepageReceiverActivity::class.java))
                    true
                }
                R.id.nav_history -> {
                    true
                }
                R.id.nav_settings -> {
                    startActivity(Intent(this, SettingsRecActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
