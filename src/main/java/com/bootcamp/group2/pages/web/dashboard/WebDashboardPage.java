package com.bootcamp.group2.pages.web.dashboard;

import com.bootcamp.group2.pages.BasePage;
import com.bootcamp.group2.pages.web.approval.WebKoreksiApprovalPage;
import com.bootcamp.group2.pages.web.approval.WebLemburApprovalPage;
import com.bootcamp.group2.pages.web.laporan.WebLaporanKoreksiPage;
import com.bootcamp.group2.pages.web.laporan.WebLaporanLemburPage;
import com.bootcamp.group2.pages.web.login.WebLoginPage;
import com.bootcamp.group2.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class WebDashboardPage extends BasePage {

    private static final String PENDING_URL = "https://magang.dikahadir.com/dashboards/pending";

    // Sidebar navigation
    private final By dashboardMenuToggle = By.xpath("//p[normalize-space()='Dashboard Menu']");
    private final By pendingMenuLink     = By.xpath("//a[contains(@href,'/dashboards/pending')] | //li[normalize-space()='Pending']");

    // Pending dashboard sections
    private final By lemburEmployeeLink   = By.xpath("//a[contains(@href,'/admin/v1-lembur/')]");
    private final By koreksiEmployeeLink  = By.xpath("//a[contains(@href,'/admin/v1-koreksi/') or contains(@href,'/admin/correction/')]");
    private final By pendingTableSection  = By.xpath("//div[contains(@class,'MuiCard') or contains(@class,'MuiPaper')]");

    // General page indicators
    private final By pageBody            = By.tagName("body");

    @Step("Check if web dashboard is loaded (landed on /dashboards/pending)")
    public boolean isDashboardLoaded() {
        try {
            WaitUtils.waitForUrlContains(driver, "dashboards/pending");
            return true;
        } catch (org.openqa.selenium.TimeoutException e) {
            log.warn("Admin did not land on /dashboards/pending after login. Current URL: {}", getCurrentUrl());
            return false;
        }
    }

    @Step("Navigate to Pending dashboard")
    public WebDashboardPage navigateToPending() {
        driver.get(PENDING_URL);
        WaitUtils.waitForUrlContains(driver, "dashboards/pending");
        try { Thread.sleep(2000); } catch (Exception e) {}
        driver.navigate().refresh();
        WaitUtils.waitForUrlContains(driver, "dashboards/pending");
        return this;
    }

    @Step("Check if pending dashboard is loaded")
    public boolean isPendingDashboardLoaded() {
        return getCurrentUrl().contains("dashboards/pending");
    }

    @Step("Get total pending Lembur count for employee {employeeName}")
    public int getLemburPendingCount(String employeeName) {
        try {
            By countLocator = By.xpath(
                "//a[contains(normalize-space(), '" + employeeName + "') and contains(@href,'/admin/v1-lembur/')]/following-sibling::td | " +
                "//a[contains(normalize-space(), '" + employeeName + "') and contains(@href,'/admin/v1-lembur/')]/parent::tr/td[last()]"
            );
            String countText = getText(countLocator).trim();
            return Integer.parseInt(countText);
        } catch (Exception e) {
            log.warn("Could not get Lembur count for {}: {}", employeeName, e.getMessage());
            return 0;
        }
    }

    @Step("Get total pending Koreksi count for employee {employeeName}")
    public int getKoreksiPendingCount(String employeeName) {
        try {
            By countLocator = By.xpath(
                "//a[contains(normalize-space(), '" + employeeName + "') and (contains(@href,'/admin/v1-koreksi/') or contains(@href,'/admin/correction/'))]/parent::tr/td[last()]"
            );
            String countText = getText(countLocator).trim();
            return Integer.parseInt(countText);
        } catch (Exception e) {
            log.warn("Could not get Koreksi count for {}: {}", employeeName, e.getMessage());
            return 0;
        }
    }

    @Step("Click Lembur pending link for employee {employeeName}")
    public WebLemburApprovalPage clickLemburEmployeeLink(String employeeName) {
        By link = By.xpath(
            "//a[contains(normalize-space(), '" + employeeName + "') and contains(@href,'/admin/v1-lembur/')]"
        );
        try {
            click(link);
        } catch (Exception e) {
            log.warn("Link not found for {}, clicking first lembur link", employeeName);
            click(By.xpath("(//a[contains(@href,'/admin/v1-lembur/')])[1]"));
        }
        WaitUtils.waitForUrlContains(driver, "/admin/v1-lembur/");
        return new WebLemburApprovalPage();
    }

    @Step("Click Koreksi pending link for employee {employeeName}")
    public WebKoreksiApprovalPage clickKoreksiEmployeeLink(String employeeName) {
        By link = By.xpath(
            "//a[contains(normalize-space(), '" + employeeName + "') and (contains(@href,'/admin/v1-koreksi/') or contains(@href,'/admin/correction/'))]"
        );
        try {
            click(link);
        } catch (Exception e) {
            log.warn("Link not found for {}, checking all links", employeeName);
            java.util.List<WebElement> allLinks = driver.findElements(By.tagName("a"));
            for (WebElement a : allLinks) {
                System.out.println("DEBUG DASHBOARD LINK: href=" + a.getAttribute("href") + " text=" + a.getText());
            }
            click(By.xpath("(//a[contains(@href,'/admin/v1-koreksi/') or contains(@href,'/admin/correction/')])[1]"));
        }
        WaitUtils.waitForUrlContains(driver, "/admin/");
        return new WebKoreksiApprovalPage();
    }

    @Step("Check if Lembur pending entry exists for employee {employeeName}")
    public boolean isLemburPendingVisible(String employeeName) {
        By link = By.xpath(
            "//a[normalize-space()='" + employeeName + "' and contains(@href,'/admin/v1-lembur/')]"
        );
        return isDisplayed(link);
    }

    @Step("Check if Koreksi pending entry exists for employee {employeeName}")
    public boolean isKoreksiPendingVisible(String employeeName) {
        By link = By.xpath(
            "//a[normalize-space()='" + employeeName + "' and (contains(@href,'/admin/v1-koreksi/') or contains(@href,'/admin/correction/'))]"
        );
        return isDisplayed(link);
    }

    @Step("Check if pending dashboard has any items")
    public boolean hasPendingItems() {
        try {
            List<WebElement> links = driver.findElements(lemburEmployeeLink);
            List<WebElement> koreksiLinks = driver.findElements(koreksiEmployeeLink);
            return !links.isEmpty() || !koreksiLinks.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    // ─── Laporan sidebar navigation ──────────────────────────────────────────

    /**
     * Sidebar top-level item "Laporan" — click to expand its sub-menu.
     * The item is a div.sidebar__item containing a &lt;p&gt; with text "Laporan".
     */
    private final By laporanSidebarItem = By.xpath(
        "//div[contains(@class,'sidebar__item') and ./p[normalize-space()='Laporan']]"
    );

    /**
     * Sub-menu items that appear after "Laporan" is expanded.
     * Each item is a MuiBox div whose complete text matches exactly "Lembur" or "Koreksi".
     */
    private final By laporanLemburSubItem = By.xpath(
        "//*[contains(@class,'MuiBox-root') and normalize-space()='Lembur']"
    );
    private final By laporanKoreksiSubItem = By.xpath(
        "//*[contains(@class,'MuiBox-root') and normalize-space()='Koreksi']"
    );

    @Step("Expand Laporan section in sidebar")
    public WebDashboardPage clickLaporanMenu() {
        click(laporanSidebarItem);
        // Wait for at least one sub-item to become clickable
        WaitUtils.waitForClickable(driver, laporanLemburSubItem);
        return this;
    }

    @Step("Click Laporan > Lembur sub-menu")
    public WebLaporanLemburPage clickLaporanLemburSubMenu() {
        click(laporanLemburSubItem);
        WaitUtils.waitForUrlContains(driver, "laporan/overtime");
        return new WebLaporanLemburPage();
    }

    @Step("Click Laporan > Koreksi sub-menu")
    public WebLaporanKoreksiPage clickLaporanKoreksiSubMenu() {
        click(laporanKoreksiSubItem);
        WaitUtils.waitForUrlContains(driver, "laporan/koreksi");
        return new WebLaporanKoreksiPage();
    }

    /**
     * Logout from the web admin panel.
     *
     * <p>Flow: click the profile button in the header (MuiButton-text containing "Admin")
     * → a User Profile dialog appears → click the "Logout" button inside it
     * → wait for redirect to /authentication/login.</p>
     *
     * <p>Falls back to clearing cookies + navigating to the login URL if the button
     * flow fails for any reason.</p>
     */
    @Step("Logout from web admin panel")
    public WebLoginPage logout() {
        // Profile button is the MuiButton-text in the header (contains admin name)
        By profileBtn    = By.xpath("//header//button[contains(@class,'MuiButton-text')]");
        // Logout button inside the User Profile dialog
        By logoutDialogBtn = By.xpath("//button[normalize-space()='Logout' and contains(@class,'MuiButton-contained')]");
        try {
            WaitUtils.waitForClickable(driver, profileBtn, 5).click();
            WaitUtils.waitForClickable(driver, logoutDialogBtn, 5).click();
            WaitUtils.waitForUrlContains(driver, "authentication/login");
        } catch (Exception e) {
            log.warn("Profile/Logout button flow failed — clearing session manually: {}", e.getMessage());
            driver.manage().deleteAllCookies();
            try {
                executeJavaScript("window.localStorage.clear(); window.sessionStorage.clear();");
            } catch (Exception ignored) {}
            navigateTo(com.bootcamp.group2.utils.ConfigManager.getWebBaseUrl());
        }
        return new WebLoginPage();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
