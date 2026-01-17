<?php
// API untuk menambah rekomendasi budaya (Admin only)
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Allow-Headers: Content-Type, Authorization");

include 'koneksi.php';
include 'jwt_helper.php';

// Verify JWT dan pastikan user adalah admin
$user = requireAdmin();

// Ambil data dari request
$budaya_id = isset($_POST['budaya_id']) ? intval($_POST['budaya_id']) : 0;
$budaya_rekomendasi_id = isset($_POST['budaya_rekomendasi_id']) ? intval($_POST['budaya_rekomendasi_id']) : 0;

// Validasi input
if ($budaya_id <= 0 || $budaya_rekomendasi_id <= 0) {
    echo json_encode([
        "success" => false,
        "message" => "ID budaya tidak valid"
    ]);
    exit;
}

// Validasi: budaya tidak boleh merekomendasikan dirinya sendiri
if ($budaya_id === $budaya_rekomendasi_id) {
    echo json_encode([
        "success" => false,
        "message" => "Budaya tidak boleh merekomendasikan dirinya sendiri"
    ]);
    exit;
}

// Cek apakah sudah ada 3 rekomendasi
$checkCount = $conn->prepare("SELECT COUNT(*) as total FROM rekomendasi WHERE budaya_id = ?");
$checkCount->bind_param("i", $budaya_id);
$checkCount->execute();
$countResult = $checkCount->get_result();
$count = $countResult->fetch_assoc()['total'];
$checkCount->close();

if ($count >= 3) {
    echo json_encode([
        "success" => false,
        "message" => "Maksimal 3 rekomendasi per budaya"
    ]);
    exit;
}

// Insert rekomendasi baru
$stmt = $conn->prepare("INSERT INTO rekomendasi (budaya_id, budaya_rekomendasi_id) VALUES (?, ?)");
$stmt->bind_param("ii", $budaya_id, $budaya_rekomendasi_id);

if ($stmt->execute()) {
    echo json_encode([
        "success" => true,
        "message" => "Rekomendasi berhasil ditambahkan"
    ]);
} else {
    // Check if error is duplicate entry
    if ($conn->errno === 1062) {
        echo json_encode([
            "success" => false,
            "message" => "Rekomendasi sudah ada"
        ]);
    } else {
        echo json_encode([
            "success" => false,
            "message" => "Gagal menambahkan rekomendasi: " . $conn->error
        ]);
    }
}

$stmt->close();
$conn->close();
