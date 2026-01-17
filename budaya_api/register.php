<?php
// API untuk registrasi user baru
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");

include 'koneksi.php';

// Ambil data dari request
$nama = isset($_POST['nama']) ? $_POST['nama'] : '';
$email = isset($_POST['email']) ? $_POST['email'] : '';
$password = isset($_POST['password']) ? $_POST['password'] : '';

// Validasi input
if (empty($nama) || empty($email) || empty($password)) {
    echo json_encode([
        "success" => false,
        "message" => "Semua field harus diisi"
    ]);
    exit;
}

// Validasi email harus Gmail
if (!preg_match('/@gmail\.com$/i', $email)) {
    echo json_encode([
        "success" => false,
        "message" => "Email harus menggunakan @gmail.com"
    ]);
    exit;
}

// Cek apakah email sudah terdaftar
$checkEmail = $conn->prepare("SELECT id FROM user WHERE email = ?");
$checkEmail->bind_param("s", $email);
$checkEmail->execute();
$result = $checkEmail->get_result();

if ($result->num_rows > 0) {
    echo json_encode([
        "success" => false,
        "message" => "Email sudah terdaftar"
    ]);
    exit;
}

// Hash password menggunakan bcrypt untuk keamanan
$hashed_password = password_hash($password, PASSWORD_BCRYPT);

// Insert user baru dengan password yang sudah di-hash
$stmt = $conn->prepare("INSERT INTO user (nama, email, password) VALUES (?, ?, ?)");
$stmt->bind_param("sss", $nama, $email, $hashed_password);

if ($stmt->execute()) {
    echo json_encode([
        "success" => true,
        "message" => "Registrasi berhasil"
    ]);
} else {
    echo json_encode([
        "success" => false,
        "message" => "Registrasi gagal: " . $conn->error
    ]);
}

$stmt->close();
$conn->close();
