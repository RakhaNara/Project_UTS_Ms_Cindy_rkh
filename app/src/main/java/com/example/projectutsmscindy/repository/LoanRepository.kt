package com.example.projectutsmscindy.repository

import com.example.projectutsmscindy.*
import kotlinx.coroutines.flow.Flow

class LoanRepository(private val dao: AppDao) {
    val allItems: Flow<List<Item>> = dao.getAllItems()
    val allTransactions: Flow<List<TransactionDetail>> = dao.getAllTransactionDetails()

    suspend fun insertItem(namaBarang: String) {
        val item = Item(nama_barang = namaBarang, status_tersedia = true)
        dao.insertItem(item)
    }

    suspend fun pinjamBarang(
        itemId: Int,
        namaBarang: String,
        namaPenyewa: String,
        kontak: String,
        tglPinjam: String,
        tglKembali: String
    ) {
        val borrowerId = dao.insertBorrower(Borrower(nama_penyewa = namaPenyewa, kontak = kontak)).toInt()

        val transaction = LoanTransaction(
            item_id = itemId,
            borrower_id = borrowerId,
            tgl_pinjam = tglPinjam,
            tgl_kembali = tglKembali,
            status_pinjam = "Dipinjam"
        )
        dao.insertTransaction(transaction)
        dao.updateItem(Item(id = itemId, nama_barang = namaBarang, status_tersedia = false))
    }

    suspend fun kembalikanBarang(transactionId: Int, itemId: Int, namaBarang: String) {
        dao.updateTransaction(
            LoanTransaction(
                id = transactionId,
                item_id = itemId,
                borrower_id = 0,
                tgl_pinjam = "",
                tgl_kembali = "",
                status_pinjam = "Dikembalikan"
            )
        )
        dao.updateItem(Item(id = itemId, nama_barang = namaBarang, status_tersedia = true))
    }
}