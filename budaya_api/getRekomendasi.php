<?php
// API untuk mengambil rekomendasi budaya
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

// Query untuk get rekomendasi dengan JOIN ke tabel budaya
$stmt = $conn->prepare("
    SELECT b.id, b.nama, b.daerah, b.deskripsi, b.gambar 
    FROM rekomendasi r
    INNER JOIN budaya b ON r.budaya_rekomendasi_id = b.id
    WHERE r.budaya_id = ?
    ORDER BY r.id ASC
");
$stmt->bind_param("i", $budaya_id);
$stmt->execute();
$result = $stmt->get_result();

$rekomendasiList = [];
while ($row = $result->fetch_assoc()) {
    $rekomendasiList[] = [
        "id" => (int)$row['id'],
        "nama" => $row['nama'],
        "daerah" => $row['daerah'],
        "deskripsi" => $row['deskripsi'],
        "gambar" => $row['gambar']
    ];
}

echo json_encode([
    "success" => true,
    "message" => count($rekomendasiList) > 0 ? "Rekomendasi berhasil diambil" : "Tidak ada rekomendasi",
    "data" => $rekomendasiList
]);

$stmt->close();
$conn->close();
