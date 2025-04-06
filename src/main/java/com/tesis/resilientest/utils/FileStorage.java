package com.tesis.resilientest.utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.util.UUID;
import java.io.File;

public class FileStorage {
    private static final String FULL_PAGE_FOLDER = "full-page";
    private static final String ELEMENT_FOLDER = "element";
    private static final String SCREENSHOTS_PATH = "src/main/resources/screenshots/";


    private FileStorage() {
        throw new IllegalStateException("Utility class");
    }

    public static String saveImage(WebElement webElement) {
        File screenshot = webElement.getScreenshotAs(OutputType.FILE);
        return saveImage(screenshot, false);
    }

    public static String saveImage(ChromeDriver driver) {
        File screenshot = driver.getScreenshotAs(OutputType.FILE);
        return saveImage(screenshot, true);
    }

    private static String saveImage(File screenshotFile, boolean isFullPage) {
        String folder = isFullPage ? FULL_PAGE_FOLDER : ELEMENT_FOLDER;
        File file = new File(SCREENSHOTS_PATH + folder);
        if (!file.exists()) {
            throw new RuntimeException("File not found: " + file.getAbsolutePath());
        }
        String fileName = UUID.randomUUID() + ".png";
        File destination = new File(file, fileName); // <-- fixed this line
        try {
            FileUtils.copyFile(screenshotFile, destination);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileName;
    }


}
