package com.bootcamp.group2.pages.web.approval;

import com.bootcamp.group2.pages.BasePage;
import com.bootcamp.group2.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page object for Web Admin Lembur Approval page.
 * URL pattern: /admin/v1-lembur/{upliner_id}?start_date=...&end_date=...&fullname=...
 *
 * Table columns: NAMA KARYAWAN | TANGGAL PENGAJUAN | TANGGAL ABSEN |
 *                TANGGAL LEMBUR | UPLINER | UPLINER 2 | UPLINER 3 | NOTES | STATUS
 *
 * Status values:
 *   null  → displayed as "PENDING"   (orange badge)
 *   true  → displayed as "Approved"  (green badge)
 *   false → displayed as "Rejected"  (red badge)
 */
public class WebLemburApprovalPage extends BasePage {

    // Page structure
    private final By pageHeader       = By.xpath("//h4[contains(text(),'Validator') and contains(text(),'Lembur')] | //h6[contains(text(),'Validator 1 Lembur')]");
    private final By searchInput      = By.cssSelector("input[type='search'], input[placeholder*='nama']");
    private final By searchButton     = By.cssSelector("button[type='submit']");
    private final By resetButton      = By.xpath("//button[normalize-space()='Reset']");

    // Table rows
    private final By tableDataRows    = By.cssSelector("tr.MuiTableRow-root:not(.MuiTableRow-head)");
    private final By totalRowCount    = By.cssSelector(".MuiTablePagination-displayedRows");

    // Status badges — "• PENDING", "Approved", "Rejected" inside MuiBox
    private final By pendingBadge     = By.xpath("//div[contains(@class,'MuiBox-root') and normalize-space()='PENDING']");
    private final By approvedBadge    = By.xpath("//div[contains(@class,'MuiBox-root') and (normalize-space()='Approved' or normalize-space()='APPROVED')]");
    private final By rejectedBadge    = By.xpath("//div[contains(@class,'MuiBox-root') and (normalize-space()='Rejected' or contains(normalize-space(),'Rejected'))]");

    // Approval modal (appears after clicking PENDING badge)
    private final By approvalModal    = By.cssSelector("[role='dialog']");
    private final By approveButton    = By.xpath("//*[@role='dialog']//button[contains(normalize-space(),'Setuju') or contains(normalize-space(),'Approve') or contains(normalize-space(),'Terima')]");
    private final By rejectButton     = By.xpath("//*[@role='dialog']//button[contains(normalize-space(),'Tolak') or contains(normalize-space(),'Reject')]");
    private final By rejectNotesInput = By.xpath("//*[@role='dialog']//textarea | //*[@role='dialog']//input[@type='text']");
    private final By confirmButton    = By.xpath("//*[@role='dialog']//button[contains(normalize-space(),'Konfirmasi') or contains(normalize-space(),'Confirm') or contains(normalize-space(),'Ya') or contains(normalize-space(),'Submit')]");

    // Koreksi-style: sometimes modal has a different structure
    private final By modalAnyButton   = By.xpath("//*[@role='dialog']//button");

