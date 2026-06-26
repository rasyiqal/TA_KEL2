# Final Project Bootcamp Group 2 — QA Automation Framework

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Selenium](https://img.shields.io/badge/Selenium-4.27-green?logo=selenium)
![Cucumber](https://img.shields.io/badge/Cucumber-7.22-brightgreen?logo=cucumber)
![Maven](https://img.shields.io/badge/Maven-3.9+-red?logo=apachemaven)
![CI](https://img.shields.io/badge/CI-GitHub%20Actions-blue?logo=githubactions)

Framework automation testing berbasis **Selenium Java** dengan **Cucumber BDD** dan **Page Object Model**. Mendukung dua platform pengujian (Web & Mobile), dua test runner (JUnit5 / TestNG), dua laporan hasil (Allure / ExtentReports), Selenium Grid via Docker, dan pipeline CI/CD GitHub Actions.

---

## Daftar Isi

- [Kebutuhan Awal](#kebutuhan-awal)
- [Teknologi yang Digunakan](#teknologi-yang-digunakan)
- [Struktur Project](#struktur-project)
- [Cara Menjalankan Test](#cara-menjalankan-test)
- [Konfigurasi Environment](#konfigurasi-environment)
- [Platform: Web vs Mobile](#platform-web-vs-mobile)
- [Laporan Hasil Test](#laporan-hasil-test)
- [Docker & Selenium Grid](#docker--selenium-grid)
- [CI/CD GitHub Actions](#cicd-github-actions)
- [Cara Menulis Test Baru](#cara-menulis-test-baru)
- [Aturan Tim](#aturan-tim)

---

## Kebutuhan Awal

| Tool | Versi | Cara Cek |
|------|-------|----------|
| Java JDK | 21+ | `java -version` |
| Maven | 3.9+ | `mvn -version` |
| Chrome | Terbaru | Untuk run lokal |
| Docker + Docker Compose | Terbaru | Untuk Selenium Grid |
| Allure CLI | 2.29+ | Untuk generate laporan lokal |

---

## Teknologi yang Digunakan

| Kategori | Library | Versi |
|----------|---------|-------|
| Browser Automation | Selenium Java | 4.27.0 |
| Browser Automation | Selenide | 7.7.3 |
| Driver Management | WebDriverManager | 5.9.2 |
| BDD Framework | Cucumber | 7.22.0 |
| Test Runner | JUnit 5 | 5.11.4 |
| Test Runner | TestNG | 7.10.2 |
| Laporan | Allure | 2.29.0 |
| Laporan | ExtentReports | 5.1.2 |
| API Testing (future) | REST Assured | 5.5.0 |
| API Testing (future) | Playwright Java | 1.49.0 |
| Assertions | AssertJ | 3.27.3 |
| Data Test | DataFaker | 2.4.0 |
| Utilities | Lombok | 1.18.36 |
| Utilities | Awaitility | 4.2.2 |
| Konfigurasi | Typesafe Config | 1.4.3 |
| Logging | SLF4J + Logback | 2.0.17 / 1.5.17 |

---

## Struktur Project

```
final-project-bootcamp-group-2/
│
├── .github/
│   └── workflows/
│       └── ci.yml                              # Pipeline GitHub Actions
│
├── docker/
│   └── docker-compose.yml                      # Selenium Grid (Chrome + Firefox)
│
├── src/
│   ├── main/java/com/bootcamp/group2/
│   │   ├── enums/
│   │   │   └── Platform.java                   # Enum: WEB | MOBILE
│   │   ├── components/
│   │   │   └── BaseComponent.java              # Base class untuk komponen UI reusable
│   │   ├── pages/
│   │   │   ├── BasePage.java                   # Base class semua Page Object
│   │   │   ├── web/
│   │   │   │   ├── login/
│   │   │   │   │   └── WebLoginPage.java       # Halaman login web
│   │   │   │   └── dashboard/
│   │   │   │       └── WebDashboardPage.java   # Halaman dashboard web
│   │   │   └── mobile/
│   │   │       ├── login/
│   │   │       │   └── MobileLoginPage.java       # Halaman login mobile
│   │   │       ├── dashboard/
│   │   │       │   └── MobileDashboardPage.java   # Halaman utama mobile
│   │   │       └── koreksiabsen/
│   │   │           └── MobileKoreksiAbsenPage.java# Halaman koreksi absen mobile
│   │   └── utils/
│   │       ├── ConfigManager.java              # Manajemen konfigurasi (env-aware)
│   │       ├── DriverFactory.java              # ThreadLocal WebDriver factory
│   │       ├── ScreenshotUtils.java            # Ambil & lampirkan screenshot per step
│   │       └── WaitUtils.java                  # Explicit wait utilities
│   │
│   └── test/
│       ├── java/com/bootcamp/group2/
│       │   ├── hooks/
│       │   │   └── Hooks.java                  # @Before/@After lifecycle
│       │   ├── runners/
│       │   │   ├── junit5/
│       │   │   │   └── JUnit5Runner.java       # Runner JUnit 5
│       │   │   └── testng/
│       │   │       ├── TestNGRunner.java       # Runner TestNG
│       │   │       └── testng-suite.xml        # Konfigurasi TestNG suite
│       │   └── steps/
│       │       ├── ui/
│       │       │   ├── web/
│       │       │   │   └── login/
│       │       │   │       └── WebLoginSteps.java     # Step definitions login web
│       │       │   └── mobile/
│       │       │       ├── login/
│       │       │       │   └── MobileLoginSteps.java         # Step definitions login mobile
│       │       │       └── koreksiabsen/
│       │       │           └── MobileKoreksiAbsenSteps.java  # Step definitions koreksi absen mobile
│       │       └── api/                        # (Siap digunakan untuk API testing)
│       │
│       └── resources/
│           ├── config/
│           │   ├── application.conf            # Konfigurasi default (HOCON)
│           │   ├── dev.conf                    # Environment development
│           │   ├── staging.conf                # Environment staging
│           │   └── prod.conf                   # Environment production
│           ├── features/
│           │   ├── ui/
│           │   │   ├── web/
│           │   │   │   └── login/
│           │   │   │       └── login.feature   # Skenario login web
│           │   │   └── mobile/
│           │   │       ├── login/
│           │   │       │   ├── login.feature            # Skenario login mobile (positif)
│           │   │       │   └── login_negative.feature   # Skenario login mobile (negatif)
│           │   │       └── koreksiabsen/
│           │   │           ├── koreksi_absen.feature          # Skenario koreksi absen (positif)
│           │   │           └── koreksi_absen_negative.feature # Skenario koreksi absen (negatif)
│           │   └── api/                        # (Siap untuk skenario API)
│           ├── allure.properties
│           ├── junit-platform.properties       # Konfigurasi parallel JUnit5
│           └── logback-test.xml                # Konfigurasi logging
│
├── reports/                                    # Hasil laporan (diabaikan git)
├── .gitignore
├── pom.xml
└── README.md
```

---

## Cara Menjalankan Test

### Mulai Cepat

```bash
# 1. Clone repository
git clone https://github.com/your-org/final-project-bootcamp-group-2.git
cd final-project-bootcamp-group-2

# 2. Install dependencies
mvn install -DskipTests

# 3. Jalankan smoke test (web, default)
mvn test -Psmoke
```

### Pilih Test Runner

```bash
# JUnit 5 (default)
mvn test

# TestNG
mvn test -Ptestng
```

### Jalankan Berdasarkan Tag

```bash
# Smoke test saja
mvn test -Psmoke

# Regression lengkap
mvn test -Pregression

# Hanya test web
mvn test -Dcucumber.filter.tags="@web"

# Hanya test mobile
mvn test -Dplatform=mobile -Dcucumber.filter.tags="@mobile"

# Hanya test login
mvn test -Dcucumber.filter.tags="@login"

# Filter bebas
mvn test -Dcucumber.filter.tags="@smoke and @web"

# Menjalankan scenario negatif saja
mvn test -Dcucumber.filter.tags="@loginNegative or @koreksiAbsenNegatif"

### Pilih Platform

```bash
# Platform web (default)
mvn test -Dplatform=web

# Platform mobile (viewport 390x844)
mvn test -Dplatform=mobile
```

### Pilih Environment

```bash
# Development (default)
mvn test

# Staging
mvn test -Denv=staging

# Production (hati-hati)
mvn test -Denv=prod
```

### Pilih Browser

```bash
mvn test -Dbrowser=chrome     # default
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge
```

### Mode Headless

```bash
mvn test -Dheadless=true
```

### Contoh Perintah Lengkap

```bash
# Web, smoke, Chrome, headless, staging
mvn test -Psmoke \
         -Dplatform=web \
         -Denv=staging \
         -Dbrowser=chrome \
         -Dheadless=true

# Mobile, regression, Firefox
mvn test -Pregression \
         -Dplatform=mobile \
         -Dcucumber.filter.tags="@mobile" \
         -Dbrowser=firefox
```

---

## Konfigurasi Environment

Framework menggunakan **HOCON** (Typesafe Config) dengan urutan prioritas:

```
System Properties (-D flags)        ← Prioritas tertinggi
         ↓
Env config (dev/staging/prod.conf)
         ↓
Base config (application.conf)      ← Prioritas terendah
```

### Cara Ganti Environment

```bash
# Via property Maven
mvn test -Denv=staging

# Via environment variable
export ENV=staging && mvn test
```

### Tabel Konfigurasi per Environment

| File | Env | headless | remote | Grid |
|------|-----|----------|--------|------|
| `dev.conf` | Development | false | false | - |
| `staging.conf` | Staging | true | true | selenium-hub:4444 |
| `prod.conf` | Production | true | true | selenium-hub:4444 |

### Kredensial Production

Untuk production, jangan hardcode kredensial. Gunakan environment variable:

```bash
export PROD_TEST_EMAIL=your-email
export PROD_TEST_PASSWORD=your-password
mvn test -Denv=prod
```

---

## Platform: Web vs Mobile

### URL yang Digunakan

| Platform | URL |
|----------|-----|
| Web | `https://magang.dikahadir.com/authentication/login` |
| Mobile | `https://magang.dikahadir.com/absen/login` |

### Perbedaan Platform

| Fitur | Web | Mobile |
|-------|-----|--------|
| Ukuran viewport | Maximize | 390 x 844 px |
| Tombol "Lupa Password" | Tidak ada | Ada |
| Placeholder input | Tidak ada | Ada |
| Page Object | `pages/web/login/WebLoginPage` | `pages/mobile/login/MobileLoginPage` |
| Tag Cucumber | `@web` | `@mobile` |

### Cara Menjalankan per Platform

```bash
# Jalankan semua test web
mvn test -Dplatform=web -Dcucumber.filter.tags="@web"

# Jalankan semua test mobile
mvn test -Dplatform=mobile -Dcucumber.filter.tags="@mobile"
```

---

## Laporan Hasil Test

### Allure Report

```bash
# Generate laporan
mvn allure:report

# Generate dan buka di browser
mvn allure:serve
```

### ExtentReports

Otomatis dibuat setelah setiap test run. Buka langsung:

```
reports/extent/AutomationReport.html
```

### Cucumber HTML

```
reports/cucumber/cucumber-report.html
```

### Screenshot Otomatis (Step-by-Step)

Framework ini secara otomatis mengambil *screenshot* di **setiap interaksi** (seperti *open URL*, *click*, dan *type/input*).
Screenshot tersebut secara *seamless* akan langsung terlampir di dalam garis waktu (timeline) laporan **Allure** dan *step log* **ExtentReports** yang relevan, sehingga Anda bisa melihat pergerakan visual test secara penuh tanpa harus menunggu tes *failed*.

---

## Docker & Selenium Grid

### Menjalankan Grid

```bash
# Jalankan Hub + Chrome + Firefox
docker-compose -f docker/docker-compose.yml up -d

# Lihat Grid Console
open http://localhost:4444/ui

# Lihat browser Chrome secara live (noVNC)
open http://localhost:7900
```

### Menjalankan Test di Grid

```bash
mvn test -Denv=staging -Dremote=true -Dgrid.url=http://localhost:4444/wd/hub -Dheadless=true
```

### Menambah Node Chrome

```bash
docker-compose -f docker/docker-compose.yml up -d --scale chrome=4
```

### Menghentikan Grid

```bash
docker-compose -f docker/docker-compose.yml down
```

---

## CI/CD GitHub Actions

### Gambaran Pipeline

| Job | Trigger | Keterangan |
|-----|---------|------------|
| `build` | Setiap push/PR | Compile & validasi project |
| `smoke-tests` | Setiap push/PR | Jalankan skenario `@smoke` |
| `regression` | Terjadwal / Manual | Suite penuh semua skenario |

### Trigger Manual

Buka **Actions → QA Automation CI → Run workflow** lalu pilih:
- `environment`: dev / staging / prod
- `tags`: @smoke / @regression / bebas
- `browser`: chrome / firefox / edge

### Secrets yang Diperlukan

Tambahkan di **Settings → Secrets and variables → Actions**:

| Secret | Keterangan |
|--------|------------|
| `PROD_TEST_EMAIL` | Email akun test production |
| `PROD_TEST_PASSWORD` | Password akun test production |
| `SLACK_WEBHOOK_URL` | Webhook Slack untuk notifikasi gagal |

### Allure di GitHub Pages

Hasil regression otomatis di-deploy ke GitHub Pages. Aktifkan di:
**Settings → Pages → Source → branch gh-pages**

---

## Cara Menulis Test Baru

### 1. Buat Feature File

Berdasarkan struktur modular, letakkan *feature* ke dalam `ui/<platform>/<modul>/`.

```gherkin
# src/test/resources/features/ui/web/nama_fitur/nama_fitur.feature
@web @nama-fitur
Feature: Nama Fitur

  @smoke
  Scenario: Skenario sukses
    Given user opens nama page
    When  user clicks something
    Then  success message is displayed
```

### 2. Buat Page Object

Untuk fitur web, buat di `pages/web/<modul>/`. Untuk mobile di `pages/mobile/<modul>/`.

```java
// pages/web/nama_fitur/WebNamaPage.java
package com.bootcamp.group2.pages.web.nama_fitur;

import com.bootcamp.group2.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class WebNamaPage extends BasePage {

    private final By someElement = By.id("some-id");

    @Step("Klik elemen tertentu")
    public WebNamaPage clickSomething() {
        click(someElement); // Screenshot akan otomatis terambil di dalam BasePage
        return this;
    }
}
```

### 3. Buat Step Definitions

```java
// steps/ui/web/nama_fitur/NamaSteps.java
package com.bootcamp.group2.steps.ui.web.nama_fitur;

import com.bootcamp.group2.pages.web.nama_fitur.WebNamaPage;
import io.cucumber.java.en.Given;

public class NamaSteps {
    private WebNamaPage namaPage;

    @Given("user opens nama page")
    public void userOpensNamaPage() {
        namaPage = new WebNamaPage();
        namaPage.open();
    }
}
```

### 4. Jalankan Test

```bash
mvn test -Dcucumber.filter.tags="@nama-fitur"
```

---

## Aturan Tim

### Boleh
- Step definition harus **tipis** — semua logika ada di Page Object
- Gunakan **AssertJ** untuk assertion, bukan raw JUnit assert
- Gunakan `@Step` Allure di method Page Object
- Gunakan `ConfigManager` untuk semua nilai konfigurasi — jangan hardcode URL
- Tag setiap skenario dengan tepat (`@smoke`, `@regression`, `@web`, `@mobile`, `@loginNegative`)
- Pisahkan feature file antara skenario positif (contoh: `login.feature`) dan negatif (`login_negative.feature`)
- Gunakan `DataFaker` untuk data test dinamis

### Tidak Boleh
- Jangan gunakan `Thread.sleep()` — gunakan `WaitUtils` atau `Awaitility`
- Jangan taruh assertion di Page Object — hanya di step definitions
- Jangan gunakan XPath absolut — utamakan ID > Name > CSS > XPath relatif
- Jangan extend Page Object — gunakan komposisi (composition)
- Jangan hardcode kredensial di kode — gunakan environment variable untuk prod
- Jangan commit folder `target/`, `reports/`, atau file `.env`

---

## Kontribusi

1. Buat branch dari `develop`: `git checkout -b feature/nama-fitur`
2. Tulis test mengikuti aturan di atas
3. Pastikan `mvn test -Psmoke` berhasil sebelum buat PR
4. PR harus di-review minimal 1 anggota tim

---

*Dibuat oleh QA Bootcamp Group 2*
