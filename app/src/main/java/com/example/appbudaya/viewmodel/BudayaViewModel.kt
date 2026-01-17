package com.example.appbudaya.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appbudaya.model.Budaya
import com.example.appbudaya.model.User
import com.example.appbudaya.model.Stats
import com.example.appbudaya.network.RetrofitClient
import kotlinx.coroutines.launch

// ViewModel untuk manage state aplikasi
class BudayaViewModel : ViewModel() {
    
    // State untuk loading
    var isLoading by mutableStateOf(false)
        private set
    
    // State untuk error message
    var errorMessage by mutableStateOf<String?>(null)
        private set
    
    // State untuk list budaya
    var budayaList by mutableStateOf<List<Budaya>>(emptyList())
        private set
    
    // State untuk budaya yang dipilih
    var selectedBudaya by mutableStateOf<Budaya?>(null)
        private set
    
    // State untuk user yang login
    var currentUser by mutableStateOf<User?>(null)
        private set
    
    // ===== ADMIN STATES =====
    
    // State untuk list user (admin)
    var userList by mutableStateOf<List<User>>(emptyList())
        private set
    
    // State untuk statistik (admin)
    var stats by mutableStateOf<Stats?>(null)
        private set
    
    // State untuk rekomendasi budaya
    var rekomendasiList by mutableStateOf<List<Budaya>>(emptyList())
        private set
    
    // State untuk available rekomendasi (budaya yang belum jadi rekomendasi)
    var availableRekomendasiList by mutableStateOf<List<Budaya>>(emptyList())
        private set
    
    // State untuk available budaya (admin rekomendasi)
    var availableBudayaList by mutableStateOf<List<Budaya>>(emptyList())
        private set
    
    // Fungsi untuk login
    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            // Log untuk debugging
            android.util.Log.d("JWT_TOKEN", "=== LOGIN DIMULAI ===")
            android.util.Log.d("JWT_TOKEN", "Email: $email")
            
