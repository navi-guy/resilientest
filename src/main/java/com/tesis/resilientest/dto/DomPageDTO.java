package com.tesis.resilientest.dto;

import org.openqa.selenium.Dimension;

public record DomPageDTO(
        Integer id,
        String url, //TODO: Should have a step url (The PK should be url and step)
        String description,
        String content,
        Dimension screenSize,
        Dimension viewportSize,
        String fullPageScreenshotPath
) {
    public static class Builder {
        private Integer id;
        private String url;
        private String description = "";
        private String content;
        private Dimension screenSize;
        private Dimension viewportSize;
        private String fullPageScreenshotPath = "";

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder screenSize(Dimension screenSize) {
            this.screenSize = screenSize;
            return this;
        }

        public Builder viewportSize(Dimension viewportSize) {
            this.viewportSize = viewportSize;
            return this;
        }

        public Builder fullPageScreenshotPath(String fullPageScreenshotPath) {
            this.fullPageScreenshotPath = fullPageScreenshotPath;
            return this;
        }

        public DomPageDTO build() {
            validate();
            return new DomPageDTO(id, url, description, content, screenSize, viewportSize, fullPageScreenshotPath);
        }

        private void validate() {
            if (url == null || url.isBlank()) {
                throw new IllegalArgumentException("URL cannot be null or empty");
            }
            if (content == null || content.isBlank()) {
                throw new IllegalArgumentException("Content cannot be null or empty");
            }
            if (screenSize == null) {
                throw new IllegalArgumentException("Screen size cannot be null");
            }
            if (viewportSize == null) {
                throw new IllegalArgumentException("Viewport size cannot be null");
            }
        }
    }

}
