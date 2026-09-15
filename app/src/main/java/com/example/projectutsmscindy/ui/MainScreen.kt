package com.example.projectutsmscindy.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectutsmscindy.Item
import com.example.projectutsmscindy.TransactionDetail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: LoanViewModel) {
    val items by viewModel.itemList.collectAsState()
    val transactions by viewModel.transactionList.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Rental & Loan Tracker",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
        ) {
            PrimaryTabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Inventaris Barang", fontWeight = FontWeight.SemiBold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Riwayat Pinjaman", fontWeight = FontWeight.SemiBold) }
                )
            }

            AnimatedContent(
                targetState = selectedTab,
                label = "tab_animation"
            ) { tabIndex ->
                when (tabIndex) {
                    0 -> BarangTabContent(
                        items = items,
                        onTambahBarang = { viewModel.tambahBarang(it) },
                        onPinjamBarang = { id, namaB, namaP, kontak, tglP, tglK ->
                            viewModel.catatPinjaman(id, namaB, namaP, kontak, tglP, tglK)
                        }
                    )
                    1 -> TransaksiTabContent(
                        transactions = transactions,
                        onKembalikan = viewModel::kembalikanBarang
                    )
                }
            }
        }
    }
}

@Composable
fun BarangTabContent(
    items: List<Item>,
    onTambahBarang: (String) -> Unit,
    onPinjamBarang: (Int, String, String, String, String, String) -> Unit
) {
    var namaBarangBaru by remember { mutableStateOf("") }
    var selectedItemForLoan by remember { mutableStateOf<Item?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Tambah Barang Baru",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = namaBarangBaru,
                        onValueChange = { namaBarangBaru = it },
                        label = { Text("Nama Barang") },
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (namaBarangBaru.isNotBlank()) {
                                onTambahBarang(namaBarangBaru)
                                namaBarangBaru = ""
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(56.dp)
                    ) {
                        Text("Simpan")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            "Daftar Inventaris",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(items) { item ->
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                item.nama_barang,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            SuggestionChip(
                                onClick = {},
                                label = {
                                    Text(
                                        if (item.status_tersedia) "Tersedia" else "Sedang Dipinjam",
                                        fontWeight = FontWeight.Medium
                                    )
                                },
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = if (item.status_tersedia)
                                        Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                                    labelColor = if (item.status_tersedia)
                                        Color(0xFF2E7D32) else Color(0xFFC62828)
                                ),
                                border = null
                            )
                        }
                        if (item.status_tersedia) {
                            FilledTonalButton(
                                onClick = { selectedItemForLoan = item },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Pinjam")
                            }
                        }
                    }
                }
            }
        }
    }

    selectedItemForLoan?.let { item ->
        DialogPinjamBarang(
            item = item,
            onDismiss = { selectedItemForLoan = null },
            onConfirm = { nama, kontak, tglP, tglK ->
                onPinjamBarang(item.id, item.nama_barang, nama, kontak, tglP, tglK)
                selectedItemForLoan = null
            }
        )
    }
}

@Composable
fun DialogPinjamBarang(
    item: Item,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String) -> Unit
) {
    var namaPenyewa by remember { mutableStateOf("") }
    var kontak by remember { mutableStateOf("") }
    var tglPinjam by remember { mutableStateOf("") }
    var tglKembali by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Form Pinjam: ${item.nama_barang}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = namaPenyewa,
                    onValueChange = { namaPenyewa = it },
                    label = { Text("Nama Peminjam / Penyewa") },
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = kontak,
                    onValueChange = { kontak = it },
                    label = { Text("Kontak / No HP") },
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = tglPinjam,
                    onValueChange = { tglPinjam = it },
                    label = { Text("Tanggal Pinjam") },
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = tglKembali,
                    onValueChange = { tglKembali = it },
                    label = { Text("Tgl Rencana Kembali") },
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (namaPenyewa.isNotBlank() && kontak.isNotBlank()) {
                        onConfirm(namaPenyewa, kontak, tglPinjam, tglKembali)
                    }
                },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Proses Pinjam")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

@Composable
fun TransaksiTabContent(
    transactions: List<TransactionDetail>,
    onKembalikan: (Int, Int, String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(transactions) { tx ->
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            tx.namaBarang,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        AssistChip(
                            onClick = {},
                            label = { Text(tx.statusPinjam) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = if (tx.statusPinjam == "Dipinjam")
                                    Color(0xFFFFEBEE) else Color(0xFFE8F5E9),
                                labelColor = if (tx.statusPinjam == "Dipinjam")
                                    Color(0xFFC62828) else Color(0xFF2E7D32)
                            ),
                            border = null
                        )
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                    Text(
                        "Peminjam: ${tx.namaPenyewa} (${tx.kontak})",
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Pinjam: ${tx.tglPinjam}  •  Kembali: ${tx.tglKembali}",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (tx.statusPinjam == "Dipinjam") {
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { onKembalikan(tx.transactionId, tx.itemId, tx.namaBarang) },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            ),
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Tandai Dikembalikan")
                        }
                    }
                }
            }
        }
    }
}