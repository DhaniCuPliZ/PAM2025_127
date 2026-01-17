package com.example.appbudaya.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appbudaya.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit
) {
    // Auto navigate setelah 2 detik
    LaunchedEffect(Unit) {
        delay(2000)
        onNavigateToLogin()
    }
    
    // Background dengan gradient krem/putih
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF8E1), // Krem terang
                        Color(0xFFFFECB3), // Krem
                        Color(0xFFFFE082)  // Krem keemasan
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icon/Logo Aplikasi
            // Menggunakan icon default dulu, bisa diganti dengan logo custom
            Text(
                text = "🏛️",
                fontSize = 80.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            // Nama Aplikasi
            Text(
                text = "Edukasi Budaya\nNusantara",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6D4C41), // Coklat tua
                textAlign = TextAlign.Center,
                lineHeight = 36.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Tagline
            Text(
                text = "Mengenal Kekayaan Budaya Indonesia",
                fontSize = 14.sp,
                color = Color(0xFF8D6E63), // Coklat medium
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Motif budaya (dekoratif)
            Text(
                text = "◈ ❖ ◈",
                fontSize = 20.sp,
                color = Color(0xFFD7CCC8), // Coklat muda
                letterSpacing = 8.sp
            )
        }
        
        // Footer
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Versi 1.0",
                fontSize = 12.sp,
                color = Color(0xFF8D6E63)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "© 2026 Edukasi Budaya Nusantara",
                fontSize = 10.sp,
                color = Color(0xFFA1887F)
            )
        }
    }
}
