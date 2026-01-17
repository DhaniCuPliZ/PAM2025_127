<?php
// API untuk mendapatkan list budaya yang bisa direkomendasikan
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET");

include 'koneksi.php';

// Ambil budaya_id dari parameter
$budaya_id = isset($_GET['budaya_id']) ? intval($_GET['budaya_id']) : 0;

if ($budaya_id <= 0) {
    echo json_encode([
        "success" => false,
        "message" => "ID budaya tidak valid"
    ]);
    exit;
}

// Query untuk get budaya yang belum direkomendasikan
// Exclude: budaya itu sendiri dan budaya yang sudah direkomendasikan
$stmt = $conn->prepare("
    SELECT b.id, b.nama, b.daerah, b.deskripsi, b.gambar
    FROM budaya b
    WHERE b.id != ?
    AND b.id NOT IN (
        SELECT budaya_rekomendasi_id 
        FROM rekomendasi 
        WHERE budaya_id = ?
    )
    ORDER BY b.daerah ASC, b.nama ASC
");
$stmt->bind_param("ii", $budaya_id, $budaya_id);
$stmt->execute();
$result = $stmt->get_result();

$availableList = [];
while ($row = $result->fetch_assoc()) {
    $availableList[] = [
        "id" => (int)$row['id'],
        "nama" => $row['nama'],
        "daerah" => $row['daerah'],
        "deskripsi" => $row['deskripsi'],
        "gambar" => $row['gambar']
    ];
}

echo json_encode([
    "success" => true,
    "message" => "Data budaya tersedia berhasil diambil",
    "data" => $availableList
]);

$stmt->close();
$conn->close();
