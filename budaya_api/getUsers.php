<?php
// API untuk get semua user (Admin only)
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET");
header("Access-Control-Allow-Headers: Content-Type, Authorization");

include 'koneksi.php';
include 'jwt_helper.php';

// Verify JWT dan pastikan user adalah admin
$user = requireAdmin();

// Query untuk get semua user
$query = "SELECT id, nama, email, role, created_at FROM user ORDER BY created_at DESC";
$result = $conn->query($query);

if ($result) {
    $users = [];
    
    while ($row = $result->fetch_assoc()) {
        $users[] = [
            "id" => (int)$row['id'],
            "nama" => $row['nama'],
            "email" => $row['email'],
            "role" => $row['role'],
            "created_at" => $row['created_at']
        ];
    }
    
    echo json_encode([
        "success" => true,
        "message" => "Data user berhasil diambil",
        "data" => $users
    ]);
} else {
    echo json_encode([
        "success" => false,
        "message" => "Gagal mengambil data user: " . $conn->error
    ]);
}

$conn->close();
