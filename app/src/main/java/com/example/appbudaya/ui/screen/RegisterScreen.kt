package com.example.appbudaya.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.appbudaya.viewmodel.BudayaViewModel

@Composable
fun RegisterScreen(
    viewModel: BudayaViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    // State untuk input
    var nama by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Judul
        Text(
            text = "Daftar Akun Baru",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Buat akun untuk mengakses aplikasi",
            style = MaterialTheme.typography.bodyMedium
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Input Nama
        OutlinedTextField(
            value = nama,
            onValueChange = { nama = it },
            label = { Text("Nama Lengkap") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Input Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            placeholder = { Text("contoh@gmail.com") }
        )
        
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Input Password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            supportingText = {
                Text(
                    text = "Minimal 6 karakter",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (password.length >= 6) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            },
            isError = password.isNotEmpty() && password.length < 6
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        
        // Tombol Register
        Button(
            onClick = {
                if (nama.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                    // Validasi email harus Gmail
                    if (!email.endsWith("@gmail.com", ignoreCase = true)) {
                        viewModel.setError("Email harus menggunakan @gmail.com")
                    } 
                    // Validasi password minimal 6 karakter
                    else if (password.length < 6) {
                        viewModel.setError("Password minimal 6 karakter")
                    }
                    else {
                        viewModel.register(nama, email, password) {
                            // Callback sukses - tampilkan dialog
                            showSuccessDialog = true
                        }
                    }
                } else {
                    viewModel.setError("Semua field harus diisi")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = !viewModel.isLoading
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Daftar")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Link ke Login
        TextButton(onClick = onNavigateToLogin) {
            Text("Sudah punya akun? Login di sini")
        }
        
        // Error message
        viewModel.errorMessage?.let { error ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
    
    // Success Dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { 
                Text(
                    "✅ Registrasi Berhasil!",
                    style = MaterialTheme.typography.titleMedium
                ) 
            },
            text = { 
                Text("Akun Anda telah berhasil dibuat. Silakan login untuk melanjutkan.") 
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onRegisterSuccess()
                    }
                ) {
                    Text("OK, Login Sekarang")
                }
            }
        )
    }
}
