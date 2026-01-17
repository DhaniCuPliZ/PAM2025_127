<?php
// File koneksi database
// Konfigurasi database
$host = "127.0.0.1";
$username = "root";
$password = "";
$database = "budaya_nusantara";
$port = 3307; // Port MySQL dari XAMPP Control Panel

// Buat koneksi dengan port custom
$conn = new mysqli($host, $username, $password, $database, $port);

// Cek koneksi
if ($conn->connect_error) {
    header("Content-Type: application/json");
    die(json_encode([
        "success" => false,
        "message" => "Koneksi database gagal: " . $conn->connect_error
    ]));
}

// Set charset UTF-8
$conn->set_charset("utf8");
