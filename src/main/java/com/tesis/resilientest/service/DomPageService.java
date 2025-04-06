package com.tesis.resilientest.service;

import static com.tesis.resilientest.database.model.tables.DomPage.DOM_PAGE;

import com.tesis.resilientest.database.DSLContextProvider;
import com.tesis.resilientest.database.model.tables.records.DomPageRecord;
import com.tesis.resilientest.dto.DomPageDTO;
import com.tesis.resilientest.dto.mapper.DomPageMapper;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Optional;

public class DomPageService {
    private final DSLContext context = DSLContextProvider.getInstance();

    public List<DomPageDTO> getAllDomPages() {
        return context.selectFrom(DOM_PAGE)
                .fetchInto(DomPageRecord.class)
                .stream()
                .map(DomPageMapper.INSTANCE::toDto)
                .toList();
    }

    public Optional<DomPageDTO> getDomPageByUrl(String url) {
        return context.selectFrom(DOM_PAGE)
                .where(DOM_PAGE.URL.eq(url))
                .fetchOptionalInto(DomPageRecord.class)
                .map(DomPageMapper.INSTANCE::toDto);
    }

    public DomPageDTO saveDomPage(DomPageDTO domPage) {
        DomPageRecord recordDomPage = context.newRecord(DOM_PAGE, DomPageMapper.INSTANCE.toRecord(domPage));
        if (domPage.id() != null) {
            recordDomPage.update();
        } else {
            recordDomPage.insert();
        }
        return DomPageMapper.INSTANCE.toDto(recordDomPage);
    }


    public void deleteDomPage(int id) {
        context.deleteFrom(DOM_PAGE)
                .where(DOM_PAGE.ID.eq(id))
                .execute();
    }
}
