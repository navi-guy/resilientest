package com.tesis.resilientest.dto;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

public record WebElementDTO(
        Integer id,
        String idAttribute,
        String className,
        String tag,
        String name,
        String href,
        String alt,
        String fullXpath,
        String relativeXpath,
        String innerText,
        Point location,
        Dimension dimension,
        boolean isButton,
        String lastValidTypeSelector,
        String lastValidSelector,
        String elementScreenshotPath,
        String neighborElements,
        String neighborElementsText,
        int domPageId
) {

    public static class Builder {
        private Integer id;
        private String idAttribute = "";
        private String className = "";
        private String tag;
        private String name = "";
        private String href = "";
        private String alt = "";
        private String fullXpath;
        private String relativeXpath;
        private String innerText = "";
        private Point location;
        private Dimension dimension;
        private boolean isButton = false;
        private String lastValidTypeSelector = "";
        private String lastValidSelector = "";
        private String elementScreenshotPath = "";
        private String neighborElements = "";
        private String neighborElementsText = "";
        private int domPageId;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder idAttribute(String idAttribute) {
            this.idAttribute = idAttribute;
            return this;
        }

        public Builder className(String className) {
            this.className = className;
            return this;
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder href(String href) {
            this.href = href;
            return this;
        }

        public Builder alt(String alt) {
            this.alt = alt;
            return this;
        }

        public Builder fullXpath(String fullXpath) {
            this.fullXpath = fullXpath;
            return this;
        }

        public Builder relativeXpath(String relativeXpath) {
            this.relativeXpath = relativeXpath;
            return this;
        }

        public Builder innerText(String innerText) {
            this.innerText = innerText;
            return this;
        }

        public Builder location(Point location) {
            this.location = location;
            return this;
        }

        public Builder dimension(Dimension dimension) {
            this.dimension = dimension;
            return this;
        }

        public Builder isButton(boolean isButton) {
            this.isButton = isButton;
            return this;
        }

        public Builder lastValidTypeSelector(String lastValidTypeSelector) {
            this.lastValidTypeSelector = lastValidTypeSelector;
            return this;
        }

        public Builder lastValidSelector(String lastValidSelector) {
            this.lastValidSelector = lastValidSelector;
            return this;
        }

        public Builder elementScreenshotPath(String elementScreenshotPath) {
            this.elementScreenshotPath = elementScreenshotPath;
            return this;
        }

        public Builder neighborElements(String neighborElements) {
            this.neighborElements = neighborElements;
            return this;
        }

        public Builder neighborElementsText(String neighborElementsText) {
            this.neighborElementsText = neighborElementsText;
            return this;
        }

        public Builder domPageId(int domPageId) {
            this.domPageId = domPageId;
            return this;
        }

        public WebElementDTO build() {
            validate();
            return new WebElementDTO(
                    id, idAttribute, className, tag, name, href, alt, fullXpath, relativeXpath, innerText, location,
                    dimension, isButton, lastValidTypeSelector, lastValidSelector, elementScreenshotPath,
                    neighborElements, neighborElementsText, domPageId
            );
        }

        private void validate() {
            if (tag == null || tag.isBlank()) {
                throw new IllegalArgumentException("tag cannot be null or empty");
            }
            if (fullXpath == null || fullXpath.isBlank()) {
                throw new IllegalArgumentException("fullXpath cannot be null or empty");
            }
            if (relativeXpath == null || relativeXpath.isBlank()) {
                throw new IllegalArgumentException("relativeXpath cannot be null or empty");
            }
            if (location == null) {
                throw new IllegalArgumentException("location cannot be null");
            }
            if (dimension == null) {
                throw new IllegalArgumentException("dimension cannot be null");
            }
            if (domPageId <= 0) {
                throw new IllegalArgumentException("domPageId cannot be less than or equal to 0");
            }
        }
    }
}
