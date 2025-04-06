package com.tesis.resilientest.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class NeighborElements {
    private WebElement centralElement;
    private WebElement left;
    private WebElement right;
    private WebElement above;
    private WebElement below;

    public NeighborElements(WebElement centralElement) {
        this.centralElement = centralElement;
        this.left = findNeighbor(By.xpath("preceding-sibling::*[1]"));
        this.right = findNeighbor(By.xpath("following-sibling::*[1]"));
        this.above = findNeighbor(By.xpath(".."));
        this.below = findNeighbor(By.xpath("./*"));
    }

    private WebElement findNeighbor(By locator) {
        try {
            return centralElement.findElement(locator);
        } catch (Exception e) {
            return null;
        }
    }

    public String getNeighborTexts() {
        String texts = "";
        if (left != null) {
            texts += left.getText() + " ";
        }
        if (right != null) {
            texts += right.getText() + " ";
        }
        if (above != null) {
            texts += above.getText() + " ";
        }
        if (below != null) {
            texts += below.getText();
        }
        return texts.trim();
    }

    public String getNeighborTags() {
        String tags = "";
        if (left != null) {
            tags += left.getTagName() + " ";
        }
        if (right != null) {
            tags += right.getTagName() + " ";
        }
        if (above != null) {
           tags += above.getTagName() + " ";
        }
        if (below != null) {
            tags += below.getTagName() ;
        }
        return tags.trim();
    }
}
