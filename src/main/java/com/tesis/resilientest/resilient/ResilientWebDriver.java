package com.tesis.resilientest.resilient;

import com.tesis.resilientest.dto.DomPageDTO;
import com.tesis.resilientest.dto.WebElementDTO;
import com.tesis.resilientest.service.DomPageService;
import com.tesis.resilientest.service.WebElementService;
import com.tesis.resilientest.utils.FileStorage;
import com.tesis.resilientest.utils.LocatorInfo;
import com.tesis.resilientest.utils.NeighborElements;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.*;

public class ResilientWebDriver extends ChromeDriver {

    DomPageService domPageService = new DomPageService();
    WebElementService webElementService = new WebElementService();

    private boolean applyResilienceLogic = true;

    public ResilientWebDriver() {
        super();
    }

    public void setApplyResilienceLogic(boolean applyResilienceLogic) {
        this.applyResilienceLogic = applyResilienceLogic;
    }

    @Override
    public WebElement findElement(By locator) {
        if (!applyResilienceLogic) {
            return super.findElement(locator);
        }
        System.out.println("Finding element: " + locator);

        DomPageDTO domPage = saveDomPageIfNotExists();
        boolean isElementPresent = (!super.findElements(locator).isEmpty());
        if (isElementPresent) {
            WebElement webElement = super.findElement(locator);
            saveWebElementInfoIfNotExists(webElement, locator, domPage);
            return webElement;
        } else {
            System.out.println("Element not found, applying resilience logic.");
            // Attempt to repair the locator
            try {
                By repairedLocator = locatorRepaired(locator);
                WebElement repairedElement = super.findElement(repairedLocator);
                saveWebElementInfoIfNotExists(repairedElement, repairedLocator, domPage);
                return repairedElement;
            } catch (NoSuchElementException e) {
                System.out.println("Repaired element not found.");
                throw new NoSuchElementException("Element not found after repair attempt", e);
            } catch (NoReparableElement e) {
                System.out.println("No reparable element found.");
                throw new NoReparableElement("Element cannot be repaired");
            }
        }

    }

    private Locator getLocatorFromBy(By locator) {
        String locatorString = locator.toString();
        String[] parts = locatorString.split(": ", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid locator string: " + locatorString);
        }
        String selectorType = parts[0].replace("By.", "");  // Extracts the type (id, xpath, cssSelector, etc.)
        String selectorValue = parts[1]; // Extracts the value

        return switch (selectorType) {
            case "id" -> new Locator(SELECTOR_TYPE.ID, selectorValue);
            case "xpath" -> new Locator(SELECTOR_TYPE.XPATH, selectorValue);
            case "cssSelector" -> new Locator(SELECTOR_TYPE.CSS_SELECTOR, selectorValue);
            case "name" -> new Locator(SELECTOR_TYPE.NAME, selectorValue);
            case "className" -> new Locator(SELECTOR_TYPE.CLASS_NAME, selectorValue);
            default -> throw new IllegalArgumentException("Invalid locator type: " + selectorType);
        };
    }

    //Save web element info into DB
    private void saveWebElementInfoIfNotExists(WebElement webElement, By locatorBy, DomPageDTO domPageDTO) {
        this.setApplyResilienceLogic(false);
        NeighborElements neighborElements = new NeighborElements(webElement);
        Locator locator = getLocatorFromBy(locatorBy);
        String selectorType = locator.type().name();
        String selectorValue = locator.value();
        String fullXPath = LocatorInfo.getFullXpath(locator, this);
        String relativeXpath = LocatorInfo.getRelativeXpath(locator, this);
        Optional<WebElementDTO> webElementResult = webElementService.getElementByLastValidLocator(selectorType, selectorValue);
        if (webElementResult.isEmpty()) {
            System.out.println("Saving web element info: " + selectorType + " - " + selectorValue);
            WebElementDTO webElementDTO = new WebElementDTO.Builder()
                    .idAttribute(webElement.getAttribute("id"))
                    .className(webElement.getAttribute("class"))
                    .tag(webElement.getTagName())
                    .name(webElement.getAttribute("name"))
                    .href(webElement.getAttribute("href"))
                    .alt(webElement.getAttribute("alt"))
                    .fullXpath(fullXPath)
                    .relativeXpath(relativeXpath)
                    .innerText(webElement.getText())
                    .location(webElement.getLocation())
                    .dimension(webElement.getSize())
                    .isButton(webElement.getTagName().equals("button")) // add more logic to determine if it is a button
                    .lastValidTypeSelector(selectorType)
                    .lastValidSelector(selectorValue)
                    .elementScreenshotPath(FileStorage.saveImage(webElement))
                    .neighborElements(neighborElements.getNeighborTags()) // PENDING TESTING
                    .neighborElementsText(neighborElements.getNeighborTexts()) // PENDING TESTING
                    .domPageId(domPageDTO.id())
                    .build();
            webElementService.saveElement(webElementDTO);
            System.out.println("Web element info saved: " + selectorType + " - " + selectorValue);
            this.setApplyResilienceLogic(true);
        }
    }


