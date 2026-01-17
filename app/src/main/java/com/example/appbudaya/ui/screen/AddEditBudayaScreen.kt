package com.example.appbudaya.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appbudaya.viewmodel.BudayaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditBudayaScreen(
    viewModel: BudayaViewModel,
    budayaId: Int? = null, // null = mode add, ada value = mode edit
    onNavigateBack: () -> Unit
) {
    // Form states
    var nama by remember { mutableStateOf("") }
    var daerah by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var gambar by remember { mutableStateOf("") }
    
    val isEditMode = budayaId != null
    
    // Clear selected budaya jika mode add (bukan edit)
    LaunchedEffect(budayaId) {
        if (budayaId == null) {
            viewModel.clearSelectedBudaya()
        }
    }
    
    // Load data jika edit mode
    LaunchedEffect(budayaId) {
        if (isEditMode && budayaId != null) {
            viewModel.loadDetailBudaya(budayaId)
        }
    }
    
    // Populate form jika edit mode
    LaunchedEffect(viewModel.selectedBudaya) {
        viewModel.selectedBudaya?.let { budaya ->
            nama = budaya.nama
            daerah = budaya.daerah
            deskripsi = budaya.deskripsi
            gambar = budaya.gambar
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Edit Budaya" else "Tambah Budaya") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Nama
            OutlinedTextField(
                value = nama,
                onValueChange = { nama = it },
                label = { Text("Nama Budaya") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            // Daerah
            OutlinedTextField(
                value = daerah,
                onValueChange = { daerah = it },
                label = { Text("Daerah Asal") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            // Deskripsi
            OutlinedTextField(
                value = deskripsi,
                onValueChange = { deskripsi = it },
                label = { Text("Deskripsi") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                maxLines = 6
            )
            
            // Gambar URL
            OutlinedTextField(
                value = gambar,
                onValueChange = { gambar = it },
                label = { Text("URL Gambar") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("https://example.com/image.jpg") }
            )
            
            // Error message
            if (viewModel.errorMessage != null) {
                Text(
                    text = viewModel.errorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            // Button Simpan
            Button(
                onClick = {
                    if (nama.isNotBlank() && daerah.isNotBlank() && deskripsi.isNotBlank() && gambar.isNotBlank()) {
                        if (isEditMode && budayaId != null) {
                            viewModel.updateBudaya(budayaId, nama, daerah, deskripsi, gambar) {
                                onNavigateBack()
                            }
                        } else {
                            viewModel.addBudaya(nama, daerah, deskripsi, gambar) {
                                onNavigateBack()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !viewModel.isLoading && nama.isNotBlank() && daerah.isNotBlank() && 
                         deskripsi.isNotBlank() && gambar.isNotBlank()
            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(if (isEditMode) "Update" else "Simpan")
                }
            }
        }
    }
}
