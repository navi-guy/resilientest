package com.tesis.resilientest.utils;

import com.tesis.resilientest.resilient.Locator;
import org.openqa.selenium.chrome.ChromeDriver;

public final class LocatorInfo {

    private LocatorInfo() {
        throw new IllegalStateException("Utility class");
    }

    public static String getObjectDescription(Thread thread) {
        String lastClassName = "";
        String methodName = "";

        StackTraceElement[] stackTraceElements = thread.getStackTrace();
        boolean mainClassFounded = false;
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            String declaringClass = stackTraceElement.getClassName();
            methodName = stackTraceElement.getMethodName();
            if (mainClassFounded) {
                String[] split = declaringClass.split("\\.");
                lastClassName = split[split.length - 1];
                break;
            }
            if (declaringClass.contains("jdk.proxy2")) {
                mainClassFounded = true;
            }
        }
        return lastClassName + "_" + methodName;
    }

    public static String getFullXpath(Locator locator, ChromeDriver driver) {
        String jsScriptGetElement = getJsScriptGetElement(locator);
        return JsExecutor.getFullXpath(jsScriptGetElement, driver);
    }


    public static String getRelativeXpath(Locator locator, ChromeDriver driver) {
        String jsScriptGetElement = getJsScriptGetElement(locator);
        return JsExecutor.getRelativeXpath(jsScriptGetElement, driver);
    }

    private static String getJsScriptGetElement(Locator locator) {
        String jsScriptGetElement = "";
        switch (locator.type()) {
            case ID -> {
                String selectorValue = locator.value();
                jsScriptGetElement = "element = document.querySelector('#" + selectorValue + "');";
            }
            case CSS_SELECTOR -> {
                String selectorValue = locator.value();
                jsScriptGetElement = "element = document.querySelector('" + selectorValue + "');";
            }
            case XPATH -> {
                String selectorValue = locator.value();
                jsScriptGetElement = "element = document.evaluate('" + selectorValue + "', document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;";
            }
            case CLASS_NAME -> {
                String selectorValue = locator.value();
                jsScriptGetElement = "element = document.getElementsByClassName('" + selectorValue + "')[0];";
            }
            case NAME -> {
                String selectorValue = locator.value();
                jsScriptGetElement = "element = document.getElementsByName('" + selectorValue + "')[0];";
            }
            case UNKNOWN -> {
                throw new IllegalArgumentException("Unknown locator type: " + locator.type());
            }
        }
        return jsScriptGetElement;
    }


}
