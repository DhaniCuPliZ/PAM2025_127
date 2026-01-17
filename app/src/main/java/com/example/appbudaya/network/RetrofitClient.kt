package com.example.appbudaya.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Singleton object untuk Retrofit client
object RetrofitClient {
    
    // Base URL API
    // Gunakan 10.0.2.2 untuk emulator Android (localhost di komputer host)
    // Port 8080 sesuai dengan XAMPP Apache yang running
    private const val BASE_URL = "http://10.0.2.2:8080/budaya_nusantara/"

    // Variable untuk menyimpan JWT token
    var authToken: String? = null
    
    // Logging interceptor untuk debugging
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    // OkHttp client dengan logging dan JWT interceptor
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val request = chain.request()
            // Tambahkan Authorization header jika ada token
            val newRequest = if (authToken != null) {
                request.newBuilder()
                    .addHeader("Authorization", "Bearer $authToken")
                    .build()
            } else {
                request
            }
            chain.proceed(newRequest)
        }
        .build()
    
    // Gson dengan lenient mode untuk handle JSON yang tidak sempurna
    private val gson = com.google.gson.GsonBuilder()
        .setLenient()
        .create()
    
    // Retrofit instance
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    
    // API service instance
    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
