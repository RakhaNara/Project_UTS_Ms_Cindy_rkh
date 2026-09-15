package com.example.projectutsmscindy

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nama_barang: String,
    val status_tersedia: Boolean = true
)

@Entity(tableName = "borrowers")
data class Borrower(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nama_penyewa: String,
    val kontak: String
)

@Entity(tableName = "loan_transactions")
data class LoanTransaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val item_id: Int,
    val borrower_id: Int,
    val tgl_pinjam: String,
    val tgl_kembali: String,
    val status_pinjam: String
)

data class TransactionDetail(
    val transactionId: Int,
    val namaBarang: String,
    val namaPenyewa: String,
    val kontak: String,
    val tglPinjam: String,
    val tglKembali: String,
    val statusPinjam: String,
    val itemId: Int
)