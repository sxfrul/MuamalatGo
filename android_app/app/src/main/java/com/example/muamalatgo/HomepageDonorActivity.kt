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

class HomepageDonorActivity : AppCompatActivity() {

    private lateinit var fullnameText: TextView
    private lateinit var nokpText: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_homepage_donor)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        fullnameText = findViewById(R.id.fullnamePenderma)
        nokpText = findViewById(R.id.nokpPenderma)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val userId = auth.currentUser?.uid
        if (userId != null) {
            firestore.collection("Penderma").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val nama = document.getString("nama") ?: "Nama Tidak Ditemui"
                        val nokp = document.getString("nokp") ?: "No KP Tidak Ditemui"
                        fullnameText.text = nama
                        nokpText.text = nokp
                    } else {
                        Toast.makeText(this, "Maklumat tidak dijumpai", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Ralat semasa mendapatkan data: ${e.message}", Toast.LENGTH_LONG).show()
                }
        } else {
            Toast.makeText(this, "Pengguna tidak dijumpai", Toast.LENGTH_SHORT).show()
        }

        val blockchainIcon: ImageView = findViewById(R.id.Blockchain)
        val zakatIcon: ImageView = findViewById(R.id.iconZakat)
        val masjidIcon: ImageView = findViewById(R.id.iconMasjid)
        val donasiIcon: ImageView = findViewById(R.id.iconDonasi)

        blockchainIcon.setOnClickListener {
            val intent = Intent(this, BlockRantaiDonorActivity::class.java)
            startActivity(intent)
        }

        zakatIcon.setOnClickListener {
            val intent = Intent(this, ZakatPageActivity::class.java)
            startActivity(intent)
        }

        masjidIcon.setOnClickListener {
            val intent = Intent(this, WaqfPageActivity::class.java)
            startActivity(intent)
        }

        donasiIcon.setOnClickListener {
            val intent = Intent(this, SadaqahPageActivity::class.java)
            startActivity(intent)
        }

        val statusButton: Button = findViewById(R.id.statusButton)
        statusButton.setOnClickListener {
            Toast.makeText(this, "Status button pressed — implement later", Toast.LENGTH_SHORT).show()
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
