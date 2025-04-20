package com.tesis.resilientest.resilient;

import com.tesis.resilientest.dto.DomPageDTO;
import com.tesis.resilientest.dto.WebElementDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SimilarityScore {
    private static final float WEIGHT_LOW = 0.5f;
    private static final float WEIGHT_HIGH = 1.5f;

    private final WebElementDTO element1;
    private final WebElementDTO element2;
    private final DomPageDTO domPageDTO;

    public SimilarityScore(WebElementDTO element1, WebElementDTO element2, DomPageDTO domPageDTO) {
        this.element1 = element1;
        this.element2 = element2;
        this.domPageDTO = domPageDTO;
    }

    public double getSimilarityScore() { // values between 0 and 1
      //  this.element1.idAttribute()
        double score = 0;
        score += compareTextEquals(element1.idAttribute(), element2.idAttribute())*WEIGHT_HIGH;
        score += compareTextDistance(element1.className(), element2.className())*WEIGHT_LOW;// change to compare words
        score += compareTextEquals(element1.tag(), element2.tag())*WEIGHT_HIGH;
        score += compareTextEquals(element1.name(), element2.name())*WEIGHT_HIGH;
        score += compareTextDistance(element1.href(), element2.href())*WEIGHT_LOW;
        score += compareTextDistance(element1.alt(), element2.alt())*WEIGHT_LOW;
        score += compareTextDistance(element1.fullXpath(), element2.fullXpath())*WEIGHT_LOW;
        score += compareTextDistance(element1.relativeXpath(), element2.relativeXpath())*WEIGHT_LOW;
        score += compareTextDistance(element1.innerText(), element2.innerText())*WEIGHT_HIGH;
        score += getDistance(element1.location(), element2.location())*WEIGHT_LOW;// THIS DONT MAKE SENSE
        score += getAreaDistance(element1.dimension(), element2.dimension())*WEIGHT_LOW;
        score += getShapeDistance(element1.dimension(), element2.dimension())*WEIGHT_LOW;
        score += wordTextComparison(element1.neighborElements(), element2.neighborElements())*WEIGHT_HIGH;
        score += ((element1.isButton() && element2.isButton()) ? 1 : 0)*WEIGHT_LOW;
        return score;
    }


    private float compareTextDistance(String text1, String text2) {
        if (text1 == null || text2 == null) {
            return 0;
        }
        int maxLength = Math.max(text1.length(), text2.length());
        // If both strings are empty, they are 100% similar
        if (maxLength == 0) {
            return 1.0f;
        }
        int distance = LevenshteinDistance.getDefaultInstance().apply(text1, text2);
        return 1.0f - (float) distance / maxLength;
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
            return 1.0f;
        }
        // Calculate Jaccard similarity
        Set<String> intersection = new HashSet<>(words1);
        intersection.retainAll(words2);

        Set<String> union = new HashSet<>(words1);
        union.addAll(words2);
        // Return Jaccard similarity between 0 and 1
        return  (float) intersection.size() / union.size();
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

    /**
     *  Returns a value between 0 and 1, where 0 means the elements are far apart and 1 means they are close.
     * @param element1 past version element Point
     * @param element2 element of current version to compare
     * @return a value between 0 and 1
     */
    double getDistance(Point element1, Point element2) {
        double maxDistance = Math.sqrt(Math.pow(domPageDTO.viewportSize().width, 2) + Math.pow(domPageDTO.viewportSize().height, 2));
        double x1 = element1.x;
        double y1 = element1.y;
        double x2 = element2.x;
        double y2 = element2.y;
        // Calculate the Euclidean distance
        double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));

        // Normalize the distance based on maxDistance
        if (maxDistance == 0) {
            return 0.0;  // If maxDistance is 0, return 0 to avoid division by zero
        }

        // Normalize the distance to a value between 0 and 1
        return 1.0 - Math.min(distance / maxDistance, 1.0);
    }

    double getAreaDistance(Dimension element1, Dimension element2) {
        double maxArea = Math.sqrt(Math.pow(domPageDTO.viewportSize().width, 2) * Math.pow(domPageDTO.viewportSize().height, 2));
        double area1 = (double) element1.getWidth() * element1.getHeight();
        double area2 = (double) element2.getWidth() * element2.getHeight();
        // Calculate the absolute difference in areas
        double areaDifference = Math.abs(area2 - area1);

        // Normalize the area difference based on maxArea
        if (maxArea == 0) {
            return 0.0; // If maxArea is 0, return 0 to avoid division by zero
        }

        // Normalize the difference to a value between 0 and 1
        return 1.0 - Math.min(areaDifference / maxArea, 1.0);
    }

    double getShapeDistance(Dimension element1, Dimension element2) {
        double maxShape = calculateMaxShape();

        double shape1 = calculateShape(element1);
        double shape2 = calculateShape(element2);

        if (maxShape == 0) {
            return 0.0;  // Avoid division by zero for maxShape
        }

        // Return a normalized shape difference between 0 and 1
        return 1.0 - Math.min(Math.abs(shape2 - shape1) / maxShape, 1.0);
    }

    // Helper method to calculate the shape ratio (width/height)
    private double calculateShape(Dimension element) {
        if (element.getHeight() == 0) {
            return 0.0;  // Avoid division by zero for element height
        }
        return (double) element.getWidth() / element.getHeight();
    }

    // Helper method to calculate maxShape
    private double calculateMaxShape() {
        double viewportWidth = domPageDTO.viewportSize().width;
        double viewportHeight = domPageDTO.viewportSize().height;
        if (viewportHeight == 0) return 0.0;  // Avoid division by zero for viewport height
        return Math.sqrt(Math.pow(viewportWidth, 2) / Math.pow(viewportHeight, 2));
    }

}
