<?php
// API untuk delete user (Admin only)
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Allow-Headers: Content-Type, Authorization");

include 'koneksi.php';
include 'jwt_helper.php';

// Verify JWT dan pastikan user adalah admin
$user = requireAdmin();

// Ambil data dari request
$id = isset($_POST['id']) ? intval($_POST['id']) : 0;

// Validasi input
if ($id <= 0) {
    echo json_encode([
        "success" => false,
        "message" => "ID user tidak valid"
    ]);
    exit;
}

// Cek apakah user yang akan dihapus adalah admin
$checkStmt = $conn->prepare("SELECT role FROM user WHERE id = ?");
$checkStmt->bind_param("i", $id);
$checkStmt->execute();
$result = $checkStmt->get_result();

if ($result->num_rows > 0) {
    $user = $result->fetch_assoc();
    
    // Tidak boleh menghapus admin
    if ($user['role'] === 'admin') {
        echo json_encode([
            "success" => false,
            "message" => "Tidak dapat menghapus user dengan role admin"
        ]);
        exit;
    }
}

$checkStmt->close();

// Delete user
$stmt = $conn->prepare("DELETE FROM user WHERE id = ?");
$stmt->bind_param("i", $id);

if ($stmt->execute()) {
    if ($stmt->affected_rows > 0) {
        echo json_encode([
            "success" => true,
            "message" => "User berhasil dihapus"
        ]);
    } else {
        echo json_encode([
            "success" => false,
            "message" => "User tidak ditemukan"
        ]);
    }
} else {
    echo json_encode([
        "success" => false,
        "message" => "Gagal menghapus user: " . $conn->error
    ]);
}

$stmt->close();
$conn->close();
