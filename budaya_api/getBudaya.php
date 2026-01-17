<?php
// API untuk mengambil semua data budaya
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET");

include 'koneksi.php';

// Query untuk mengambil semua budaya (diurutkan berdasarkan daerah A-Z)
$sql = "SELECT id, nama, daerah, deskripsi, gambar FROM budaya ORDER BY daerah ASC";
$result = $conn->query($sql);

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
        "message" => "Data budaya berhasil diambil",
        "data" => $budayaList
    ]);
} else {
    echo json_encode([
        "success" => true,
        "message" => "Tidak ada data budaya",
        "data" => []
    ]);
}

$conn->close();
