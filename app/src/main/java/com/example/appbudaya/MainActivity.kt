package com.example.appbudaya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.appbudaya.ui.screen.*
import com.example.appbudaya.ui.theme.AppBudayaTheme
import com.example.appbudaya.viewmodel.BudayaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppBudayaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BudayaApp()
                }
            }
        }
    }
}

@Composable
fun BudayaApp() {
    val navController = rememberNavController()
    val viewModel: BudayaViewModel = viewModel()
    
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        // Splash Screen
        composable("splash") {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }
        
        // Login Screen
        composable("login") {
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    // Role-based routing
                    val destination = if (viewModel.currentUser?.role == "admin") {
                        "admin_dashboard"
                    } else {
                        "home"
                    }
                    navController.navigate(destination) {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }
        
        // Register Screen
        composable("register") {
            RegisterScreen(
                viewModel = viewModel,
                onRegisterSuccess = {
                    navController.popBackStack()
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
        
        // Home Screen
        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToDetail = { budayaId ->
                    navController.navigate("detail/$budayaId")
                },
                onNavigateToSearch = {
                    navController.navigate("search")
                },
                onNavigateToAbout = {
                    navController.navigate("about")
                },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
        
        // Search Screen
        composable("search") {
            SearchScreen(
                viewModel = viewModel,
                onNavigateToDetail = { budayaId ->
                    navController.navigate("detail/$budayaId")
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        // About Screen
        composable("about") {
            AboutScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        // Detail Screen
        composable(
            route = "detail/{budayaId}",
            arguments = listOf(navArgument("budayaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val budayaId = backStackEntry.arguments?.getInt("budayaId") ?: 0
            DetailScreen(
                viewModel = viewModel,
                budayaId = budayaId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToDetail = { newBudayaId ->
                    // Navigasi ke detail budaya lain dari rekomendasi
                    navController.navigate("detail/$newBudayaId")
                }
            )
        }
        
        // ===== ADMIN ROUTES =====
        
        // Admin Dashboard
        composable("admin_dashboard") {
            AdminDashboardScreen(
                viewModel = viewModel,
                onNavigateToBudaya = {
                    navController.navigate("admin_budaya")
                },
                onNavigateToViewBudaya = {
                    navController.navigate("view_budaya_list")
                },
                onNavigateToUsers = {
                    navController.navigate("admin_users")
                },
                onNavigateToRekomendasi = {
                    navController.navigate("admin_rekomendasi")
                },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("admin_dashboard") { inclusive = true }
                    }
                }
            )
        }
        
        // View Budaya List (Read-Only dari card Total Budaya)
        composable("view_budaya_list") {
            ViewBudayaListScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        // Admin Budaya Management
        composable("admin_budaya") {
            AdminBudayaScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToAdd = {
                    navController.navigate("admin_add_budaya")
                },
                onNavigateToEdit = { budayaId ->
                    navController.navigate("admin_edit_budaya/$budayaId")
                }
            )
        }
        
        // Admin Add Budaya
        composable("admin_add_budaya") {
            AddEditBudayaScreen(
                viewModel = viewModel,
                budayaId = null,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        // Admin Edit Budaya
        composable(
            route = "admin_edit_budaya/{budayaId}",
            arguments = listOf(navArgument("budayaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val budayaId = backStackEntry.arguments?.getInt("budayaId") ?: 0
            AddEditBudayaScreen(
                viewModel = viewModel,
                budayaId = budayaId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        // Admin User Management
        composable("admin_users") {
            AdminUserScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        // Admin Manage Rekomendasi
        composable("admin_rekomendasi") {
            ManageRekomendasiScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}