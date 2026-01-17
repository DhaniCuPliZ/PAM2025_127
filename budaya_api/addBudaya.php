<?php
// API untuk menambah budaya baru (Admin only)
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Allow-Headers: Content-Type, Authorization");

include 'koneksi.php';
include 'jwt_helper.php';

// Verify JWT dan pastikan user adalah admin
$user = requireAdmin();

// Ambil data dari request
$nama = isset($_POST['nama']) ? $_POST['nama'] : '';
$daerah = isset($_POST['daerah']) ? $_POST['daerah'] : '';
$deskripsi = isset($_POST['deskripsi']) ? $_POST['deskripsi'] : '';
$gambar = isset($_POST['gambar']) ? $_POST['gambar'] : '';

// Validasi input
if (empty($nama) || empty($daerah) || empty($deskripsi) || empty($gambar)) {
    echo json_encode([
        "success" => false,
        "message" => "Semua field harus diisi"
    ]);
    exit;
}

// Insert budaya baru
$stmt = $conn->prepare("INSERT INTO budaya (nama, daerah, deskripsi, gambar) VALUES (?, ?, ?, ?)");
$stmt->bind_param("ssss", $nama, $daerah, $deskripsi, $gambar);

if ($stmt->execute()) {
    $newId = $conn->insert_id;
    
    echo json_encode([
        "success" => true,
        "message" => "Budaya berhasil ditambahkan",
        "data" => [
            "id" => $newId,
            "nama" => $nama,
            "daerah" => $daerah,
            "deskripsi" => $deskripsi,
            "gambar" => $gambar
        ]
    ]);
} else {
    echo json_encode([
        "success" => false,
        "message" => "Gagal menambahkan budaya: " . $conn->error
    ]);
}

$stmt->close();
$conn->close();
