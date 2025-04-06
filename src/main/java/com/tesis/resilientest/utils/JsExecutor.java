package com.tesis.resilientest.utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class JsExecutor {
    private static final String UTILS_FILE_PATH = "src/main/resources/js/utils.js";
    private JsExecutor() {
        throw new IllegalStateException("Utility class");
    }

    public static String getRelativeXpath(String getElementScript, ChromeDriver driver) {
        String returnPart = "return getRelativeXPath(element);";
        String jsScript = getElementScript;
        jsScript += fileUtilsString();
        jsScript += returnPart;
        return (String) driver.executeScript(jsScript);
    }

    public static String getFullXpath(String getElementScript, ChromeDriver driver) {
        String returnPart = "return getFullXPath(element);";
        String jsScript = getElementScript;
        jsScript += fileUtilsString();
        jsScript += returnPart;
        return (String) driver.executeScript(jsScript);
    }

    private static String fileUtilsString() {
        File file = new File(UTILS_FILE_PATH);
        if (!file.exists()) {
            throw new RuntimeException("File not found: " + file.getAbsolutePath());
        }
        String jsScript = "";
        try {
            jsScript += FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return jsScript;
    }


}
