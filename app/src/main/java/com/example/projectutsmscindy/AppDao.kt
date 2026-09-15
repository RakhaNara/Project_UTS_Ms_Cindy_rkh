package com.example.projectutsmscindy

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: Item)

    @Update
    suspend fun updateItem(item: Item)

    @Query("SELECT * FROM items ORDER BY id DESC")
    fun getAllItems(): Flow<List<Item>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBorrower(borrower: Borrower): Long

    @Query("SELECT * FROM borrowers ORDER BY id DESC")
    fun getAllBorrowers(): Flow<List<Borrower>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: LoanTransaction)

    @Update
    suspend fun updateTransaction(transaction: LoanTransaction)

    @Query("""
        SELECT 
            t.id AS transactionId,
            i.nama_barang AS namaBarang,
            b.nama_penyewa AS namaPenyewa,
            b.kontak AS kontak,
            t.tgl_pinjam AS tglPinjam,
            t.tgl_kembali AS tglKembali,
            t.status_pinjam AS statusPinjam,
            i.id AS itemId
        FROM loan_transactions t
        INNER JOIN items i ON t.item_id = i.id
        INNER JOIN borrowers b ON t.borrower_id = b.id
        ORDER BY t.id DESC
    """)
    fun getAllTransactionDetails(): Flow<List<TransactionDetail>>
}