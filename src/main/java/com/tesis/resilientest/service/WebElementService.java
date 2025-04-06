package com.tesis.resilientest.service;

import static com.tesis.resilientest.database.model.tables.WebElement.WEB_ELEMENT;

import com.tesis.resilientest.database.DSLContextProvider;
import com.tesis.resilientest.database.model.tables.records.WebElementRecord;
import com.tesis.resilientest.dto.WebElementDTO;
import com.tesis.resilientest.dto.mapper.WebElementMapper;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Optional;

public class WebElementService {
    private final DSLContext context = DSLContextProvider.getInstance();

    public List<WebElementDTO> getElementsByDomPageId(int domPageId) {
        return context.selectFrom(WEB_ELEMENT)
                .where(WEB_ELEMENT.DOM_PAGE_ID.eq(domPageId))
                .fetchInto(WebElementRecord.class)
                .stream()
                .map(WebElementMapper.INSTANCE::toDto)
                .toList();
    }

    public List<WebElementDTO> getAllElements() {
        return context.selectFrom(WEB_ELEMENT)
                .fetchInto(WebElementRecord.class)
                .stream()
                .map(WebElementMapper.INSTANCE::toDto)
                .toList();
    }

    public Optional<WebElementDTO> getElementByLastValidLocator(String selectorType, String selector) {
        return context.selectFrom(WEB_ELEMENT)
                .where(WEB_ELEMENT.LAST_VALID_TYPE_SELECTOR.eq(selectorType))
                .and(WEB_ELEMENT.LAST_VALID_SELECTOR.eq(selector))
                .fetchOptionalInto(WebElementRecord.class)
                .map(WebElementMapper.INSTANCE::toDto);
    }

    public WebElementRecord saveElement(WebElementDTO webElement) {
        WebElementRecord webElementRecord = context.newRecord(WEB_ELEMENT, WebElementMapper.INSTANCE.toEntity(webElement));
        webElementRecord.changed(WEB_ELEMENT.AREA, false);//auto-generated
        webElementRecord.changed(WEB_ELEMENT.SHAPE, false);
        webElementRecord.store();
        return webElementRecord;
    }
}
