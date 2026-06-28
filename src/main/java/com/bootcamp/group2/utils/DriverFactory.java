package com.bootcamp.group2.utils;

import com.bootcamp.group2.enums.Platform;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;

public class DriverFactory {

    private static final Logger log = LoggerFactory.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverFactory() {}

    public static void initDriver() {
        String browser   = ConfigManager.get("browser", "chrome").toLowerCase();
        boolean headless = ConfigManager.getBoolean("headless", false);
        boolean remote   = ConfigManager.getBoolean("remote", false);
        Platform platform = ConfigManager.getPlatform();

        log.info("Initializing WebDriver | browser={} | headless={} | remote={} | platform={}",
            browser, headless, remote, platform);

        WebDriver driver = remote
            ? createRemoteDriver(browser, headless)
            : createLocalDriver(browser, headless);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(
            ConfigManager.getInt("timeout.pageLoad", 30)
        ));

        if (platform == Platform.MOBILE) {
            driver.manage().window().setSize(new org.openqa.selenium.Dimension(390, 844));
        } else {
            driver.manage().window().maximize();
        }

        driverThreadLocal.set(driver);
        log.info("WebDriver successfully initialized for thread: {}", Thread.currentThread().getName());
    }

    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            throw new IllegalStateException(
                "WebDriver not initialized for thread: " + Thread.currentThread().getName()
            );
        }
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            log.info("Closing WebDriver for thread: {}", Thread.currentThread().getName());
            try {
                driver.quit();
            } catch (Exception e) {
                log.warn("Failed to close driver: {}", e.getMessage());
            } finally {
                driverThreadLocal.remove();
            }
        }
    }

    private static WebDriver createLocalDriver(String browser, boolean headless) {
        return switch (browser) {
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions options = new FirefoxOptions();
                if (headless) options.addArguments("--headless");
                yield new FirefoxDriver(options);
            }
            case "edge" -> {
                WebDriverManager.edgedriver().setup();
                EdgeOptions options = new EdgeOptions();
                if (headless) options.addArguments("--headless");
                yield new EdgeDriver(options);
            }
            default -> {
                WebDriverManager.chromedriver().setup();
                yield new ChromeDriver(buildChromeOptions(headless));
            }
        };
    }

    private static WebDriver createRemoteDriver(String browser, boolean headless) {
        String gridUrl = ConfigManager.get("grid.url", "http://localhost:4444/wd/hub");
        log.info("Connecting to Selenium Grid: {}", gridUrl);

        try {
            return switch (browser) {
                case "firefox" -> {
                    FirefoxOptions options = new FirefoxOptions();
                    if (headless) options.addArguments("--headless");
                    yield new RemoteWebDriver(URI.create(gridUrl).toURL(), options);
                }
                case "edge" -> {
                    EdgeOptions options = new EdgeOptions();
                    if (headless) options.addArguments("--headless");
                    yield new RemoteWebDriver(URI.create(gridUrl).toURL(), options);
                }
                default -> new RemoteWebDriver(URI.create(gridUrl).toURL(), buildChromeOptions(headless));
            };
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid Selenium Grid URL: " + gridUrl, e);
        }
    }

    private static ChromeOptions buildChromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        if (headless) options.addArguments("--headless=new");
        options.addArguments(
            "--no-sandbox",
            "--disable-dev-shm-usage",
            "--disable-gpu",
            "--disable-extensions",
            "--window-size=1920,1080",
            "--remote-allow-origins=*"
        );
        return options;
    }
}
