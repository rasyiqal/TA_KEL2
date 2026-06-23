const { Given, When, Then } = require('@cucumber/cucumber');
const { chromium } = require('playwright'); 
const { expect } = require('chai');

let browser;
let context;
let page;

const { setDefaultTimeout } = require('@cucumber/cucumber');
setDefaultTimeout(30000);

Given('Pengguna membuka halaman login aplikasi Hadir', async function () {
    browser = await chromium.launch({ 
        headless: false, 
        channel: 'chrome', 
        slowMo: 500 
    });
    context = await browser.newContext();
    page = await context.newPage();
    
    console.log(`\n[INFO] Menjalankan Playwright: Membuka Google Chrome ke halaman login.`);
    await page.goto('https://magang.dikahadir.com/absen/login');
});

When('Pengguna memasukkan email {string} dan password {string}', async function (email, password) {
    console.log(`[INFO] Mengetik Email dan Password di Chrome...`);
    await page.locator('input[type="email"]').fill(email);
    await page.locator('input[type="password"]').fill(password);
});

When('Pengguna mengeklik tombol Masuk Aplikasi', async function () {
    console.log("[INFO] Mengklik tombol 'Masuk'...");
    await page.getByRole('button', { name: 'Masuk' }).click();
});

Then('Sistem harus mengarahkan ke halaman Apps Absent Utama', async function () {
    console.log("[INFO] Menunggu browser beralih ke halaman utama...");
    
    await page.waitForURL('**/apps/absent', { timeout: 15000 });
    
    const urlSaatIni = page.url();
    console.log(`[INFO] URL saat ini terdeteksi: ${urlSaatIni}`);
    expect(urlSaatIni).to.include('/apps/absent');
    console.log("[SUKSES] Berhasil memvalidasi halaman Apps Absent Utama.");
});

When('Pengguna mengeklik menu Sakit', async function () {
    console.log("[INFO] Mengklik tombol menu kotak 'Sakit'...");
    
    await page.getByText('Sakit', { exact: true }).click();
    
    await page.waitForLoadState('load');
});

Then('Sistem harus mengarahkan ke halaman Form Pengajuan Sakit', async function () {
    const urlSaatIni = page.url();
    console.log(`[INFO] Memvalidasi URL akhir: ${urlSaatIni}`);
    expect(urlSaatIni).to.include('/absen/sakit');
    console.log("[SUKSES] Tes Selesai! Chrome berhasil diarahkan ke halaman Form Pengajuan Sakit.");
    
    await browser.close();
});