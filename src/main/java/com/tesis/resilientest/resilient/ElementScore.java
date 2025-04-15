package com.tesis.resilientest.resilient;

import org.openqa.selenium.WebElement;
public record ElementScore(WebElement element, double score) {
    public ElementScore(WebElement element, double score) {
        if (score < 0 || score > 100) {
            throw new IllegalArgumentException("Score must be between 0 and 100");
        }
        this.element = element;
        this.score = score;
    }

    public static ElementScore of(WebElement element, double score) {
        return new ElementScore(element, score);
    }
}
