package com.bootcamp.group2.pages.web.laporan;

import com.bootcamp.group2.pages.BasePage;
import com.bootcamp.group2.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page object for the Lembur report page: /laporan/overtime
 *
 * <p>Accessed via Sidebar → Laporan → Lembur. Lists all lembur submissions with
 * columns: NAMA KARYAWAN, UNIT, TANGGAL PENGAJUAN, TANGGAL ABSENSI, TANGGAL LEMBUR,
 * UPLINER, UPLINER 2, UPLINER 3, NOTES, RETURN NOTES, STATUS, OT REASON, OT ATTACHMENT, AKSI</p>
 */
public class WebLaporanLemburPage extends BasePage {

    private static final int STATUS_COLUMN_INDEX = 11; // 0-based

    private final By tableRows   = By.cssSelector("tbody tr");
    private final By pageContent = By.cssSelector("tbody");

    @Step("Check if Laporan Lembur page is loaded")
    public boolean isPageLoaded() {
        WaitUtils.waitForUrlContains(driver, "laporan/overtime");
        WaitUtils.waitForVisible(driver, pageContent);
        return getCurrentUrl().contains("laporan/overtime");
    }

    /**
     * Returns true if the table contains a row for the given employee whose STATUS
     * column equals the expected status text (case-insensitive contains match).
     */
    @Step("Check if lembur submission exists for {employeeName} with status {expectedStatus}")
    public boolean isSubmissionPresentWithStatus(String employeeName, String expectedStatus) {
        try {
            WaitUtils.waitForVisible(driver, tableRows);
            List<WebElement> rows = driver.findElements(tableRows);
            return rows.stream().anyMatch(row -> {
                String text = row.getText();
                return text.contains(employeeName) && text.contains(expectedStatus);
            });
        } catch (Exception e) {
            log.warn("Could not find lembur submission for '{}' with status '{}'", employeeName, expectedStatus);
            return false;
        }
    }

    /**
     * Returns the status value from the STATUS column of the first row matching the employee.
     * Useful for logging in assertions.
     */
    @Step("Get first lembur status for {employeeName}")
    public String getFirstSubmissionStatus(String employeeName) {
        try {
            WaitUtils.waitForVisible(driver, tableRows);
            List<WebElement> rows = driver.findElements(tableRows);
            return rows.stream()
                .filter(r -> r.getText().contains(employeeName))
                .findFirst()
                .map(r -> {
                    List<WebElement> cells = r.findElements(By.tagName("td"));
                    if (cells.size() > STATUS_COLUMN_INDEX) {
                        return cells.get(STATUS_COLUMN_INDEX).getText().trim();
                    }
                    return r.getText();
                })
                .orElse("");
        } catch (Exception e) {
            log.warn("Could not get lembur status for '{}'", employeeName);
            return "";
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
