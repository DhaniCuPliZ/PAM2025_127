<?php
// Generate password hash untuk admin123
$password = 'admin123';
$hash = password_hash($password, PASSWORD_BCRYPT);

echo "Password: $password\n";
echo "Hash: $hash\n\n";

echo "Copy query ini dan jalankan di phpMyAdmin:\n\n";
echo "UPDATE user SET password = '$hash' WHERE email = 'admin@gmail.com';\n";
?>
