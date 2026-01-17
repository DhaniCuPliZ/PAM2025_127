<?php
// API untuk login user
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");

include 'koneksi.php';
include 'jwt_helper.php';

// Ambil data dari request
$email = isset($_POST['email']) ? $_POST['email'] : '';
$password = isset($_POST['password']) ? $_POST['password'] : '';

// Validasi input
if (empty($email) || empty($password)) {
    echo json_encode([
        "success" => false,
        "message" => "Email dan password harus diisi"
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

// Cek user di database berdasarkan email
$stmt = $conn->prepare("SELECT id, nama, email, password, role FROM user WHERE email = ?");
$stmt->bind_param("s", $email);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    $user = $result->fetch_assoc();
    
    // Verifikasi password menggunakan password_verify untuk keamanan
    if (password_verify($password, $user['password'])) {
        // Generate JWT token untuk authentication
        $tokenPayload = [
            'user_id' => (int)$user['id'],
            'email' => $user['email'],
            'role' => $user['role']
        ];
        $token = generateJWT($tokenPayload, 86400); // Token valid 24 jam
        
        echo json_encode([
            "success" => true,
            "message" => "Login berhasil",
            "data" => [
                "id" => (int)$user['id'],
                "nama" => $user['nama'],
                "email" => $user['email'],
                "role" => $user['role'],
                "token" => $token
            ]
        ]);
    } else {
        echo json_encode([
            "success" => false,
            "message" => "Email atau password salah"
        ]);
    }
} else {
    echo json_encode([
        "success" => false,
        "message" => "Email atau password salah"
    ]);
}

$stmt->close();
$conn->close();
