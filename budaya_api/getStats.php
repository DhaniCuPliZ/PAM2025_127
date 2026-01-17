<?php
// API untuk get statistik dashboard (Admin only)
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET");
header("Access-Control-Allow-Headers: Content-Type, Authorization");

include 'koneksi.php';
include 'jwt_helper.php';

// Verify JWT dan pastikan user adalah admin
$user = requireAdmin();

// Query untuk count total budaya
$budayaQuery = "SELECT COUNT(*) as total FROM budaya";
$budayaResult = $conn->query($budayaQuery);
$totalBudaya = 0;
if ($budayaResult) {
    $row = $budayaResult->fetch_assoc();
    $totalBudaya = (int)$row['total'];
}

// Query untuk count total user (semua pengguna termasuk admin)
$userQuery = "SELECT COUNT(*) as total FROM user";
$userResult = $conn->query($userQuery);
$totalUser = 0;
if ($userResult) {
    $row = $userResult->fetch_assoc();
    $totalUser = (int)$row['total'];
}

// Query untuk count total admin
$adminQuery = "SELECT COUNT(*) as total FROM user WHERE role = 'admin'";
$adminResult = $conn->query($adminQuery);
$totalAdmin = 0;
if ($adminResult) {
    $row = $adminResult->fetch_assoc();
    $totalAdmin = (int)$row['total'];
}

echo json_encode([
    "success" => true,
    "message" => "Statistik berhasil diambil",
    "data" => [
        "total_budaya" => $totalBudaya,
        "total_user" => $totalUser,
        "total_admin" => $totalAdmin
    ]
]);

$conn->close();
