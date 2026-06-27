package com.bootcamp.group2.pages.mobile.lembur;

import com.bootcamp.group2.pages.BasePage;
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
