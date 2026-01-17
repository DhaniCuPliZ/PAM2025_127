package com.example.appbudaya.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appbudaya.viewmodel.BudayaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    viewModel: BudayaViewModel,
    onNavigateToBudaya: () -> Unit,
    onNavigateToViewBudaya: () -> Unit, // Navigasi ke view-only list
    onNavigateToUsers: () -> Unit,
    onNavigateToRekomendasi: () -> Unit, // Navigasi ke manage rekomendasi
    onLogout: () -> Unit
) {
    // State untuk dialog logout
    var showLogoutDialog by remember { mutableStateOf(false) }
    
    // Load stats saat pertama kali
    LaunchedEffect(Unit) {
        viewModel.loadStats()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Dashboard") },
                actions = {
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Statistik
            Text(
                text = "Statistik",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            
            if (viewModel.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Card Total Budaya - Klik untuk lihat daftar budaya (read-only)
                    StatCard(
                        title = "Total Budaya",
                        value = viewModel.stats?.total_budaya?.toString() ?: "0",
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToViewBudaya // Navigasi ke View Budaya (tanpa CRUD)
                    )
                    
                    // Card Total User - Klik untuk lihat daftar user
                    StatCard(
                        title = "Total User",
                        value = viewModel.stats?.total_user?.toString() ?: "0",
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToUsers // Navigasi ke Kelola User
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Menu Admin
            Text(
                text = "Menu Admin",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            
            // Button Kelola Budaya
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = onNavigateToBudaya
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.List,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Kelola Budaya",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            
            // Button Kelola User
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = onNavigateToUsers
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Kelola User",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            
            // Button Kelola Rekomendasi
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = onNavigateToRekomendasi
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.List,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Kelola Rekomendasi",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
        
        // Dialog konfirmasi logout
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("Logout") },
                text = { Text("Apakah Anda yakin ingin keluar?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showLogoutDialog = false
                            viewModel.logout()
                            onLogout()
                        }
                    ) {
                        Text("Ya")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text("Batal")
                    }
                }
            )
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        onClick = onClick ?: {} // Jika onClick null, gunakan empty lambda
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}
