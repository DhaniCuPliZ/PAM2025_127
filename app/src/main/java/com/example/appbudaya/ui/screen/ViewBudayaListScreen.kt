package com.example.appbudaya.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appbudaya.model.Budaya
import com.example.appbudaya.viewmodel.BudayaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewBudayaListScreen(
    viewModel: BudayaViewModel,
    onNavigateBack: () -> Unit
) {
    // Load budaya
    LaunchedEffect(Unit) {
        viewModel.loadBudaya()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daftar Budaya") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                viewModel.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                viewModel.budayaList.isEmpty() -> {
                    Text(
                        text = "Belum ada data budaya",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 16.dp,
                            bottom = 16.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(viewModel.budayaList) { budaya ->
                            ViewBudayaCard(budaya = budaya)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ViewBudayaCard(budaya: Budaya) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = budaya.nama,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = budaya.daerah,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
