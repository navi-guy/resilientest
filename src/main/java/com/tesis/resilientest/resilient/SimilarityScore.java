package com.tesis.resilientest.resilient;

import com.tesis.resilientest.dto.WebElementDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SimilarityScore {
    private static final float MAX_SCORE = 100f;
    private static final float MIN_SCORE = 0f;
    private static final float WEIGHT_LOW = 0.5f;
    private static final float WEIGHT_HIGH = 1.5f;

    private final WebElementDTO element1;
    private final WebElementDTO element2;

    public SimilarityScore(WebElementDTO element1, WebElementDTO element2) {
        this.element1 = element1;
        this.element2 = element2;
    }

    public double getSimilarityScore() { // values between 0 and 1
      //  this.element1.idAttribute()
        double score = 0;
        score += compareTextEquals(element1.idAttribute(), element2.idAttribute())*WEIGHT_HIGH;
        score += compareTextDistance(element1.className(), element2.className())*WEIGHT_LOW;
        score += compareTextEquals(element1.tag(), element2.tag())*WEIGHT_HIGH;
        score += compareTextEquals(element1.name(), element2.name())*WEIGHT_HIGH;
        score += compareTextDistance(element1.href(), element2.href())*WEIGHT_LOW;
        score += compareTextDistance(element1.alt(), element2.alt())*WEIGHT_LOW;
        score += compareTextDistance(element1.fullXpath(), element2.fullXpath())*WEIGHT_LOW;
        score += compareTextDistance(element1.relativeXpath(), element2.relativeXpath())*WEIGHT_LOW;
        score += compareTextDistance(element1.innerText(), element2.innerText())*WEIGHT_HIGH;
        score += getDistance(element1.location(), element2.location())*WEIGHT_LOW;
        score += getAreaDistance(element1.dimension(), element2.dimension())*WEIGHT_LOW;
        score += getShapeDistance(element1.dimension(), element2.dimension())*WEIGHT_LOW;
        score += wordTextComparison(element1.neighborElements(), element2.neighborElements())*WEIGHT_HIGH;
        score += ((element1.isButton() && element2.isButton()) ? 1 : 0)*WEIGHT_LOW;
        score = sigmoid(score);
        return Math.clamp(score, MIN_SCORE, MAX_SCORE);
    }


    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    private float compareTextDistance(String text1, String text2) {
        if (text1 == null || text2 == null) {
            return 0;
        }
        int maxLength = Math.max(text1.length(), text2.length());
        int distance = LevenshteinDistance.getDefaultInstance().apply(text1, text2);
        return (float) (maxLength - distance) / maxLength * MAX_SCORE;
    }

    // similarity = (number of shared words) / (total unique words in both texts)
    // this is a simple implementation, based on Jaccard similarity
    private float wordTextComparison(String text1, String text2) {
        if (text1 == null || text2 == null) {
            return 0f;
        }
        // Split texts into lowercase word sets
        Set<String> words1 = new HashSet<>(Arrays.asList(StringUtils.split(text1.toLowerCase())));
        Set<String> words2 = new HashSet<>(Arrays.asList(StringUtils.split(text2.toLowerCase())));

        if (words1.isEmpty() && words2.isEmpty()) {
            return MAX_SCORE; // Both are empty or contain only whitespace → consider them identical
        }

        // Calculate Jaccard similarity
        Set<String> intersection = new HashSet<>(words1);
        intersection.retainAll(words2);

        Set<String> union = new HashSet<>(words1);
        union.addAll(words2);

        float similarity = (float) intersection.size() / union.size();

        return similarity * MAX_SCORE;
    }


    private int compareTextEquals(String text1, String text2) {
        if (text1 == null && text2 == null) {
            return 1;
        }
        if(text1 == null || text2 == null) {
            return 0;
        }
        if(text1.equalsIgnoreCase(text2)) {
            return 1;
        }
        return 0;
    }

    double getDistance(Point element1, Point element2) {
        double x1 = element1.x;
        double y1 = element1.y;
        double x2 = element2.x;
        double y2 = element2.y;
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    double getAreaDistance(Dimension element1, Dimension element2) {
        double area1 = (double) element1.getWidth() * element1.getHeight();
        double area2 = (double) element2.getWidth() * element2.getHeight();
        return Math.sqrt(Math.pow(area2 - area1, 2));
    }

    double getShapeDistance(Dimension element1, Dimension element2) {
        double shape1 = (double) element1.getWidth() / element1.getHeight();
        double shape2 = (double) element2.getWidth() / element2.getHeight();
        return Math.sqrt(Math.pow(shape2 - shape1, 2));
    }
}
