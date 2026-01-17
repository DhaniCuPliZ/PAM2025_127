<?php
// API untuk mengambil detail budaya berdasarkan ID
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET");

include 'koneksi.php';

// Ambil ID dari parameter
$id = isset($_GET['id']) ? $_GET['id'] : '';

if (empty($id)) {
    echo json_encode([
        "success" => false,
        "message" => "ID budaya tidak boleh kosong"
    ]);
    exit;
}

// Query untuk mengambil detail budaya
$stmt = $conn->prepare("SELECT id, nama, daerah, deskripsi, gambar FROM budaya WHERE id = ?");
$stmt->bind_param("i", $id);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    $budaya = $result->fetch_assoc();
    echo json_encode([
        "success" => true,
        "message" => "Detail budaya berhasil diambil",
        "data" => [
            "id" => (int)$budaya['id'],
            "nama" => $budaya['nama'],
            "daerah" => $budaya['daerah'],
            "deskripsi" => $budaya['deskripsi'],
            "gambar" => $budaya['gambar']
        ]
    ]);
} else {
    echo json_encode([
        "success" => false,
        "message" => "Budaya tidak ditemukan"
    ]);
}

$stmt->close();
$conn->close();
