<?php
// API untuk mencari budaya berdasarkan nama atau daerah
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET");

include 'koneksi.php';

// Ambil query dari parameter
$query = isset($_GET['query']) ? $_GET['query'] : '';

if (empty($query)) {
    echo json_encode([
        "success" => false,
        "message" => "Query pencarian tidak boleh kosong"
    ]);
    exit;
}

// Search di nama atau daerah (diurutkan berdasarkan daerah A-Z)
$searchTerm = "%$query%";
$stmt = $conn->prepare("SELECT id, nama, daerah, deskripsi, gambar FROM budaya WHERE nama LIKE ? OR daerah LIKE ? ORDER BY daerah ASC");
$stmt->bind_param("ss", $searchTerm, $searchTerm);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    $budayaList = [];
    while ($row = $result->fetch_assoc()) {
        $budayaList[] = [
            "id" => (int)$row['id'],
            "nama" => $row['nama'],
            "daerah" => $row['daerah'],
            "deskripsi" => $row['deskripsi'],
            "gambar" => $row['gambar']
        ];
    }
    
    echo json_encode([
        "success" => true,
        "message" => "Ditemukan " . count($budayaList) . " hasil",
        "data" => $budayaList
    ]);
} else {
    echo json_encode([
        "success" => true,
        "message" => "Tidak ada hasil ditemukan",
        "data" => []
    ]);
}

$stmt->close();
$conn->close();
