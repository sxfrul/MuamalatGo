package com.example.muamalatgo

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener

class SignupRec2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup_rec1)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val namaEditText = findViewById<EditText>(R.id.editTextText3)
        val emelEditText = findViewById<EditText>(R.id.editTextText4)
        val greenteruskanButton = findViewById<Button>(R.id.button2)
        val redteruskanButton = findViewById<Button>(R.id.button2dupe)

        findViewById<TextView>(R.id.textView17).setOnClickListener {
            startActivity(Intent(this, SigninPenerima::class.java))
        }

        findViewById<TextView>(R.id.textView15).setOnClickListener {
            startActivity(Intent(this, SigninPenerima::class.java))
        }

        greenteruskanButton.setOnClickListener {
            val receiverData = hashMapOf<String, Any>(
                "Emel" to emelEditText.text.toString().trim(),
                "nama" to namaEditText.text.toString().trim(),
                "Nama Pemohon/Institusi" to namaEditText.text.toString().trim(),

                "Alamat" to "No. 8888, Jalan Permai 7, Taman Indah, 65790 Rawang, Selangor",
                "Bandar" to "Rawang",
                "Bank" to "Maybank",
                "Cara Pembayaran" to "Tunai",
                "Daerah" to "Sepang",
                "Dokumen Lampiran Utama" to "Penyata Bank, Salinan KP Tanggungan, Sijil Nikah/Cerai/Mati, Kad Islam, Bil Utiliti",
                "Hubungan" to "Sepupu",
                "Hubungan kekeluargaan dengan kakitangan LZS?" to "Ya",
                "Jantina" to "Lelaki",
                "Jawatan" to "",
                "Jawatan (Kakitangan Berhubungan)" to "Kerani",
                "Jenis Kerja Sendiri" to "",
                "Kariah" to "Surau Al-Kauthar",
                "Kategori Asnaf" to "Fakir",
                "Kategori pemohon" to "Institusi",
                "Kesihatan" to "Sihat",
                "Maklumat Isi Rumah" to "",
                "Nama Kakitangan" to "Azmin bin Hamzah",
                "Nama Majikan" to "",
                "Nama Si Mati" to "",
                "Nama waris" to "",
                "Nama Pemegang Akaun" to "Pusat Latihan Insani",
                "Negeri" to "Selangor",
                "No. Akaun Bank" to "987654321012",
                "No. K/P (baru)/Polis/Tentera/No. Pasport" to "828342-24-5312",
                "No. Kad Pengenalan Si Mati" to "",
                "No. Tel. Majikan" to "",
                "No. Telefon Bimbit" to "018-8888999",
                "No. Telefon Rumah/Waris" to "",
                "Pejabat (Kakitangan Berhubungan)" to "LZS Cawangan Klang",
                "Pekerjaan" to "Tidak Bekerja",
                "Perbelanjaan Bulanan" to """{"Perbelanjaan Makan Minum": 9000.00, "Sewa/Ansuran Rumah": 11110.00, "Persekolahan Anak-anak": 11110, "Pengangkutan/Tambang Bas Sekolah": 11110, "Bil Elektrik, Bil Air dan Lain-lain": 1110.50, "Kos Rawatan/Sakit Kronik": 119999.99, "Kos Penjagaan Anak": 0}""",
                "Poligami" to "",
                "Poskod" to 41000,
                "Asnaf" to "Fisabilillah",
                "Eligible" to false,
                "Fraud" to true,
                "Sebab Memohon Bantuan" to "Perlukan Bantuan",
                "Sebab Pembayaran Tunai" to "",
                "Sebab Tidak Bekerja" to "",
                "Sektor" to "",
                "Status" to "Bujang",
                "Sumber Pendapatan Bulanan" to """{"Diri": 0, "Isteri/Suami/Ibu Bapa/Penjaga": 0, "Pencen/PERKESO": 0, "Sumbangan Anak-anak": 0, "Lain-Lain (JKM dan Sebagainya)": 0, "Lain-Lain Pendapatan (Contoh Sewa Rumah)": 0, "Pendapatan tanggungan yang tinggal bersama": 0.0}""",
                "Tarikh" to "1/15/2025",
                "Tarikh Lahir" to "11/9/1967",
                "Tarikh Masuk Islam" to "",
                "Tempoh Menetap di Selangor" to 1,
                "Warganegara" to "Malaysia"
            )

            if (receiverData["nama"].toString().isEmpty() || receiverData["Emel"].toString().isEmpty()) {
                Toast.makeText(this, "Sila isi nama dan emel.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, SetPasswordRec::class.java)
            intent.putExtra("receiverData", receiverData)
            startActivity(intent)
        }

        redteruskanButton.setOnClickListener {
            val receiverData = hashMapOf<String, Any>(
                "Emel" to emelEditText.text.toString().trim(),
                "nama" to namaEditText.text.toString().trim(),
                "Nama Pemohon/Institusi" to namaEditText.text.toString().trim(),

                "Alamat" to "No. 66, Jalan Permai 7, Taman Indah, 65790 Rawang, Selangor",
                "Bandar" to "Rawang",
                "Bank" to "Maybank",
                "Cara Pembayaran" to "Tunai",
                "Daerah" to "Sepang",
                "Dokumen Lampiran Utama" to "Penyata Bank, Salinan KP Tanggungan, Sijil Nikah/Cerai/Mati, Kad Islam, Bil Utiliti",
                "Hubungan" to "Sepupu",
                "Hubungan kekeluargaan dengan kakitangan LZS?" to "Ya",
                "Jantina" to "Lelaki",
                "Jawatan" to "",
                "Jawatan (Kakitangan Berhubungan)" to "Kerani",
                "Jenis Kerja Sendiri" to "",
                "Kariah" to "Surau Al-Kauthar",
                "Kategori Asnaf" to "Fakir",
                "Kategori pemohon" to "Institusi",
                "Kesihatan" to "Sihat",
                "Maklumat Isi Rumah" to "",
                "Nama Kakitangan" to "Azmin bin Hamzah",
                "Nama Majikan" to "",
                "Nama Si Mati" to "",
                "Nama waris" to "",
                "Nama Pemegang Akaun" to "Pusat Latihan Insani",
                "Negeri" to "Selangor",
                "No. Akaun Bank" to "987654321012",
                "No. K/P (baru)/Polis/Tentera/No. Pasport" to "828342-24-5312",
                "No. Kad Pengenalan Si Mati" to "",
                "No. Tel. Majikan" to "",
                "No. Telefon Bimbit" to "018-8888999",
                "No. Telefon Rumah/Waris" to "",
                "Pejabat (Kakitangan Berhubungan)" to "LZS Cawangan Klang",
                "Pekerjaan" to "Tidak Bekerja",
                "Perbelanjaan Bulanan" to """{"Perbelanjaan Makan Minum": 3500.00, "Sewa/Ansuran Rumah": 10.00, "Persekolahan Anak-anak": 0, "Pengangkutan/Tambang Bas Sekolah": 0, "Bil Elektrik, Bil Air dan Lain-lain": 0.50, "Kos Rawatan/Sakit Kronik": 9999.99, "Kos Penjagaan Anak": 0}""",
                "Poligami" to "",
                "Poskod" to 41000,
                "Asnaf" to "Fisabilillah",
                "Eligible" to false,
                "Fraud" to true,
                "Sebab Memohon Bantuan" to "Perlukan Bantuan",
                "Sebab Pembayaran Tunai" to "",
                "Sebab Tidak Bekerja" to "",
                "Sektor" to "",
                "Status" to "Bujang",
                "Sumber Pendapatan Bulanan" to """{"Diri": 0, "Isteri/Suami/Ibu Bapa/Penjaga": 0, "Pencen/PERKESO": 0, "Sumbangan Anak-anak": 0, "Lain-Lain (JKM dan Sebagainya)": 0, "Lain-Lain Pendapatan (Contoh Sewa Rumah)": 0, "Pendapatan tanggungan yang tinggal bersama": 0.0}""",
                "Tarikh" to "1/15/2025",
                "Tarikh Lahir" to "11/9/1967",
                "Tarikh Masuk Islam" to "",
                "Tempoh Menetap di Selangor" to 1,
                "Warganegara" to "Malaysia"
            )

            if (receiverData["nama"].toString().isEmpty() || receiverData["Emel"].toString().isEmpty()) {
                Toast.makeText(this, "Sila isi nama dan emel.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, SetPasswordRec::class.java)
            intent.putExtra("receiverData", receiverData)
            startActivity(intent)
        }

        namaEditText.addTextChangedListener { validateForm() }
        emelEditText.addTextChangedListener { validateForm() }
    }

    private fun validateForm() {
        val isFilled = listOf(
            R.id.editTextText3,
            R.id.editTextText4
        ).all { id ->
            findViewById<EditText>(id).text.toString().trim().isNotEmpty()
        }
        findViewById<Button>(R.id.button2).isEnabled = isFilled
    }
}
