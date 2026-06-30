package com.bootcamp.group2.pages.web.laporan;

import com.bootcamp.group2.pages.BasePage;
import com.bootcamp.group2.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page object for the Koreksi Absen report page: /laporan/koreksi
 *
 * <p>Accessed via Sidebar → Laporan → Koreksi. Lists all koreksi submissions with
 * columns: NAMA KARYAWAN, NAMA UPLINER, UPLINER V2, UPLINER V3, JAM MASUK, JAM KELUAR,
 * STATUS, APPROVED BY, AKSI.</p>
 *
 * <p>PENDING rows have two icon buttons in the Aksi column:
 * <ul>
 *   <li>{@code aria-label="Approval Koreksi"} — opens an approval confirmation dialog</li>
 *   <li>{@code aria-label="Reject Koreksi"} — opens a rejection dialog with a required reason field</li>
 * </ul>
 * </p>
 */
public class WebLaporanKoreksiPage extends BasePage {

    private final By tableRows  = By.cssSelector("tbody tr");
    private final By pageContent = By.cssSelector("tbody");

    // Approval dialog
    private final By approveDialog = By.xpath(
        "//div[@role='dialog'][.//*[contains(normalize-space(),'Setujui Permintaan Koreksi')]]"
    );
    private final By setujuiBtn = By.xpath(
        "//div[@role='dialog']//button[normalize-space()='Setujui']"
    );

    // Rejection dialog
    private final By rejectDialog = By.xpath(
        "//div[@role='dialog'][.//*[contains(normalize-space(),'Tolak Permintaan Koreksi')]]"
    );
    private final By rejectReasonInput = By.id("rejectReason");
    private final By tolakBtn = By.xpath(
        "//div[@role='dialog']//button[normalize-space()='Tolak']"
    );

    // Success snackbar (both approve and reject share the same alert role)
    private final By successAlert = By.xpath(
        "//div[@role='alert'][contains(normalize-space(),'Berhasil')]"
    );

    @Step("Check if Laporan Koreksi page is loaded")
    public boolean isPageLoaded() {
        WaitUtils.waitForUrlContains(driver, "laporan/koreksi");
        WaitUtils.waitForVisible(driver, pageContent);
        return getCurrentUrl().contains("laporan/koreksi");
    }

    /**
     * Finds the first PENDING koreksi row for the given employee, clicks
     * "Approval Koreksi", and confirms in the dialog by clicking "Setujui".
     *
     * @param employeeName the NAMA KARYAWAN to match in the table
     * @return this page (success alert is visible after method returns)
     */
    @Step("Approve first pending koreksi for {employeeName}")
    public WebLaporanKoreksiPage approveFirstPendingKoreksiFor(String employeeName) {
        WebElement approveBtn = findPendingRowButton(employeeName, "Approval Koreksi");
        approveBtn.click();
        WaitUtils.waitForVisible(driver, approveDialog);
        WaitUtils.waitForClickable(driver, setujuiBtn).click();
        WaitUtils.waitForVisible(driver, successAlert);
        return this;
    }

    /**
     * Finds the first PENDING koreksi row for the given employee, clicks
     * "Reject Koreksi", fills in the required rejection reason, and confirms
     * by clicking "Tolak".
     *
     * @param employeeName the NAMA KARYAWAN to match in the table
     * @param reason       the rejection reason (required field)
     * @return this page (success alert is visible after method returns)
     */
    @Step("Reject first pending koreksi for {employeeName} with reason: {reason}")
    public WebLaporanKoreksiPage rejectFirstPendingKoreksiFor(String employeeName, String reason) {
        WebElement rejectBtn = findPendingRowButton(employeeName, "Reject Koreksi");
        rejectBtn.click();
        WaitUtils.waitForVisible(driver, rejectDialog);
        WebElement input = WaitUtils.waitForVisible(driver, rejectReasonInput);
        executeJavaScript(
            "var setter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
            "setter.call(arguments[0], arguments[1]);" +
            "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
            "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
            input, reason
        );
        WaitUtils.waitForClickable(driver, tolakBtn).click();
        WaitUtils.waitForVisible(driver, successAlert);
        return this;
    }

    /** Returns true if the approval success alert "Berhasil menyetujui koreksi absen" is visible. */
    @Step("Check approval success alert")
    public boolean isApprovalSuccessVisible() {
        try {
            return WaitUtils.waitForVisible(driver, successAlert, 5)
                .getText().contains("Berhasil menyetujui");
        } catch (Exception e) {
            return false;
        }
    }

    /** Returns the full text of the approval/rejection success alert. */
    @Step("Get success alert message")
    public String getSuccessAlertMessage() {
        try {
            return WaitUtils.waitForVisible(driver, successAlert, 5).getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    /** Returns true if the rejection success alert "Berhasil menolak permintaan koreksi absen" is visible. */
    @Step("Check rejection success alert")
    public boolean isRejectionSuccessVisible() {
        try {
            return WaitUtils.waitForVisible(driver, successAlert, 5)
                .getText().contains("Berhasil menolak");
        } catch (Exception e) {
            return false;
        }
    }

    // ─── Private helpers ─────────────────────────────────────────────────────

    /**
     * Locates the first table row that contains both the employee name and "PENDING"
     * in its text, then returns the button with the given aria-label from that row.
     */
    private WebElement findPendingRowButton(String employeeName, String ariaLabel) {
        WaitUtils.waitForVisible(driver, tableRows);
        List<WebElement> rows = driver.findElements(tableRows);
        return rows.stream()
            .filter(r -> r.getText().contains(employeeName) && r.getText().contains("PENDING"))
            .findFirst()
            .map(r -> {
                try {
                    return r.findElement(By.cssSelector("button[aria-label='" + ariaLabel + "']"));
                } catch (NoSuchElementException e) {
                    throw new NoSuchElementException(
                        "Button '" + ariaLabel + "' not found in PENDING row for: " + employeeName);
                }
            })
            .orElseThrow(() -> new NoSuchElementException(
                "No PENDING koreksi row found for employee: " + employeeName));
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
