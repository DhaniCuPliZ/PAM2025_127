<?php
// API untuk menghapus rekomendasi budaya (Admin only)
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
        "message" => "ID rekomendasi tidak valid"
    ]);
    exit;
}

// Delete rekomendasi
$stmt = $conn->prepare("DELETE FROM rekomendasi WHERE id = ?");
$stmt->bind_param("i", $id);

if ($stmt->execute()) {
    if ($stmt->affected_rows > 0) {
        echo json_encode([
            "success" => true,
            "message" => "Rekomendasi berhasil dihapus"
        ]);
    } else {
        echo json_encode([
            "success" => false,
            "message" => "Rekomendasi tidak ditemukan"
        ]);
    }
} else {
    echo json_encode([
        "success" => false,
        "message" => "Gagal menghapus rekomendasi: " . $conn->error
    ]);
}

$stmt->close();
$conn->close();
