<?php
// API untuk update budaya (Admin only)
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
$nama = isset($_POST['nama']) ? $_POST['nama'] : '';
$daerah = isset($_POST['daerah']) ? $_POST['daerah'] : '';
$deskripsi = isset($_POST['deskripsi']) ? $_POST['deskripsi'] : '';
$gambar = isset($_POST['gambar']) ? $_POST['gambar'] : '';

// Validasi input
if ($id <= 0 || empty($nama) || empty($daerah) || empty($deskripsi) || empty($gambar)) {
    echo json_encode([
        "success" => false,
        "message" => "Semua field harus diisi"
    ]);
    exit;
}

// Update budaya
$stmt = $conn->prepare("UPDATE budaya SET nama = ?, daerah = ?, deskripsi = ?, gambar = ? WHERE id = ?");
$stmt->bind_param("ssssi", $nama, $daerah, $deskripsi, $gambar, $id);

if ($stmt->execute()) {
    if ($stmt->affected_rows > 0) {
        echo json_encode([
            "success" => true,
            "message" => "Budaya berhasil diupdate"
        ]);
    } else {
        echo json_encode([
            "success" => false,
            "message" => "Budaya tidak ditemukan atau tidak ada perubahan"
        ]);
    }
} else {
    echo json_encode([
        "success" => false,
        "message" => "Gagal mengupdate budaya: " . $conn->error
    ]);
}

$stmt->close();
$conn->close();
