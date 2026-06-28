package com.bootcamp.group2.pages.mobile.cuti;   

import com.bootcamp.group2.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import com.bootcamp.group2.utils.WaitUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

public class MobileMenuCutiPage extends BasePage {

    private final By menuCutiBtn = By.xpath("//img[@alt='Cuti']/ancestor::a");

    private final By ajukanCutiBtn = By.xpath("//button[normalize-space()='Ajukan Cuti']");

    private final By tipeCutiDropdown = By.xpath(
        "//div[@id[starts-with(.,'mui-p-')] or contains(@class,'MuiFormControl-root')]//div[contains(@class,'MuiSelect-root') or contains(@class,'MuiInputBase-root')]"
    );

    private final By tipeCutiOptions = By.xpath(
        "//ul[contains(@id,'mui-')]//li[contains(@class,'MuiMenuItem-root')"
        + " and not(contains(@class,'Mui-disabled'))"
        + " and not(@aria-disabled='true')"
        + " and normalize-space()!='']"
    );

    private final By inputAreaBtn = By.xpath(
        "//div[@id[starts-with(.,'mui-p-')]]/form//div[contains(@class,'MuiFormControl-root')]//div[contains(@class,'MuiBox-root css-6rfaag')]"
    );

    private final By calendarWrapper = By.xpath("//div[contains(@class,'rdrCalendarWrapper')]");

    private final By catatanField = By.xpath(
        "//div[@id[starts-with(.,'mui-p-')]]//textarea | //div[@id[starts-with(.,'mui-p-')]]//input[@type='text' and not(@readonly)]"
    );

    private final By ajukanFormBtn = By.xpath(
        "//div[@id[starts-with(.,'mui-p-')]]/form//div[contains(@class,'css-1k8q9mu')]//button[contains(@class,'MuiButton-contained')]"
    );

    private final By resetBtn = By.xpath(
        "//div[@id[starts-with(.,'mui-p-')]]/form//div[contains(@class,'css-1k8q9mu')]//button[contains(@class,'MuiButton-text')]"
    );

    private final By simpanBtn = By.xpath(
        "//div[contains(@class,'MuiDialog-root')]//button[contains(@class,'MuiButton-contained') and contains(@class,'MuiButton-fullWidth')]"
    );

    private final By muiBackdrop = By.xpath(
        "//*[contains(@class,'MuiBackdrop-root') and not(contains(@style,'opacity: 0'))]"
    );

    private final By nextMonthBtn = By.xpath(
        "//button[contains(@class,'rdrNextButton')]"
    );

    @Step("Click menu Cuti")
    public MobileMenuCutiPage clickMenuCuti() {
        WaitUtils.waitForClickable(driver, menuCutiBtn);
        jsClick(menuCutiBtn);
        waitForUrl("apps/absent/leave");
        sleep(2000);
        return this;
    }

    @Step("Click button Ajukan Cuti")
    public MobileMenuCutiPage clickAjukanCuti() {
        waitForBackdropToDisappear();
        WebElement btn = WaitUtils.waitForVisible(driver, ajukanCutiBtn);
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

    @Step("Pilih tipe cuti")
    public MobileMenuCutiPage pilihTipeCuti() {
        WebElement dropdown = WaitUtils.waitForClickable(driver, tipeCutiDropdown);
        scrollToCenterViewport(dropdown);
        sleep(300);
        try {
            new Actions(driver).moveToElement(dropdown).click().perform();
        } catch (Exception e) {
            jsDispatchClick(dropdown);
        }
        sleep(500);

        List<WebElement> options = driver.findElements(tipeCutiOptions);
        if (!options.isEmpty()) {
            WebElement option = options.get(0);
            scrollToCenterViewport(option);
            sleep(200);
            try {
                new Actions(driver).moveToElement(option).click().perform();
            } catch (Exception e) {
                jsDispatchClick(option);
            }
        }
        sleep(300);
        return this;
    }

    @Step("Click Input Area (buka kalender)")
    public MobileMenuCutiPage clickInputArea() {
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

    @Step("Pilih tanggal mulai cuti: hari ini")
    public MobileMenuCutiPage pilihTanggalMulaiHariIni() {
        String hari = String.valueOf(LocalDate.now().getDayOfMonth());
        clickCalendarDate(hari);
        sleep(300);
        return this;
    }

    @Step("Pilih tanggal selesai cuti: {plusDays} hari dari sekarang")
    public MobileMenuCutiPage pilihTanggalSelesaiPlusDays(int plusDays) {
        LocalDate targetDate = LocalDate.now().plusDays(plusDays);
        String hari = String.valueOf(targetDate.getDayOfMonth());

        if (targetDate.getMonthValue() != LocalDate.now().getMonthValue()
                || targetDate.getYear() != LocalDate.now().getYear()) {
            clickNextMonth();
        }

        clickCalendarDate(hari);
        sleep(300);
        return this;
    }

    @Step("Input catatan random")
    public MobileMenuCutiPage inputCatatanRandom() {
        String[] catatan = {
            "Keperluan keluarga",
            "Urusan pribadi",
            "Pernikahan saudara",
            "Acara keluarga",
            "Keperluan mendesak"
        };
        String randomCatatan = catatan[new Random().nextInt(catatan.length)];
        try {
            WebElement field = WaitUtils.waitForVisible(driver, catatanField);
            scrollToCenterViewport(field);
            field.click();
            field.clear();
            field.sendKeys(randomCatatan);
        } catch (Exception ignored) {}
        sleep(300);
        return this;
    }

    @Step("Click button Ajukan (form)")
    public MobileMenuCutiPage clickAjukanForm() {
        WebElement btn = WaitUtils.waitForClickable(driver, ajukanFormBtn);
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

    @Step("Click button Reset")
    public MobileMenuCutiPage clickReset() {
        WebElement btn = WaitUtils.waitForClickable(driver, resetBtn);
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

    @Step("Click button Simpan")
    public MobileMenuCutiPage clickSimpan() {
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
        try {
            WebElement next = WaitUtils.waitForClickable(driver, nextMonthBtn);
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