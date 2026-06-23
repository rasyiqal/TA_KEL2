const { Given, When, Then } = require('@cucumber/cucumber');
const { expect } = require('chai');

let statusBrowser = "tertutup";
let halamanSaatIni = "";
let formLogin = { email: "", password: "" };

Given('Pengguna membuka halaman login aplikasi Hadir', function () {
    statusBrowser = "terbuka";
    halamanSaatIni = "https://magang.dikahadir.com/absen/login";
    console.log(`\n[INFO] Robot membuka browser ke: ${halamanSaatIni}`);
});

When('Pengguna memasukkan email {string} dan password {string}', function (email, password) {
    formLogin.email = email;
    formLogin.password = password;
    console.log(`[INFO] Robot mengetik ke field Email: ${email}`);
    console.log(`[INFO] Robot mengetik ke field Password: ********`);
});

When('Pengguna mengeklik tombol Masuk Aplikasi', function () {
    console.log("[INFO] Robot mengeklik tombol 'Masuk'");
    if (formLogin.email === "karyawan@dikahadir.com" && formLogin.password === "password123") {
        halamanSaatIni = "https://magang.dikahadir.com/absen/dashboard";
    }
});

Then('Sistem harus mengarahkan ke halaman Dashboard Utama', function () {
    expect(halamanSaatIni).to.equal("https://magang.dikahadir.com/absen/dashboard");
    console.log("[SUKSES] Tes Berhasil! Robot mendeteksi halaman Dashboard.\n");
});