    private DomPageDTO saveDomPageIfNotExists() {
        String currentUrl = this.getCurrentUrl();
        Optional<DomPageDTO> domPageResult = domPageService.getDomPageByUrl(currentUrl);
        if (domPageResult.isEmpty()) {
            DomPageDTO domPage = new DomPageDTO.Builder()
                    .url(currentUrl)
                    .content(this.getPageSource())
                    .description(this.getTitle())
                    .screenSize(this.manage().window().getSize())
                    .viewportSize(getViewportSize())
                    .fullPageScreenshotPath(FileStorage.saveImage(this))
                    .build();
            return domPageService.saveDomPage(domPage);
        } else {
            return domPageResult.get();
        }
    }

    private Dimension getViewportSize() {
        Map<String, Number> viewPort = (Map<String, Number>) this.executeScript(
                "return {width: window.innerWidth, height: window.innerHeight};"
        );
        assert viewPort != null;
        int width = (viewPort.get("width")).intValue();
        int height = (viewPort.get("height")).intValue();
        return new Dimension(width, height);
    }

    private By locatorRepaired(By brokenLocator) throws NoReparableElement {

        Locator locator = getLocatorFromBy(brokenLocator);
        WebElementDTO currentElement = webElementService.getElementByLastValidLocator(locator.type().name(), locator.value())
                .orElseThrow(() -> new NoReparableElement("Element cannot be repaired, because it never was tested before"));


        WebElement domTree = this.findElement(By.xpath("//html/body"));
        List<WebElement> allElements = domTree.findElements(By.xpath("//*")); // This gets all elements in the body page
        List<ElementScore> scoredElements = new ArrayList<>();

        // Iterate through all elements and apply the similarity algorithm
        for (WebElement element : allElements) {
            NeighborElements neighborElements = new NeighborElements(element);
            WebElementDTO elementToCompare = new WebElementDTO.Builder()
                    .idAttribute(element.getAttribute("id"))
                    .className(element.getAttribute("class"))
                    .tag(element.getTagName())
                    .name(element.getAttribute("name"))
                    .href(element.getAttribute("href"))
                    .alt(element.getAttribute("alt"))
                    .fullXpath(LocatorInfo.getFullXpath(getLocatorFromBy(brokenLocator), this))
                    .relativeXpath(LocatorInfo.getRelativeXpath(getLocatorFromBy(brokenLocator), this))
                    .innerText(element.getText())
                    .location(element.getLocation())
                    .dimension(element.getSize())
                    .isButton(element.getTagName().equals("button"))
                    .neighborElements(neighborElements.getNeighborTags())
                    .neighborElementsText(neighborElements.getNeighborTexts())
                    .build();
            // Calculate the similarity score between the broken element and each element
            int score = calculateSimilarityScore(currentElement, elementToCompare);
            scoredElements.add(ElementScore.of(element, score));
        }
        // Sort the scored elements by the similarity score in descending order
        scoredElements.sort((e1, e2) -> Integer.compare(e2.score(), e1.score()));

        // Return the most similar element's locator
        WebElement mostSimilarElement = scoredElements.get(0).element();

        By locatorx = By.xpath("//button");
        try {
            findElement(locatorx);
        } catch (NoSuchElementException e) {
            throw new NoReparableElement("Element cannot be repaired");
        }

        return locatorx;
    }

    private int calculateSimilarityScore(WebElementDTO element1, WebElementDTO element2) {
        int attributeIdScore = (element1.idAttribute() != null && element1.idAttribute().equals(element2.idAttribute())) ? 1 : 0;
        // TODO: Add more attribute comparisons as needed
        return 1;
    }
}
