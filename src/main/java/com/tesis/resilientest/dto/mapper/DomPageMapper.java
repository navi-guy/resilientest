package com.tesis.resilientest.dto.mapper;

import com.tesis.resilientest.database.model.tables.records.DomPageRecord;
import com.tesis.resilientest.dto.DomPageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DomPageMapper {
    DomPageMapper INSTANCE = Mappers.getMapper(DomPageMapper.class);

    @Mapping(target = "screenSize", expression = "java(new Dimension(domPageRecord.getScreenWidth(), domPageRecord.getScreenHeight()))")
    @Mapping(target = "viewportSize", expression = "java(new Dimension(domPageRecord.getViewportWidth(), domPageRecord.getViewportHeight()))")
    DomPageDTO toDto(DomPageRecord domPageRecord);

    @Mapping(target = "screenHeight", expression = "java(dto.screenSize().height)")
    @Mapping(target = "screenWidth", expression = "java(dto.screenSize().width)")
    @Mapping(target = "viewportHeight", expression = "java(dto.viewportSize().height)")
    @Mapping(target = "viewportWidth", expression = "java(dto.viewportSize().width)")
    DomPageRecord toRecord(DomPageDTO dto);
}
