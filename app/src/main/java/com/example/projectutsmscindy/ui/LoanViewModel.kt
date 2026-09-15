package com.example.projectutsmscindy.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectutsmscindy.*
import com.example.projectutsmscindy.repository.LoanRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LoanViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: LoanRepository

    val itemList: StateFlow<List<Item>>
    val transactionList: StateFlow<List<TransactionDetail>>

    init {
        val dao = AppDatabase.getDatabase(application).appDao()
        repository = LoanRepository(dao)

        itemList = repository.allItems.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        transactionList = repository.allTransactions.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
    }

    fun tambahBarang(namaBarang: String) {
        viewModelScope.launch {
            if (namaBarang.isNotBlank()) {
                repository.insertItem(namaBarang)
            }
        }
    }

    fun catatPinjaman(
        itemId: Int,
        namaBarang: String,
        namaPenyewa: String,
        kontak: String,
        tglPinjam: String,
        tglKembali: String
    ) {
        viewModelScope.launch {
            repository.pinjamBarang(itemId, namaBarang, namaPenyewa, kontak, tglPinjam, tglKembali)
        }
    }

    fun kembalikanBarang(transactionId: Int, itemId: Int, namaBarang: String) {
        viewModelScope.launch {
            repository.kembalikanBarang(transactionId, itemId, namaBarang)
        }
    }
}