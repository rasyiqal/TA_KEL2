package com.bootcamp.group2.pages.mobile.koreksiabsen;

import com.bootcamp.group2.pages.BasePage;
import com.bootcamp.group2.pages.mobile.dashboard.MobileDashboardPage;
import com.bootcamp.group2.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class MobileKoreksiAbsenPage extends BasePage {

    private final By pageTitle          = By.xpath("//p[normalize-space()='Halaman Koreksi']");
    private final By listKoreksiLabel   = By.xpath("//p[normalize-space()='List Koreksi']");
    private final By totalKoreksiText   = By.xpath("//p[starts-with(normalize-space(.), 'Total :')]");
    private final By ajukanKoreksiBtn   = By.xpath("//button[normalize-space()='Ajukan Koreksi']");
    private final By kembaliBtn         = By.xpath("//button[normalize-space()='Kembali']");
    private final By correctionCards    = By.cssSelector(".MuiCard-root");

    private final By drawer             = By.cssSelector(".MuiDrawer-root");
    private final By jamMasukInput      = By.xpath("(//input[@placeholder='dd mm yyyy, hh:mm'])[1]");
    private final By jamKeluarInput     = By.xpath("(//input[@placeholder='dd mm yyyy, hh:mm'])[2]");
    private final By ajukanSubmitBtn    = By.xpath("//button[@type='submit' and normalize-space()='Ajukan']");
    private final By resetBtn           = By.xpath("//button[normalize-space()='Reset']");
    private final By errorValidation    = By.xpath("//p[normalize-space()='Salah satu harus diisi!']");

    public boolean isPageLoaded() {
        return isDisplayed(pageTitle);
    }

    public boolean isListKoreksiVisible() {
        return isDisplayed(listKoreksiLabel);
    }

    public boolean isTotalKoreksiVisible() {
        return isDisplayed(totalKoreksiText);
    }

    public int getTotalKoreksiCount() {
        String text = getText(totalKoreksiText);
        try {
            return Integer.parseInt(text.replace("Total :", "").trim());
        } catch (NumberFormatException e) {
            log.warn("Could not parse total koreksi from: {}", text);
            return -1;
        }
    }

    public boolean isCorrectionListVisible() {
        return isDisplayed(correctionCards);
    }

    @Step("Click 'Ajukan Koreksi' button to open form")
    public MobileKoreksiAbsenPage clickAjukanKoreksi() {
        click(ajukanKoreksiBtn);
        WaitUtils.waitForVisible(driver, drawer);
        return this;
    }

    @Step("Click 'Kembali' button to return to dashboard")
    public MobileDashboardPage clickKembali() {
        click(kembaliBtn);
        return new MobileDashboardPage();
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

    public boolean isErrorDisplayed() {
        return isDisplayed(errorValidation);
    }

    public String getErrorMessageText() {
        return getText(errorValidation);
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

    @Step("Enter Jam Masuk: {datetime}")
    public MobileKoreksiAbsenPage enterJamMasuk(String datetime) {
        if (datetime != null && !datetime.isBlank()) {
            setDateTimeValue(jamMasukInput, datetime);
        }
        return this;
    }

    @Step("Enter Jam Keluar: {datetime}")
    public MobileKoreksiAbsenPage enterJamKeluar(String datetime) {
        if (datetime != null && !datetime.isBlank()) {
            setDateTimeValue(jamKeluarInput, datetime);
        }
        return this;
    }

    @Step("Click 'Ajukan' submit button")
    public MobileKoreksiAbsenPage clickAjukan() {
        try {
            click(ajukanSubmitBtn);
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            log.warn("Ajukan button intercepted, using jsClick");
            jsClick(WaitUtils.waitForPresence(driver, ajukanSubmitBtn));
        }
        return this;
    }

    @Step("Click 'Ajukan' expecting validation error")
    public MobileKoreksiAbsenPage clickAjukanExpectFail() {
        try {
            click(ajukanSubmitBtn);
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            log.warn("Ajukan button intercepted, using jsClick");
            jsClick(WaitUtils.waitForPresence(driver, ajukanSubmitBtn));
        }
        WaitUtils.waitForVisible(driver, errorValidation, 5);
        return this;
    }

    @Step("Click 'Reset' button")
    public MobileKoreksiAbsenPage clickReset() {
        try {
            click(resetBtn);
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            log.warn("Reset button intercepted, using jsClick");
            jsClick(WaitUtils.waitForPresence(driver, resetBtn));
        }
        return this;
    }

    public String getFirstCardText() {
        WebElement firstCard = WaitUtils.waitForVisible(driver, correctionCards);
        return firstCard.getText();
    }

    public boolean isFirstCardContains(String text) {
        return getFirstCardText().contains(text);
    }

    public String getFirstCardJamMasuk() {
        return extractCardField(0, "Jam masuk");
    }

    public String getFirstCardJamKeluar() {
        return extractCardField(0, "Jam keluar");
    }

    public boolean isFirstCardHasUserName() {
        return getFirstCardText().contains("Hadir");
    }

    public boolean isFirstCardHasJamMasukInfo() {
        return getFirstCardText().contains("Jam masuk :");
    }

    public boolean isFirstCardHasJamKeluarInfo() {
        return getFirstCardText().contains("Jam keluar :");
    }

    public boolean isFirstCardHasStatusInfo() {
        return getFirstCardText().toLowerCase().contains("status");
    }

    // ─── Duplicate submission alert ──────────────────────────────────────────

    /**
     * Alert that appears when submitting a koreksi for a date that already has a
     * pending submission being processed. Message pattern: "sedang diproses".
     */
    private final By duplicateAlert = By.xpath(
        "//*[@role='alert' and contains(normalize-space(),'sedang diproses')]"
    );
    private final By duplicateAlertCloseBtn = By.xpath(
        "//*[@role='alert' and contains(normalize-space(),'sedang diproses')]//button"
    );

    /**
     * Returns true if the duplicate-submission alert is currently visible.
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
     */
    public MobileKoreksiAbsenPage dismissDuplicateAlert() {
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

        // 4. Wait until the backdrop is fully gone before returning
        try {
            WaitUtils.waitForInvisible(driver, backdrop, 8);
        } catch (Exception e) {
            log.warn("Backdrop still visible after all dismiss attempts: {}", e.getMessage());
        }

        return this;
    }

    // ─── Status checks ───────────────────────────────────────────────────────

    /**
     * Returns the status value from the first koreksi card.
     *
     * <p>Koreksi cards display status as a text line: {@code "status:-"} or
     * {@code "status:Approved"} (lowercase, no spaces). This method scans the
     * card's text line-by-line and extracts the value after the colon.</p>
     */
    /**
     * Returns the status value from the first koreksi card.
     *
     * <p>Pending cards display: {@code "status:-"} (value on same line).<br>
     * Approved/rejected cards display the value on the NEXT non-empty line:
     * {@code "status:\n\nApproved"} or {@code "status:\n\nDitolak"}.</p>
     */
    public String getFirstCardStatusText() {
        try {
            WebElement card = WaitUtils.waitForVisible(driver, correctionCards);
            String[] lines = card.getText().split("\n");
            for (int i = 0; i < lines.length; i++) {
                String trimmed = lines[i].trim();
                if (trimmed.toLowerCase().startsWith("status:") || trimmed.toLowerCase().startsWith("status :")) {
                    String value = trimmed.replaceFirst("(?i)status\\s*:\\s*", "").trim();
                    if (!value.isEmpty()) return value;
                    // Value is on the next non-empty line (e.g. "Approved", "Ditolak")
                    for (int j = i + 1; j < lines.length; j++) {
                        String next = lines[j].trim();
                        // Skip empty lines and "Approved by" (a separate field)
                        if (!next.isEmpty() && !next.toLowerCase().startsWith("approved by")) {
                            return next;
                        }
                    }
                    return "-"; // no value found after status label
                }
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }



    /** Returns true if the first card's status indicates it has been approved. */
    public boolean isFirstCardStatusApproved() {
        String status = getFirstCardStatusText().toLowerCase();
        return status.contains("approved") || status.contains("disetujui");
    }

    /** Returns true if the first card's status indicates it has been rejected. */
    public boolean isFirstCardStatusRejected() {
        String status = getFirstCardStatusText().toLowerCase();
        return status.contains("rejected") || status.contains("ditolak");
    }

    /** Refreshes the page to get the latest status from the server. */
    public MobileKoreksiAbsenPage refresh() {
        driver.navigate().refresh();
        WaitUtils.waitForVisible(driver, pageTitle);
        return this;
    }

    public boolean isOnKoreksiAbsenPage() {
        return getCurrentUrl().contains("absent/correction");
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
        List<WebElement> cards = driver.findElements(correctionCards);
        if (cards.isEmpty() || cardIndex >= cards.size()) return "-";
        return cards.get(cardIndex).findElements(By.tagName("p")).stream()
            .filter(p -> p.getText().startsWith(fieldLabel + " :"))
            .findFirst()
            .map(p -> p.getText().replace(fieldLabel + " :", "").trim())
            .orElse("-");
    }
}
