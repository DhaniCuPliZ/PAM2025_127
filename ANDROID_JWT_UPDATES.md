# Manual Updates Required for Android App

## Files to Update Manually

### 1. BudayaViewModel.kt

#### Update Login Function (Line 55-76)
Replace the login function with:

```kotlin
// Fungsi untuk login
fun login(email: String, password: String, onSuccess: () -> Unit) {
    viewModelScope.launch {
        isLoading = true
        errorMessage = null
        
        try {
            val response = RetrofitClient.apiService.login(email, password)
            
            if (response.success && response.data != null) {
                currentUser = response.data
                // Simpan JWT token untuk request selanjutnya
                RetrofitClient.authToken = response.data.token
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
```

#### Update Logout Function (Line 183-191)
Replace the logout function with:

```kotlin
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
```

## Summary of Changes

1. **User.kt** - Added `token: String = ""` field ✅ (Already updated)
2. **RetrofitClient.kt** - Added JWT interceptor ✅ (Already updated)
3. **BudayaViewModel.kt** - Need to update login() and logout() functions

## After Making Changes

1. Sync Gradle
2. Clean and Rebuild project
3. Test login functionality
4. Verify JWT token is sent with admin requests
