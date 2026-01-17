
package com.example.appbudaya.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.appbudaya.model.Budaya
import com.example.appbudaya.viewmodel.BudayaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    viewModel: BudayaViewModel,
    budayaId: Int,
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Int) -> Unit // Navigasi ke detail budaya lain
) {
    // Load detail budaya dan rekomendasi saat screen dibuka
    LaunchedEffect(budayaId) {
        viewModel.loadDetailBudaya(budayaId)
        viewModel.loadRekomendasi(budayaId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Budaya") },
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
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                viewModel.errorMessage != null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = viewModel.errorMessage ?: "",
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadDetailBudaya(budayaId) }) {
                            Text("Coba Lagi")
                        }
                    }
                }
                viewModel.selectedBudaya != null -> {
                    val budaya = viewModel.selectedBudaya!!
                    
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Gambar budaya
                        AsyncImage(
                            model = budaya.gambar,
                            contentDescription = budaya.nama,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            contentScale = ContentScale.Crop
                        )
                        
                        // Konten detail
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            // Nama budaya
                            Text(
                                text = budaya.nama,
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // Daerah
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Daerah: ",
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Text(
                                    text = budaya.daerah,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Divider()
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Deskripsi
                            Text(
                                text = "Deskripsi",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = budaya.deskripsi,
                                style = MaterialTheme.typography.bodyLarge,
                                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
                            )
                            
                            // Section Rekomendasi
                            if (viewModel.rekomendasiList.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(24.dp))
                                
                                Divider()
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                Text(
                                    text = "📌 Rekomendasi Budaya Lainnya",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                // Horizontal scroll rekomendasi cards
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    contentPadding = PaddingValues(end = 16.dp)
                                ) {
                                    items(viewModel.rekomendasiList) { rekomendasi ->
                                        RekomendasiCard(
                                            budaya = rekomendasi,
                                            onClick = { 
                                                // Navigasi ke detail budaya yang diklik
                                                onNavigateToDetail(rekomendasi.id)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Component untuk card rekomendasi
@Composable
fun RekomendasiCard(
    budaya: Budaya,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Gambar budaya
            AsyncImage(
                model = budaya.gambar,
                contentDescription = budaya.nama,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentScale = ContentScale.Crop
            )
            
            // Info budaya
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = budaya.nama,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = budaya.daerah,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