            try {
                val response = RetrofitClient.apiService.login(email, password)
                
                android.util.Log.d("JWT_TOKEN", "Response received: ${response.success}")
                
                if (response.success && response.data != null) {
                    currentUser = response.data
                    // Simpan JWT token untuk request selanjutnya
                    RetrofitClient.authToken = response.data.token
                    
                    // Log token untuk debugging (hapus di production)
                    android.util.Log.d("JWT_TOKEN", "✅ LOGIN BERHASIL!")
                    android.util.Log.d("JWT_TOKEN", "Token tersimpan: ${response.data.token}")
                    android.util.Log.d("JWT_TOKEN", "User: ${response.data.nama} (${response.data.role})")
                    
                    onSuccess()
                } else {
                    android.util.Log.e("JWT_TOKEN", "❌ Login gagal: ${response.message}")
                    errorMessage = response.message
                }
            } catch (e: Exception) {
                android.util.Log.e("JWT_TOKEN", "❌ Error: ${e.message}")
                errorMessage = "Terjadi kesalahan: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
    
    // Fungsi untuk register
    fun register(nama: String, email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            try {
                val response = RetrofitClient.apiService.register(nama, email, password)
                
                if (response.success) {
                    onSuccess()
                } else {
                    errorMessage = response.message
                }
            } catch (e: Exception) {
                errorMessage = "Terjadi kesalahan: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
    
    // Fungsi untuk load semua budaya
    fun loadBudaya() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            try {
                val response = RetrofitClient.apiService.getBudaya()
                
                if (response.success && response.data != null) {
                    budayaList = response.data
                } else {
                    errorMessage = response.message
                }
            } catch (e: Exception) {
                errorMessage = "Terjadi kesalahan: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
    
    // Fungsi untuk search budaya
    fun searchBudaya(query: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            try {
                val response = RetrofitClient.apiService.searchBudaya(query)
                
                if (response.success && response.data != null) {
                    budayaList = response.data
                } else {
                    errorMessage = response.message
                    budayaList = emptyList()
                }
            } catch (e: Exception) {
                errorMessage = "Terjadi kesalahan: ${e.message}"
                budayaList = emptyList()
            } finally {
                isLoading = false
            }
        }
    }
    
    // Fungsi untuk load detail budaya
    fun loadDetailBudaya(id: Int) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            try {
                val response = RetrofitClient.apiService.getDetailBudaya(id)
                
                if (response.success && response.data != null) {
                    selectedBudaya = response.data
                } else {
                    errorMessage = response.message
                }
            } catch (e: Exception) {
                errorMessage = "Terjadi kesalahan: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
    
    // Fungsi untuk clear error message
    fun clearError() {
        errorMessage = null
    }
    
    // Fungsi untuk clear selected budaya
    fun clearSelectedBudaya() {
        selectedBudaya = null
    }
    
    // Fungsi untuk set error message
    fun setError(message: String) {
        errorMessage = message
    }
    
    // Fungsi untuk logout
    fun logout() {
        currentUser = null
        budayaList = emptyList()
        selectedBudaya = null
        errorMessage = null
        userList = emptyList()
        stats = null
        // Hapus JWT token
        RetrofitClient.authToken = null
    }
    
    // ===== ADMIN FUNCTIONS =====
    
    // Admin: Tambah budaya baru
    fun addBudaya(nama: String, daerah: String, deskripsi: String, gambar: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            try {
                val response = RetrofitClient.apiService.addBudaya(nama, daerah, deskripsi, gambar)
                
                if (response.success) {
                    loadBudaya() // Reload list
                    onSuccess()
                } else {
                    errorMessage = response.message
                }
            } catch (e: Exception) {
                errorMessage = "Terjadi kesalahan: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
    
    // Admin: Update budaya
    fun updateBudaya(id: Int, nama: String, daerah: String, deskripsi: String, gambar: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            try {
                val response = RetrofitClient.apiService.updateBudaya(id, nama, daerah, deskripsi, gambar)
                
                if (response.success) {
                    loadBudaya() // Reload list
                    onSuccess()
                } else {
                    errorMessage = response.message
                }
            } catch (e: Exception) {
                errorMessage = "Terjadi kesalahan: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
    
    // Admin: Delete budaya
    fun deleteBudaya(id: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            try {
                val response = RetrofitClient.apiService.deleteBudaya(id)
                
                if (response.success) {
                    loadBudaya() // Reload list
                    onSuccess()
                } else {
                    errorMessage = response.message
                }
            } catch (e: Exception) {
                errorMessage = "Terjadi kesalahan: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
    
    // Admin: Load semua user
    fun loadUsers() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            try {
                val response = RetrofitClient.apiService.getUsers()
                
                if (response.success && response.data != null) {
                    userList = response.data
                } else {
                    errorMessage = response.message
                }
            } catch (e: Exception) {
                errorMessage = "Terjadi kesalahan: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
    
    // Admin: Delete user
    fun deleteUser(id: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            try {
                val response = RetrofitClient.apiService.deleteUser(id)
                
                if (response.success) {
                    loadUsers() // Reload list
                    onSuccess()
                } else {
                    errorMessage = response.message
                }
            } catch (e: Exception) {
                errorMessage = "Terjadi kesalahan: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
    
    // Admin: Load statistik
    fun loadStats() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            try {
                val response = RetrofitClient.apiService.getStats()
                
                if (response.success && response.data != null) {
                    stats = response.data
                } else {
                    errorMessage = response.message
                }
            } catch (e: Exception) {
                errorMessage = "Terjadi kesalahan: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
    
    // ===== REKOMENDASI FUNCTIONS =====
    
    // Fungsi untuk load rekomendasi budaya
    fun loadRekomendasi(budayaId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getRekomendasi(budayaId)
                
                if (response.success && response.data != null) {
                    rekomendasiList = response.data
                } else {
                    rekomendasiList = emptyList()
                }
            } catch (e: Exception) {
                rekomendasiList = emptyList()
            }
        }
    }
    
    // Fungsi untuk load available rekomendasi (budaya yang belum jadi rekomendasi)
    fun loadAvailableRekomendasi(budayaId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getAvailableRekomendasi(budayaId)
                
                if (response.success && response.data != null) {
                    availableRekomendasiList = response.data
                } else {
                    errorMessage = response.message
                    availableRekomendasiList = emptyList()
                }
            } catch (e: Exception) {
                errorMessage = "Terjadi kesalahan: ${e.message}"
                availableRekomendasiList = emptyList()
            }
        }
    }
    
    // Admin: Tambah rekomendasi
    fun addRekomendasi(budayaId: Int, rekomendasiId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            try {
                val response = RetrofitClient.apiService.addRekomendasi(budayaId, rekomendasiId)
                
                if (response.success) {
                    loadRekomendasi(budayaId) // Reload rekomendasi
                    onSuccess()
                } else {
                    errorMessage = response.message
                }
            } catch (e: Exception) {
                errorMessage = "Terjadi kesalahan: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
    
    // Admin: Delete rekomendasi
    fun deleteRekomendasi(budayaId: Int, rekomendasiId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            try {
                val response = RetrofitClient.apiService.deleteRekomendasi(budayaId, rekomendasiId)
                
                if (response.success) {
                    onSuccess()
                } else {
                    errorMessage = response.message
                }
            } catch (e: Exception) {
                errorMessage = "Terjadi kesalahan: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}
