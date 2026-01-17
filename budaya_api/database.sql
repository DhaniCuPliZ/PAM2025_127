-- Database untuk Aplikasi Edukasi Budaya Nusantara
-- Buat database baru
CREATE DATABASE IF NOT EXISTS budaya_nusantara;
USE budaya_nusantara;

-- Tabel untuk menyimpan data user
CREATE TABLE IF NOT EXISTS user (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nama VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role ENUM('admin', 'user') DEFAULT 'user',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabel untuk menyimpan data budaya
CREATE TABLE IF NOT EXISTS budaya (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nama VARCHAR(100) NOT NULL,
  daerah VARCHAR(100) NOT NULL,
  deskripsi TEXT NOT NULL,
  gambar VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert sample data budaya
INSERT INTO budaya (nama, daerah, deskripsi, gambar) VALUES
('Tari Kecak', 'Bali', 'Tarian tradisional Bali yang menggambarkan kisah Ramayana dengan gerakan massal dan suara "cak". Tarian ini biasanya dipentaskan oleh puluhan penari pria yang duduk melingkar dan mengangkat kedua tangan sambil menyerukan "cak" secara berulang-ulang.', 'https://picsum.photos/400/300?random=1'),
('Wayang Kulit', 'Jawa', 'Seni pertunjukan boneka kulit yang menceritakan kisah pewayangan dengan diiringi gamelan. Wayang kulit merupakan warisan budaya Indonesia yang telah diakui UNESCO sebagai Masterpiece of Oral and Intangible Heritage of Humanity.', 'https://picsum.photos/400/300?random=2'),
('Reog Ponorogo', 'Jawa Timur', 'Kesenian tari tradisional yang menampilkan topeng singa raksasa dengan bulu merak. Topeng ini memiliki berat mencapai 50-60 kg dan dimainkan dengan cara digigit oleh penari yang sangat kuat.', 'https://picsum.photos/400/300?random=3'),
('Tari Tor-Tor', 'Sumatera Utara', 'Tarian tradisional Batak yang biasa ditampilkan dalam upacara adat seperti pernikahan, kematian, dan pesta panen. Tarian ini diiringi dengan musik gondang sabangunan dan memiliki gerakan yang energik.', 'https://picsum.photos/400/300?random=4'),
('Angklung', 'Jawa Barat', 'Alat musik tradisional yang terbuat dari bambu dan dimainkan dengan cara digoyangkan. Angklung telah diakui UNESCO sebagai Warisan Budaya Takbenda pada tahun 2010.', 'https://picsum.photos/400/300?random=5'),
('Tari Saman', 'Aceh', 'Tarian tradisional Aceh yang dimainkan oleh puluhan penari dengan gerakan yang kompak dan cepat. Tarian ini biasanya diiringi dengan syair-syair bernuansa Islam dan tepukan tangan yang harmonis.', 'https://picsum.photos/400/300?random=6'),
('Batik', 'Jawa', 'Kain tradisional Indonesia yang dibuat dengan teknik pewarnaan kain menggunakan malam (lilin) untuk mencegah pewarnaan sebagian dari kain. Batik telah diakui UNESCO sebagai Warisan Kemanusiaan untuk Budaya Lisan dan Nonbendawi.', 'https://picsum.photos/400/300?random=7'),
('Tari Pendet', 'Bali', 'Tarian penyambutan yang berasal dari Bali. Tarian ini awalnya merupakan tarian pemujaan yang banyak diperagakan di pura-pura, namun kini telah berkembang menjadi tarian penyambutan.', 'https://picsum.photos/400/300?random=8');

-- Insert sample user untuk testing (dengan email Gmail yang valid)
INSERT INTO user (nama, email, password, role) VALUES
('Admin Budaya', 'admin@gmail.com', 'admin123', 'admin'),
('Budi Santoso', 'budi.santoso@gmail.com', 'budi123', 'user');

-- Tabel untuk menyimpan rekomendasi budaya
CREATE TABLE IF NOT EXISTS rekomendasi (
  id INT AUTO_INCREMENT PRIMARY KEY,
  budaya_id INT NOT NULL,
  budaya_rekomendasi_id INT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (budaya_id) REFERENCES budaya(id) ON DELETE CASCADE,
  FOREIGN KEY (budaya_rekomendasi_id) REFERENCES budaya(id) ON DELETE CASCADE,
  UNIQUE KEY unique_rekomendasi (budaya_id, budaya_rekomendasi_id)
);

-- Insert sample data rekomendasi
-- Rekomendasi untuk Tari Kecak (id=1)
INSERT INTO rekomendasi (budaya_id, budaya_rekomendasi_id) VALUES
(1, 8),  -- Tari Kecak -> Tari Pendet
(1, 2),  -- Tari Kecak -> Wayang Kulit
(1, 6);  -- Tari Kecak -> Tari Saman
