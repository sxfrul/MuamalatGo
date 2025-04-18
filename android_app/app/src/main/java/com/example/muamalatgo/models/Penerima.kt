package com.example.muamalatgo.models

data class Penerima(
    val kategoriAsnaf: String = "",
    val sebabMemohonBantuan: String = "",
    val nokp: String = "",
    val nama: String = "",
    val alamat: String = "",
    val daerah: String = "",
    val poskod: String = "",
    val bandar: String = "",
    val negeri: String = "",
    val emel: String = "",
    val telefon: String = "",
    val warganegara: String = "",
    val tarikhLahir: String = "",
    val sumberPendapatan: Double = 0.0,
    val perbelanjaanBulanan: Double = 0.0,
    val fraudLabel: String = ""
)
