package com.bootcamp.group2.pages.mobile.lembur;

import com.bootcamp.group2.pages.BasePage;
import com.bootcamp.group2.pages.mobile.dashboard.MobileDashboardPage;
import com.bootcamp.group2.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class MobileLemburPage extends BasePage {

    private final By pageTitle        = By.xpath("//p[normalize-space()='Halaman Lembur']");
    private final By listOvertimeLabel = By.xpath("//p[normalize-space()='List Overtime']");
    private final By totalLemburText  = By.xpath("//p[starts-with(normalize-space(.), 'Total :')]");
    private final By ajukanLemburBtn  = By.xpath("//button[normalize-space()='Ajukan Lembur']");
    private final By kembaliBtn       = By.xpath("//button[normalize-space()='Kembali']");
    private final By overtimeCards    = By.cssSelector(".MuiCard-root");

    private final By drawer           = By.cssSelector(".MuiDrawer-root");
    private final By jamMasukInput    = By.xpath("(//input[@placeholder='dd mm yyyy, hh:mm'])[1]");
    private final By jamKeluarInput   = By.xpath("(//input[@placeholder='dd mm yyyy, hh:mm'])[2]");
    private final By catatanTextarea  = By.id("notes");
    private final By ajukanSubmitBtn  = By.xpath("//button[@type='submit' and normalize-space()='Ajukan']");
    private final By resetBtn         = By.xpath("//button[normalize-space()='Reset']");
    private final By anyError         = By.cssSelector("p.Mui-error");

    public boolean isPageLoaded() {
        return isDisplayed(pageTitle);
    }

    public boolean isListOvertimeVisible() {
        return isDisplayed(listOvertimeLabel);
    }

    public boolean isTotalLemburVisible() {
        return isDisplayed(totalLemburText);
    }

    public int getTotalLemburCount() {
        String text = getText(totalLemburText);
        try {
            return Integer.parseInt(text.replace("Total :", "").trim());
        } catch (NumberFormatException e) {
            log.warn("Could not parse total lembur from: {}", text);
            return -1;
        }
    }

    public boolean isOvertimeListVisible() {
        return isDisplayed(overtimeCards);
    }

    @Step("Click 'Ajukan Lembur' button to open form")
    public MobileLemburPage clickAjukanLembur() {
        click(ajukanLemburBtn);
        WaitUtils.waitForVisible(driver, drawer);
        return this;
    }

    @Step("Click 'Kembali' button to return to dashboard")
    public MobileDashboardPage clickKembali() {
        click(kembaliBtn);
        return new MobileDashboardPage();
    }

    /**
     * Returns the visible text inside the open submission drawer.
     * Useful for debugging form state after submit.
     */
    public String getDrawerText() {
        try {
            return driver.findElement(drawer).getText();
        } catch (Exception e) {
            return "[Drawer not found or closed]";
        }
    }

    public boolean isDrawerOpen() {
        return isDisplayed(drawer);
    }

    public boolean isDrawerClosed() {
        try {
            waitForInvisible(drawer);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isFieldErrorDisplayed(String errorText) {
        By locator = By.xpath("//p[contains(@class,'Mui-error') and normalize-space()='" + errorText + "']");
        return isDisplayed(locator);
    }

    public boolean isJamMasukEmpty() {
        try {
            WaitUtils.waitFor(driver, d -> {
                String val = getAttribute(jamMasukInput, "value");
                return val == null || val.isBlank();
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isJamKeluarEmpty() {
        try {
            WaitUtils.waitFor(driver, d -> {
                String val = getAttribute(jamKeluarInput, "value");
                return val == null || val.isBlank();
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCatatanEmpty() {
        try {
            WaitUtils.waitFor(driver, d -> {
                String val = getAttribute(catatanTextarea, "value");
                return val == null || val.isBlank();
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Enter Jam Masuk: {datetime}")
    public MobileLemburPage enterJamMasuk(String datetime) {
        if (datetime != null && !datetime.isBlank()) {
            setDateTimeValue(jamMasukInput, datetime);
        }
        return this;
    }

    @Step("Enter Jam Keluar: {datetime}")
    public MobileLemburPage enterJamKeluar(String datetime) {
        if (datetime != null && !datetime.isBlank()) {
            setDateTimeValue(jamKeluarInput, datetime);
        }
        return this;
    }

    @Step("Enter Catatan: {catatan}")
    public MobileLemburPage enterCatatan(String catatan) {
        if (catatan != null && !catatan.isBlank()) {
            WebElement textarea = WaitUtils.waitForVisible(driver, catatanTextarea);
            executeJavaScript(
                "var setter = Object.getOwnPropertyDescriptor(window.HTMLTextAreaElement.prototype, 'value').set;" +
                "setter.call(arguments[0], arguments[1]);" +
                "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                textarea, catatan
            );
        }
        return this;
    }

    @Step("Click 'Ajukan' submit button")
    public MobileLemburPage clickAjukan() {
        try {
            click(ajukanSubmitBtn);
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            log.warn("Ajukan button intercepted, using jsClick");
            jsClick(WaitUtils.waitForPresence(driver, ajukanSubmitBtn));
        }
        return this;
    }

    @Step("Click 'Ajukan' expecting validation error")
    public MobileLemburPage clickAjukanExpectFail() {
        try {
            click(ajukanSubmitBtn);
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            log.warn("Ajukan button intercepted, using jsClick");
            jsClick(WaitUtils.waitForPresence(driver, ajukanSubmitBtn));
        }
        WaitUtils.waitForVisible(driver, anyError, 5);
        return this;
    }

    @Step("Click 'Reset' button")
    public MobileLemburPage clickReset() {
        try {
            click(resetBtn);
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            log.warn("Reset button intercepted, using jsClick");
            jsClick(WaitUtils.waitForPresence(driver, resetBtn));
        }
        return this;
    }

    public String getFirstCardText() {
        WebElement firstCard = WaitUtils.waitForVisible(driver, overtimeCards);
        return firstCard.getText();
    }

    public boolean isFirstCardHasUserName() {
        return getFirstCardText().contains("Hadir");
    }

    public boolean isFirstCardHasJamMasukInfo() {
        return getFirstCardText().contains("Jam Masuk :");
    }

    public boolean isFirstCardHasJamKeluarInfo() {
        return getFirstCardText().contains("Jam Keluar :");
    }

    public boolean isFirstCardHasStatusInfo() {
        return getFirstCardText().toLowerCase().contains("status");
    }

    public boolean isFirstCardHasNotesInfo() {
        return getFirstCardText().contains("Notes :");
    }

    public boolean isFirstCardHasTanggalPengajuanInfo() {
        return getFirstCardText().contains("Tanggal Pengajuan :");
    }

    // ─── Duplicate submission alert ──────────────────────────────────────────

    /**
     * Alert role element that appears when a lembur for the same date is already pending.
     * Message: "Permintaan request lembur anda sedang diproses validator 1"
     */
    private final By duplicateAlert = By.xpath(
        "//*[@role='alert' and contains(normalize-space(),'sedang diproses')]"
    );
    private final By duplicateAlertCloseBtn = By.xpath(
        "//*[@role='alert' and contains(normalize-space(),'sedang diproses')]//button"
    );

    /**
     * Returns true if the duplicate-submission alert is currently visible.
     * This appears when submitting a lembur whose date/time overlaps with an existing
     * pending submission that has not yet been processed by the validator.
     */
    public boolean isDuplicateSubmissionAlertVisible() {
        return WaitUtils.isElementPresent(driver, duplicateAlert);
    }

    /** Returns the duplicate alert message text (useful for logging). */
    public String getDuplicateAlertMessage() {
        try {
            return WaitUtils.waitForVisible(driver, duplicateAlert, 3).getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Dismisses the duplicate-submission alert and closes the drawer.
     * Call this when {@link #isDuplicateSubmissionAlertVisible()} returns true.
     * The existing pending submission (same date) will be used for the E2E flow.
     */
    public MobileLemburPage dismissDuplicateAlert() {
        By backdrop = By.cssSelector(".MuiBackdrop-root");

        // 1. Close the alert snackbar via its close button
        try {
            WaitUtils.waitForClickable(driver, duplicateAlertCloseBtn, 3).click();
            WaitUtils.waitForInvisible(driver, duplicateAlert, 5);
        } catch (Exception e) {
            log.warn("Could not close duplicate alert button: {}", e.getMessage());
        }

        // 2. Send Escape to trigger drawer close
        try {
            driver.findElement(By.tagName("body")).sendKeys(org.openqa.selenium.Keys.ESCAPE);
        } catch (Exception e) {
            log.warn("Escape key failed: {}", e.getMessage());
        }

        // 3. If backdrop is still present, jsClick it to force-dismiss the drawer
        //    (regular click fails when backdrop has aria-hidden="true" and intercepts events)
        try {
            if (WaitUtils.isElementPresent(driver, backdrop)) {
                jsClick(driver.findElement(backdrop));
            }
        } catch (Exception e) {
            log.warn("jsClick on backdrop failed: {}", e.getMessage());
        }

        // 4. Wait until the backdrop is fully gone before returning —
        //    ensures the caller (clickKembali etc.) won't be intercepted
        try {
            WaitUtils.waitForInvisible(driver, backdrop, 8);
        } catch (Exception e) {
            log.warn("Backdrop still visible after all dismiss attempts: {}", e.getMessage());
        }

        return this;
    }

    // ─── Status checks ───────────────────────────────────────────────────────

    /**
     * Returns the status value from the first lembur card.
     *
     * <p>Mobile cards display status as a text line: {@code "Status : Menunggu Approval"}.
     * This method extracts just the value after the colon. Falls back to scanning
     * the full card text if the pattern is not found.</p>
     */
    /**
     * Returns the status value from the first lembur card.
     *
     * <p>Pending cards display: {@code "Status : Menunggu Approval"} (value on same line).<br>
     * Approved cards may display the value on the NEXT non-empty line.</p>
     */
    public String getFirstCardStatusText() {
        try {
            WebElement card = WaitUtils.waitForVisible(driver, overtimeCards);
            String[] lines = card.getText().split("\n");
            for (int i = 0; i < lines.length; i++) {
                String trimmed = lines[i].trim();
                if (trimmed.toLowerCase().startsWith("status:") || trimmed.toLowerCase().startsWith("status :")) {
                    String value = trimmed.replaceFirst("(?i)status\\s*:\\s*", "").trim();
                    if (!value.isEmpty()) return value;
                    // Value is on the next non-empty line
                    for (int j = i + 1; j < lines.length; j++) {
                        String next = lines[j].trim();
                        if (!next.isEmpty() && !next.toLowerCase().startsWith("approved by")) {
                            return next;
                        }
                    }
                    return "-";
                }
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Returns true if the first card's status indicates it is awaiting approval.
     * Covers: "Menunggu Approval" (mobile), "PENDING" (web admin badge).
     */
    public boolean isFirstCardStatusPending() {
        String status = getFirstCardStatusText().toLowerCase();
        return status.contains("menunggu") || status.contains("pending");
    }

    /**
     * Returns true if the first card's status indicates it has been approved.
     * Covers: "Disetujui" (Indonesian), "Approved" (English).
     */
    public boolean isFirstCardStatusApproved() {
        String status = getFirstCardStatusText().toLowerCase();
        return status.contains("disetujui") || status.contains("approved");
    }

    /**
     * Returns true if the first card's status indicates it has been rejected.
     * Covers: "Ditolak" (Indonesian), "Rejected" (English).
     */
    public boolean isFirstCardStatusRejected() {
        String status = getFirstCardStatusText().toLowerCase();
        return status.contains("ditolak") || status.contains("rejected");
    }

    /** Refreshes the page to get the latest status from the server. */
    public MobileLemburPage refresh() {
        driver.navigate().refresh();
        WaitUtils.waitForVisible(driver, pageTitle);
        return this;
    }

    public boolean isOnLemburPage() {
        return getCurrentUrl().contains("overtime-client");
    }

    private void setDateTimeValue(By locator, String value) {
        WebElement input = WaitUtils.waitForVisible(driver, locator);
        executeJavaScript(
            "var setter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
            "setter.call(arguments[0], arguments[1]);" +
            "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
            "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
            input, value
        );
    }

    private String extractCardField(int cardIndex, String fieldLabel) {
        List<WebElement> cards = driver.findElements(overtimeCards);
        if (cards.isEmpty() || cardIndex >= cards.size()) return "-";
        return cards.get(cardIndex).findElements(By.tagName("p")).stream()
            .filter(p -> p.getText().startsWith(fieldLabel + " :"))
            .findFirst()
            .map(p -> p.getText().replace(fieldLabel + " :", "").trim())
            .orElse("-");
    }
}
