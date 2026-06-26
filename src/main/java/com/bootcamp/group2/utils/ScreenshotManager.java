package com.bootcamp.group2.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ScreenshotManager {

    private static final Logger log = LoggerFactory.getLogger(ScreenshotManager.class);
    private static final String BASE_DIR = "reports/screenshots";
    private static final int MAX_ROOT_FOLDERS = 3;
    private static final DateTimeFormatter RUN_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    private static final Path runFolder;
    private static final ThreadLocal<AtomicInteger> stepCounter = ThreadLocal.withInitial(AtomicInteger::new);

    static {
        String timestamp = LocalDateTime.now().format(RUN_FORMATTER);
        runFolder = Paths.get(BASE_DIR, timestamp);
        try {
            Files.createDirectories(runFolder);
            log.info("Screenshot run folder: {}", runFolder.toAbsolutePath());
            rotateOldFolders();
        } catch (IOException e) {
            log.error("Failed to initialize screenshot folder: {}", e.getMessage());
        }
    }

    private ScreenshotManager() {}

    public static void resetStepCounter() {
        stepCounter.get().set(0);
    }

    public static void saveStepScreenshot(WebDriver driver, String featureName, String scenarioName, String stepText) {
        if (driver == null) return;
        try {
            int stepNum = stepCounter.get().incrementAndGet();
            Path dir = runFolder
                .resolve(sanitize(featureName, 60))
                .resolve(sanitize(scenarioName, 100));
            Files.createDirectories(dir);

            String fileName = String.format("%02d_%s.png", stepNum, sanitize(stepText, 80));
            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Files.write(dir.resolve(fileName), bytes);
            log.debug("Screenshot: {}/{}/{}", featureName, scenarioName, fileName);
        } catch (Exception e) {
            log.warn("Failed to save step screenshot: {}", e.getMessage());
        }
    }

    private static void rotateOldFolders() throws IOException {
        Path base = Paths.get(BASE_DIR);
        List<Path> folders;
        try (var stream = Files.list(base)) {
            folders = stream
                .filter(Files::isDirectory)
                .sorted(Comparator.comparing(p -> p.getFileName().toString()))
                .collect(Collectors.toList());
        }

        while (folders.size() > MAX_ROOT_FOLDERS) {
            Path oldest = folders.remove(0);
            deleteDirectory(oldest);
            log.info("Deleted old screenshot folder: {}", oldest.getFileName());
        }
    }

    private static void deleteDirectory(Path dir) throws IOException {
        Files.walkFileTree(dir, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path d, IOException exc) throws IOException {
                Files.delete(d);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static String sanitize(String name, int maxLength) {
        String result = name
            .toLowerCase()
            .replaceAll("[^a-z0-9]+", "_")
            .replaceAll("^_+|_+$", "");
        if (result.length() > maxLength) {
            result = result.substring(0, maxLength).replaceAll("_+$", "");
        }
        return result.isEmpty() ? "unknown" : result;
    }
}
