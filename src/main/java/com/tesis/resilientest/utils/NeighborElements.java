package com.tesis.resilientest.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NeighborElements {
    private static final Logger log = LoggerFactory.getLogger(NeighborElements.class);

    private final WebDriver driver;
    private final WebElement left;
    private final WebElement right;
    private final WebElement above;
    private final WebElement below;

    public NeighborElements(WebElement centralElement, WebDriver driver) {
        log.info("Begin constructor NeighborElements");
        this.driver = driver;
        log.info("BEGIN FindNeighbour way 1");
        this.left = findNeighbour(RelativeLocator.with(By.cssSelector("*")).toLeftOf(centralElement));
        log.info("END FindNeighbour way 1");
        this.right = findNeighbour(RelativeLocator.with(By.cssSelector("*")).toRightOf(centralElement));
        this.above = findNeighbour(RelativeLocator.with(By.cssSelector("*")).above(centralElement));
        this.below = findNeighbour(RelativeLocator.with(By.cssSelector("*")).below(centralElement));
        log.info("End of constructor NeighborElements");
    }

    private WebElement findNeighbour(By locator) {
        List<WebElement> elements = this.driver.findElements(locator);
        return elements.isEmpty() ? null: elements.getFirst();
    }

    private WebElement findNeighbour2(By locator, WebElement centralElement) {
        List<WebElement> elements = centralElement.findElements(locator);
        return elements.isEmpty() ? null: elements.getFirst();
    }

    public String getNeighbourTags() {
        StringBuilder tags = new StringBuilder();
        if (left != null) {
            tags.append(left.getTagName()).append(" ");
        }
        if (right != null) {
            tags.append(right.getTagName()).append(" ");
        }
        if (above != null) {
            tags.append(above.getTagName()).append(" ");
        }
        if (below != null) {
            tags.append(below.getTagName());
        }
        return tags.toString();
    }

    public String getNeighbourText() {
        StringBuilder text = new StringBuilder();
        if (left != null && !left.getText().isEmpty()) {
            text.append(left.getText()).append(" ");
        }
        if (right != null && !right.getText().isEmpty()) {
            text.append(right.getText()).append(" ");
        }
        if (above != null && !above.getText().isEmpty()) {
            text.append(above.getText()).append(" ");
        }
        if (below != null && !below.getText().isEmpty()) {
            text.append(below.getText());
        }
        return text.toString();
    }
}
