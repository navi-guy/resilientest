package com.tesis.resilientest.service;

import com.tesis.resilientest.dto.DomPageDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.Dimension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static org.assertj.core.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DomPageServiceTest {
    DomPageService domPageService = new DomPageService();
    private static final String TEST_URL = "https://www.google.com";
    private static final Logger logger = LoggerFactory.getLogger(DomPageServiceTest.class);


    @Order(1)
    @Test
    void testSaveDomPage() {
        DomPageDTO domPage = new DomPageDTO.Builder()
                .id(null)
                .url(TEST_URL)
                .content("<html></html>")
                .description("Google search page")
                .screenSize(new Dimension(1920, 1080))
                .viewportSize(new Dimension(1920, 1080))
                .build();
        DomPageDTO newDomPage = domPageService.saveDomPage(domPage);
        assertThat(newDomPage).isNotNull();
        assertThat(newDomPage.id()).isNotNull();
        assertThat(newDomPage.id()).isPositive();
        assertThat(newDomPage.url()).isEqualTo(TEST_URL);
        assertThat(newDomPage.screenSize().height).isEqualTo(1080);
        assertThat(newDomPage.screenSize().width).isEqualTo(1920);
    }

    @Order(2)
    @Test
    void testUpdateDomPage() {

        DomPageDTO domPageDTO = domPageService.getDomPageByUrl(TEST_URL).orElseThrow();
        DomPageDTO updatedDomPage = new DomPageDTO.Builder()
                .id(domPageDTO.id())
                .url(TEST_URL)
                .content("<html></html>")
                .description("Updated Google search page")
                .screenSize(new Dimension(1920, 1080))
                .viewportSize(new Dimension(1920, 1080))
                .build();

        DomPageDTO newDomPage = domPageService.saveDomPage(updatedDomPage);

        assertThat(newDomPage).isNotNull();
        assertThat(newDomPage.id()).isNotNull();
        assertThat(newDomPage.id()).isPositive();
        assertThat(newDomPage.url()).isEqualTo(TEST_URL);
        assertThat(newDomPage.description()).isEqualTo("Updated Google search page");
    }


    @Order(3)
    @Test
    void testDeleteDomPage() {
        DomPageDTO domPageDTO = domPageService.getDomPageByUrl(TEST_URL).orElseThrow();
        int domPageId = domPageDTO.id();
        domPageService.deleteDomPage(domPageId);
        assertThat(domPageService.getDomPageByUrl(TEST_URL)).isEmpty();
    }
}
