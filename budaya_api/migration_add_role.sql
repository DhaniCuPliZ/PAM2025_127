-- Migration Script: Add Role Column to User Table
-- Untuk Edukasi Budaya Nusantara App

-- 1. Tambah kolom role ke tabel user
ALTER TABLE user ADD COLUMN role VARCHAR(20) DEFAULT 'user' AFTER password;

-- 2. Set admin role untuk user admin yang sudah ada
UPDATE user SET role = 'admin' WHERE email = 'admin@budaya.com';

-- 3. Verifikasi perubahan
-- SELECT id, nama, email, role FROM user;
