package com.bootcamp.group2.pages.mobile.m_sakit;

import com.bootcamp.group2.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import com.bootcamp.group2.utils.WaitUtils;

import java.time.LocalDate;

public class MobileMenuSakitPage extends BasePage {

    private final By menuSakitBtn = By.xpath("//img[@alt='Sakit']/ancestor::a");

    private final By ajukanSakitBtn = By.xpath(
        "(//button[contains(@class,'MuiButton-contained') and normalize-space()='Ajukan Sakit'])[last()]"
    );

    private final By inputAreaBtn = By.xpath(
        "//label[contains(text(),'Tanggal')]/following-sibling::div[contains(@class,'MuiBox-root')]"
        + " | "
        + "//label[contains(text(),'Tanggal')]/parent::*//div[contains(@class,'MuiInputBase-root')]"
        + " | "
        + "//div[contains(@class,'MuiBox-root') and .//svg[@data-testid='AccessAlarmIcon']]"
    );

    private final By calendarWrapper = By.xpath(
        "//div[contains(@class,'rdrCalendarWrapper')]"
    );

    private final By simpanBtn = By.xpath(
        "//div[contains(@class,'MuiDialog-root')]//button[contains(@class,'MuiButton-contained') and contains(@class,'MuiButton-fullWidth')]"
    );

    private final By muiBackdrop = By.xpath(
        "//*[contains(@class,'MuiBackdrop-root') and not(contains(@style,'opacity: 0'))]"
    );

    @Step("Click menu Sakit")
    public MobileMenuSakitPage clickMenuSakit() {
        WaitUtils.waitForClickable(driver, menuSakitBtn);
        jsClick(menuSakitBtn);
        waitForUrl("apps/absent/sick");
        sleep(2000);
        return this;
    }

    @Step("Click button Ajukan Sakit")
    public MobileMenuSakitPage clickAjukanSakit() {
        waitForBackdropToDisappear();
        WebElement btn = WaitUtils.waitForVisible(driver, ajukanSakitBtn);
        scrollToCenterViewport(btn);
        sleep(500);
        try {
            new Actions(driver).moveToElement(btn).click().perform();
        } catch (Exception e) {
            jsDispatchClick(btn);
        }
        sleep(800);
        return this;
    }

    @Step("Click Input Area (buka kalender)")
    public MobileMenuSakitPage clickInputArea() {
        WebElement area = WaitUtils.waitForClickable(driver, inputAreaBtn);
        scrollToCenterViewport(area);
        sleep(300);
        try {
            new Actions(driver).moveToElement(area).click().perform();
        } catch (Exception e) {
            jsClick(inputAreaBtn);
        }
        WaitUtils.waitForVisible(driver, calendarWrapper);
        sleep(500);
        return this;
    }

    @Step("Pilih tanggal mulai sakit: hari ini")
    public MobileMenuSakitPage pilihTanggalMulaiHariIni() {
        String hari = String.valueOf(LocalDate.now().getDayOfMonth());
        clickCalendarDate(hari);
        sleep(300);
        return this;
    }

    @Step("Pilih tanggal selesai sakit: {plusDays} hari dari sekarang")
    public MobileMenuSakitPage pilihTanggalSelesaiPlusDays(int plusDays) {
        LocalDate targetDate = LocalDate.now().plusDays(plusDays);
        String hari = String.valueOf(targetDate.getDayOfMonth());

        // Jika tanggal selesai beda bulan, klik navigasi next month dulu
        if (targetDate.getMonthValue() != LocalDate.now().getMonthValue()
                || targetDate.getYear() != LocalDate.now().getYear()) {
            clickNextMonth();
        }

        clickCalendarDate(hari);
        sleep(300);
        return this;
    }

    @Step("Click button Simpan")
    public MobileMenuSakitPage clickSimpan() {
        WebElement btn = WaitUtils.waitForClickable(driver, simpanBtn);
        scrollToCenterViewport(btn);
        sleep(300);
        try {
            new Actions(driver).moveToElement(btn).click().perform();
        } catch (Exception e) {
            jsDispatchClick(btn);
        }
        sleep(500);
        return this;
    }

    private void clickCalendarDate(String tanggal) {
        By dateBtn = By.xpath(
            "//div[contains(@class,'rdrDays')]"
            + "//button[contains(@class,'rdrDay') and not(contains(@class,'rdrDayPassive'))]"
            + "//span[contains(@class,'rdrDayNumber')]/span[normalize-space()='" + tanggal + "']"
        );
        WebElement el = WaitUtils.waitForClickable(driver, dateBtn);
        scrollToCenterViewport(el);
        try {
            new Actions(driver).moveToElement(el).click().perform();
        } catch (Exception e) {
            jsDispatchClick(el);
        }
    }

    private void clickNextMonth() {
        By nextBtn = By.xpath("//button[contains(@class,'rdrPprevButton') or contains(@class,'rdrNextButton')]//i[not(contains(@class,'rdrPprev'))]/..");
        try {
            WebElement next = WaitUtils.waitForClickable(driver, nextBtn);
            next.click();
            sleep(500);
        } catch (Exception ignored) {}
    }

    private void waitForBackdropToDisappear() {
        try {
            WaitUtils.waitForInvisible(driver, muiBackdrop);
        } catch (Exception ignored) {}
    }

    private void scrollToCenterViewport(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({behavior:'instant', block:'center', inline:'center'});",
            element
        );
    }

    private void jsDispatchClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
            "var el = arguments[0];"
            + "['mousedown','mouseup','click'].forEach(function(type) {"
            + "  var evt = new MouseEvent(type, {bubbles:true, cancelable:true, view:window});"
            + "  el.dispatchEvent(evt);"
            + "});",
            element
        );
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (Exception ignored) {}
    }
}