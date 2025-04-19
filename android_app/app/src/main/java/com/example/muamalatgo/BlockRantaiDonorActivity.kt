package com.example.muamalatgo

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import android.view.LayoutInflater
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.widget.Toast

class BlockRantaiDonorActivity : AppCompatActivity() {
    private lateinit var oneBlockContainer: LinearLayout
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_blokrantai_donor)

        oneBlockContainer = findViewById(R.id.oneBlockContainer)
        bottomNav = findViewById(R.id.bottomNavigation)
        firestore = FirebaseFirestore.getInstance()

        fetchBlocksFromFirestore()

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)

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
        bottomNav.selectedItemId = R.id.nav_history
    }

    private fun fetchBlocksFromFirestore() {
        firestore.collection("blockchain")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val fundType = document.getString("fundType") ?: "-"
                    val amount = document.getDouble("amount") ?: 0.0
                    val hash = document.getString("hash") ?: "-"
                    val prevHash = document.getString("previousHash") ?: "-"

                    addBlockToLayout(fundType, amount, hash, prevHash)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to fetch blockchain data: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun addBlockToLayout(fundType: String, amount: Double, hash: String, prevHash: String) {
        val inflater = LayoutInflater.from(this)
        val cardView = inflater.inflate(R.layout.block_card_item, oneBlockContainer, false) as CardView

        val iconImage = cardView.findViewById<ImageView>(R.id.blockIcon)
        val fundText = cardView.findViewById<TextView>(R.id.blockFundType)
        val hashText = cardView.findViewById<TextView>(R.id.blockHash)
        val prevText = cardView.findViewById<TextView>(R.id.blockPrevHash)
        val amountText = cardView.findViewById<TextView>(R.id.blockAmount)

        fundText.text = fundType.uppercase()
        hashText.text = "Hash: $hash"
        prevText.text = "Prev: $prevHash"
        amountText.text = "+RM%.2f".format(amount)

        when (fundType.lowercase()) {
            "zakat" -> iconImage.setBackgroundResource(R.drawable.zakat_block)
            "waqf" -> iconImage.setBackgroundResource(R.drawable.waqf_block)
            "sadaqah" -> iconImage.setBackgroundResource(R.drawable.sadaqah_block)
            else -> iconImage.setBackgroundResource(R.drawable.default_block)
        }

        oneBlockContainer.addView(cardView)
    }
}