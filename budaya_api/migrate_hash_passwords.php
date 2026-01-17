<?php
/**
 * Migration Script: Hash Existing Passwords
 * 
 * Script ini digunakan untuk meng-hash password yang sudah ada di database
 * dari plain text menjadi bcrypt hash.
 * 
 * PERINGATAN: Jalankan script ini HANYA SEKALI setelah update login.php dan register.php
 * 
 * Cara menjalankan:
 * 1. Buka browser ke: http://localhost/budaya_api/migrate_hash_passwords.php
 * 2. Script akan otomatis hash semua password yang masih plain text
 * 3. Setelah selesai, HAPUS file ini untuk keamanan
 */

header("Content-Type: application/json");
include 'koneksi.php';

// Ambil semua user dari database
$result = $conn->query("SELECT id, email, password FROM user");

$updated = 0;
$skipped = 0;
$errors = [];

while ($user = $result->fetch_assoc()) {
    $userId = $user['id'];
    $email = $user['email'];
    $currentPassword = $user['password'];
    
    // Cek apakah password sudah di-hash (bcrypt hash dimulai dengan $2y$)
    if (substr($currentPassword, 0, 4) === '$2y$') {
        $skipped++;
        continue; // Skip, sudah di-hash
    }
    
    // Hash password yang masih plain text
    $hashedPassword = password_hash($currentPassword, PASSWORD_BCRYPT);
    
    // Update ke database
    $stmt = $conn->prepare("UPDATE user SET password = ? WHERE id = ?");
    $stmt->bind_param("si", $hashedPassword, $userId);
    
    if ($stmt->execute()) {
        $updated++;
    } else {
        $errors[] = "Failed to update user ID $userId ($email): " . $conn->error;
    }
    
    $stmt->close();
}

// Output hasil migration
echo json_encode([
    "success" => true,
    "message" => "Migration completed",
    "details" => [
        "total_users" => $result->num_rows,
        "updated" => $updated,
        "skipped" => $skipped,
        "errors" => $errors
    ]
], JSON_PRETTY_PRINT);

$conn->close();

?>
