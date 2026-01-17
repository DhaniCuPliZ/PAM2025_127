package com.example.appbudaya.network

import com.example.appbudaya.model.ApiResponse
import com.example.appbudaya.model.Budaya
import com.example.appbudaya.model.User
import com.example.appbudaya.model.Stats
import retrofit2.http.*

// Interface untuk semua endpoint API
interface ApiService {
    
    // Login user
    @FormUrlEncoded
    @POST("login.php")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): ApiResponse<User>
    
    // Register user baru
    @FormUrlEncoded
    @POST("register.php")
    suspend fun register(
        @Field("nama") nama: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): ApiResponse<Unit>
    
    // Get semua budaya
    @GET("getBudaya.php")
    suspend fun getBudaya(): ApiResponse<List<Budaya>>
    
    // Search budaya
    @GET("searchBudaya.php")
    suspend fun searchBudaya(
        @Query("query") query: String
    ): ApiResponse<List<Budaya>>
    
    // Get detail budaya
    @GET("getDetailBudaya.php")
    suspend fun getDetailBudaya(
        @Query("id") id: Int
    ): ApiResponse<Budaya>
    
    // Get rekomendasi budaya
    @GET("getRekomendasi.php")
    suspend fun getRekomendasi(
        @Query("budaya_id") budayaId: Int
    ): ApiResponse<List<Budaya>>
    
    // ===== ADMIN ENDPOINTS =====
    
    // Admin: Tambah budaya baru
    @FormUrlEncoded
    @POST("addBudaya.php")
    suspend fun addBudaya(
        @Field("nama") nama: String,
        @Field("daerah") daerah: String,
        @Field("deskripsi") deskripsi: String,
        @Field("gambar") gambar: String
    ): ApiResponse<Budaya>
    
    // Admin: Update budaya
    @FormUrlEncoded
    @POST("updateBudaya.php")
    suspend fun updateBudaya(
        @Field("id") id: Int,
        @Field("nama") nama: String,
        @Field("daerah") daerah: String,
        @Field("deskripsi") deskripsi: String,
        @Field("gambar") gambar: String
    ): ApiResponse<Unit>
    
    // Admin: Delete budaya
    @FormUrlEncoded
    @POST("deleteBudaya.php")
    suspend fun deleteBudaya(
        @Field("id") id: Int
    ): ApiResponse<Unit>
    
    // Admin: Get semua user
    @GET("getUsers.php")
    suspend fun getUsers(): ApiResponse<List<User>>
    
    // Admin: Delete user
    @FormUrlEncoded
    @POST("deleteUser.php")
    suspend fun deleteUser(
        @Field("id") id: Int
    ): ApiResponse<Unit>
    
    // Admin: Get statistik
    @GET("getStats.php")
    suspend fun getStats(): ApiResponse<Stats>
    
    // Admin: Get available budaya untuk rekomendasi
    @GET("getAvailableRekomendasi.php")
    suspend fun getAvailableRekomendasi(
        @Query("budaya_id") budayaId: Int
    ): ApiResponse<List<Budaya>>
    
    // Admin: Tambah rekomendasi
    @FormUrlEncoded
    @POST("addRekomendasi.php")
    suspend fun addRekomendasi(
        @Field("budaya_id") budayaId: Int,
        @Field("budaya_rekomendasi_id") rekomendasiId: Int
    ): ApiResponse<Unit>
    
    // Admin: Delete rekomendasi
    @FormUrlEncoded
    @POST("deleteRekomendasi.php")
    suspend fun deleteRekomendasi(
        @Field("budaya_id") budayaId: Int,
        @Field("budaya_rekomendasi_id") rekomendasiId: Int
    ): ApiResponse<Unit>
}
