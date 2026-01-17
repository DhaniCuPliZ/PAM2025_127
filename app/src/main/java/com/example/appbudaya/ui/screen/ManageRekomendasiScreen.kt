package com.example.appbudaya.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appbudaya.model.Budaya
import com.example.appbudaya.viewmodel.BudayaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageRekomendasiScreen(
    viewModel: BudayaViewModel,
    onNavigateBack: () -> Unit
) {
    var selectedBudaya by remember { mutableStateOf<Budaya?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }
    
    // Load budaya list
    LaunchedEffect(Unit) {
        viewModel.loadBudaya()
    }
    
    // Load rekomendasi ketika budaya dipilih
    LaunchedEffect(selectedBudaya) {
        selectedBudaya?.let {
            viewModel.loadRekomendasi(it.id)
            viewModel.loadAvailableRekomendasi(it.id)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kelola Rekomendasi") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            if (selectedBudaya != null) {
                FloatingActionButton(
                    onClick = { showAddDialog = true }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Tambah Rekomendasi")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Section: Pilih Budaya
            Text(
                text = "1. Pilih Budaya",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Dropdown untuk pilih budaya
            var expanded by remember { mutableStateOf(false) }
            
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedBudaya?.nama ?: "Pilih budaya...",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    viewModel.budayaList.forEach { budaya ->
                        DropdownMenuItem(
                            text = { Text(budaya.nama) },
                            onClick = {
                                selectedBudaya = budaya
                                expanded = false
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Section: Daftar Rekomendasi
            if (selectedBudaya != null) {
                Text(
                    text = "2. Rekomendasi untuk \"${selectedBudaya?.nama}\"",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                if (viewModel.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else if (viewModel.rekomendasiList.isEmpty()) {
                    Text(
                        text = "Belum ada rekomendasi. Klik tombol + untuk menambah.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(viewModel.rekomendasiList) { rekomendasi ->
                            RekomendasiItem(
                                budaya = rekomendasi,
                                onDelete = {
                                    viewModel.deleteRekomendasi(
                                        budayaId = selectedBudaya!!.id,
                                        rekomendasiId = rekomendasi.id
                                    ) {
                                        // Reload setelah delete
                                        viewModel.loadRekomendasi(selectedBudaya!!.id)
                                        viewModel.loadAvailableRekomendasi(selectedBudaya!!.id)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
        
        // Dialog untuk tambah rekomendasi
        if (showAddDialog && selectedBudaya != null) {
            AddRekomendasiDialog(
                viewModel = viewModel,
                budayaId = selectedBudaya!!.id,
                onDismiss = { showAddDialog = false },
                onSuccess = {
                    showAddDialog = false
                    // Reload setelah tambah
                    viewModel.loadRekomendasi(selectedBudaya!!.id)
                    viewModel.loadAvailableRekomendasi(selectedBudaya!!.id)
                }
            )
        }
    }
}

@Composable
fun RekomendasiItem(
    budaya: Budaya,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier.fillMaxWidth()
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
                    text = budaya.nama,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = budaya.daerah,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Hapus",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
    
    // Delete confirmation dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Hapus Rekomendasi") },
            text = { Text("Apakah Anda yakin ingin menghapus \"${budaya.nama}\" dari rekomendasi?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    }
                ) {
                    Text("Hapus", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}

@Composable
fun AddRekomendasiDialog(
    viewModel: BudayaViewModel,
    budayaId: Int,
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    var selectedRekomendasi by remember { mutableStateOf<Budaya?>(null) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tambah Rekomendasi") },
        text = {
            Column {
                Text("Pilih budaya yang akan direkomendasikan:")
                
                Spacer(modifier = Modifier.height(8.dp))
                
                if (viewModel.availableRekomendasiList.isEmpty()) {
                    Text(
                        text = "Tidak ada budaya yang tersedia untuk rekomendasi.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.height(200.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(viewModel.availableRekomendasiList) { budaya ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = if (selectedRekomendasi?.id == budaya.id) {
                                    CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer
                                    )
                                } else {
                                    CardDefaults.cardColors()
                                },
                                onClick = { selectedRekomendasi = budaya }
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = budaya.nama,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = budaya.daerah,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    selectedRekomendasi?.let { rekomendasi ->
                        viewModel.addRekomendasi(budayaId, rekomendasi.id, onSuccess)
                    }
                },
                enabled = selectedRekomendasi != null
            ) {
                Text("Tambah")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
