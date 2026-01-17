<?php
/**
 * JWT Helper Functions
 * 
 * Simple JWT implementation tanpa library eksternal
 * Untuk production, disarankan menggunakan library seperti firebase/php-jwt
 */

// Secret key untuk signing JWT (GANTI dengan random string yang aman!)
define('JWT_SECRET_KEY', 'budaya_nusantara_secret_key_2024_change_this_in_production');
define('JWT_ALGORITHM', 'HS256');

/**
 * Encode data menjadi JWT token
 * 
 * @param array $payload Data yang akan di-encode (user_id, role, dll)
 * @param int $expiration Waktu expired dalam detik (default 24 jam)
 * @return string JWT token
 */
function generateJWT($payload, $expiration = 86400) {
    // Header
    $header = [
        'typ' => 'JWT',
        'alg' => JWT_ALGORITHM
    ];
    
    // Payload dengan expiration time
    $payload['iat'] = time(); // Issued at
    $payload['exp'] = time() + $expiration; // Expiration time
    
    // Encode header dan payload
    $headerEncoded = base64UrlEncode(json_encode($header));
    $payloadEncoded = base64UrlEncode(json_encode($payload));
    
    // Buat signature
    $signature = hash_hmac('sha256', "$headerEncoded.$payloadEncoded", JWT_SECRET_KEY, true);
    $signatureEncoded = base64UrlEncode($signature);
    
    // Gabungkan menjadi JWT token
    return "$headerEncoded.$payloadEncoded.$signatureEncoded";
}

/**
 * Decode dan verify JWT token
 * 
 * @param string $token JWT token
 * @return array|false Payload jika valid, false jika invalid
 */
function verifyJWT($token) {
    // Split token
    $parts = explode('.', $token);
    
    if (count($parts) !== 3) {
        return false; // Invalid format
    }
    
    list($headerEncoded, $payloadEncoded, $signatureEncoded) = $parts;
    
    // Verify signature
    $signature = base64UrlEncode(hash_hmac('sha256', "$headerEncoded.$payloadEncoded", JWT_SECRET_KEY, true));
    
    if ($signature !== $signatureEncoded) {
        return false; // Invalid signature
    }
    
    // Decode payload
    $payload = json_decode(base64UrlDecode($payloadEncoded), true);
    
    // Check expiration
    if (isset($payload['exp']) && $payload['exp'] < time()) {
        return false; // Token expired
    }
    
    return $payload;
}

/**
 * Base64 URL encode
 */
function base64UrlEncode($data) {
    return rtrim(strtr(base64_encode($data), '+/', '-_'), '=');
}

/**
 * Base64 URL decode
 */
function base64UrlDecode($data) {
    return base64_decode(strtr($data, '-_', '+/'));
}

/**
 * Middleware untuk verify JWT dari request header
 * 
 * @return array|false Payload jika valid, false jika invalid
 */
function verifyJWTFromRequest() {
    // Cek Authorization header
    $headers = getallheaders();
    
    if (!isset($headers['Authorization'])) {
        return false;
    }
    
    // Format: "Bearer <token>"
    $authHeader = $headers['Authorization'];
    
    if (!preg_match('/Bearer\s+(.*)$/i', $authHeader, $matches)) {
        return false;
    }
    
    $token = $matches[1];
    
    return verifyJWT($token);
}

/**
 * Require admin role
 * Gunakan di endpoint yang hanya bisa diakses admin
 */
function requireAdmin() {
    $payload = verifyJWTFromRequest();
    
    if (!$payload) {
        header('HTTP/1.1 401 Unauthorized');
        echo json_encode([
            'success' => false,
            'message' => 'Token tidak valid atau sudah expired'
        ]);
        exit;
    }
    
    if (!isset($payload['role']) || $payload['role'] !== 'admin') {
        header('HTTP/1.1 403 Forbidden');
        echo json_encode([
            'success' => false,
            'message' => 'Akses ditolak. Hanya admin yang bisa mengakses endpoint ini'
        ]);
        exit;
    }
    
    return $payload; // Return payload untuk digunakan di endpoint
}

/**
 * Require authenticated user (admin atau user biasa)
 */
function requireAuth() {
    $payload = verifyJWTFromRequest();
    
    if (!$payload) {
        header('HTTP/1.1 401 Unauthorized');
        echo json_encode([
            'success' => false,
            'message' => 'Token tidak valid atau sudah expired'
        ]);
        exit;
    }
    
    return $payload;
}

?>
