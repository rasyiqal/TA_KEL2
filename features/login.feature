Feature: Fitur Login Aplikasi Hadir Magang

  Scenario: Login berhasil dan mengakses menu sakit
    Given Pengguna membuka halaman login aplikasi Hadir
    When Pengguna memasukkan email "hadirsqa1@gmail.com" dan password "SQA@Hadir12345"
    And Pengguna mengeklik tombol Masuk Aplikasi
    Then Sistem harus mengarahkan ke halaman Apps Absent Utama
    And Pengguna mengeklik menu Sakit
    Then Sistem harus mengarahkan ke halaman Form Pengajuan Sakit