    @Step("Check if Lembur approval page is loaded")
    public boolean isPageLoaded() {
        try {
            WaitUtils.waitForUrlContains(driver, "admin/v1-lembur");
            return getCurrentUrl().contains("admin/v1-lembur");
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Check if page header displays 'Validator 1 Lembur'")
    public boolean isPageHeaderVisible() {
        return isDisplayed(pageHeader);
    }

    @Step("Search lembur by employee name {name}")
    public WebLemburApprovalPage searchByName(String name) {
        type(searchInput, name);
        click(searchButton);
        WaitUtils.waitForInvisible(driver, By.cssSelector(".MuiCircularProgress-root"));
        return this;
    }

    @Step("Get total number of lembur rows in table")
    public int getTableRowCount() {
        try {
            List<WebElement> rows = driver.findElements(tableDataRows);
            return rows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    @Step("Check if any PENDING lembur items exist")
    public boolean hasPendingItems() {
        return isDisplayed(pendingBadge);
    }

    @Step("Get count of PENDING badges in the table")
    public int getPendingCount() {
        return driver.findElements(pendingBadge).size();
    }

    @Step("Check if any Approved lembur items exist")
    public boolean hasApprovedItems() {
        return isDisplayed(approvedBadge);
    }

    @Step("Check if any Rejected lembur items exist")
    public boolean hasRejectedItems() {
        return isDisplayed(rejectedBadge);
    }

    @Step("Click the first PENDING badge to open approval modal")
    public WebLemburApprovalPage clickFirstPendingBadge() {
        WebElement badge = WaitUtils.waitForVisible(driver, pendingBadge);
        jsClick(badge);
        return this;
    }

    @Step("Click PENDING badge for row containing employee {employeeName}")
    public WebLemburApprovalPage clickPendingBadgeForEmployee(String employeeName) {
        By rowPendingBadge = By.xpath(
            "//tr[.//h6[normalize-space()='" + employeeName + "']]//div[contains(@class,'MuiBox-root') and normalize-space()='PENDING']"
        );
        WebElement badge = WaitUtils.waitForVisible(driver, rowPendingBadge);
        jsClick(badge);
        return this;
    }

    @Step("Check if approval modal is displayed")
    public boolean isApprovalModalVisible() {
        try {
            WaitUtils.waitForVisible(driver, approvalModal, 5);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Click Approve (Setuju) button in modal")
    public WebLemburApprovalPage clickApproveButton() {
        click(approveButton);
        return this;
    }

    @Step("Click Reject (Tolak) button in modal")
    public WebLemburApprovalPage clickRejectButton() {
        click(rejectButton);
        return this;
    }

    @Step("Enter reject notes: {notes}")
    public WebLemburApprovalPage enterRejectNotes(String notes) {
        type(rejectNotesInput, notes);
        return this;
    }

    @Step("Confirm action in modal")
    public WebLemburApprovalPage confirmModal() {
        if (isDisplayed(confirmButton)) {
            click(confirmButton);
        }
        return this;
    }

    @Step("Approve lembur for employee {employeeName}")
    public WebLemburApprovalPage approveLembur(String employeeName) {
        clickPendingBadgeForEmployee(employeeName);
        WaitUtils.waitForVisible(driver, approvalModal, 5);
        click(approveButton);
        if (isDisplayed(confirmButton)) {
            click(confirmButton);
        }
        WaitUtils.waitForInvisible(driver, approvalModal);
        return this;
    }

    @Step("Reject lembur for employee {employeeName} with notes: {rejectNotes}")
    public WebLemburApprovalPage rejectLembur(String employeeName, String rejectNotes) {
        clickPendingBadgeForEmployee(employeeName);
        WaitUtils.waitForVisible(driver, approvalModal, 5);
        click(rejectButton);
        if (isDisplayed(rejectNotesInput)) {
            type(rejectNotesInput, rejectNotes);
        }
        if (isDisplayed(confirmButton)) {
            click(confirmButton);
        }
        WaitUtils.waitForInvisible(driver, approvalModal);
        return this;
    }

    @Step("Wait for status to change from PENDING")
    public WebLemburApprovalPage waitForStatusChange() {
        try {
            WaitUtils.waitForInvisible(driver, approvalModal, 10);
        } catch (Exception ignored) {}
        return this;
    }

    @Step("Get status text for employee {employeeName}")
    public String getStatusForEmployee(String employeeName) {
        By statusCell = By.xpath(
            "//tr[.//h6[normalize-space()='" + employeeName + "']]//div[contains(@class,'MuiBox-root') and (normalize-space()='PENDING' or normalize-space()='Approved' or contains(normalize-space(),'Rejected'))]"
        );
        try {
            return getText(statusCell).trim();
        } catch (Exception e) {
            return "";
        }
    }

    @Step("Check if status for employee {employeeName} is Approved")
    public boolean isStatusApproved(String employeeName) {
        String status = getStatusForEmployee(employeeName);
        return status.equalsIgnoreCase("Approved") || status.equalsIgnoreCase("APPROVED");
    }

    @Step("Check if status for employee {employeeName} is Rejected")
    public boolean isStatusRejected(String employeeName) {
        String status = getStatusForEmployee(employeeName);
        return status.toLowerCase().contains("rejected");
    }

    @Step("Check if status for employee {employeeName} is still Pending")
    public boolean isStatusPending(String employeeName) {
        String status = getStatusForEmployee(employeeName);
        return status.equalsIgnoreCase("PENDING");
    }

    @Step("Get employee name from first row")
    public String getFirstRowEmployeeName() {
        By firstRowName = By.cssSelector("tr.MuiTableRow-root:not(.MuiTableRow-head):first-child td:first-child h6");
        try {
            return getText(firstRowName).trim();
        } catch (Exception e) {
            return "";
        }
    }

    @Step("Reload page to refresh status")
    public WebLemburApprovalPage refresh() {
        driver.navigate().refresh();
        WaitUtils.waitForUrlContains(driver, "admin/v1-lembur");
        return this;
    }
}
