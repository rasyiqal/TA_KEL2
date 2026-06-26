package com.bootcamp.group2.pages.mobile.koreksiabsen;

import com.bootcamp.group2.pages.BasePage;
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

    @Step("Click 'Kembali' button")
    public void clickKembali() {
        click(kembaliBtn);
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

    public String getFirstCardStatus() {
        List<WebElement> cards = driver.findElements(correctionCards);
        if (cards.isEmpty()) return "-";
        try {
            WebElement statusBox = cards.get(0).findElement(
                By.xpath(".//div[contains(@class,'MuiBox-root') and contains(.,'status')]")
            );
            return statusBox.getText().replace("status:", "").trim();
        } catch (Exception e) {
            return "-";
        }
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
