<?php
// Test script untuk cek password di database
header("Content-Type: application/json");

include 'koneksi.php';

// Ambil semua user untuk cek password format
$query = "SELECT id, nama, email, password, role FROM user";
$result = $conn->query($query);

$users = [];
while ($row = $result->fetch_assoc()) {
    $passwordFormat = substr($row['password'], 0, 4);
    $isHashed = ($passwordFormat === '$2y$');
    
    $users[] = [
        'id' => $row['id'],
        'nama' => $row['nama'],
        'email' => $row['email'],
        'password_preview' => substr($row['password'], 0, 20) . '...',
        'password_length' => strlen($row['password']),
        'is_hashed' => $isHashed,
        'role' => $row['role']
    ];
}

echo json_encode([
    'success' => true,
    'message' => 'Data user berhasil diambil',
    'data' => $users
], JSON_PRETTY_PRINT);

$conn->close();
?>
