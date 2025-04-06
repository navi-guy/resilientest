package com.tesis.resilientest.dto.mapper;

import com.tesis.resilientest.database.model.tables.records.WebElementRecord;
import com.tesis.resilientest.dto.WebElementDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WebElementMapper {

    WebElementMapper INSTANCE = Mappers.getMapper(WebElementMapper.class);

    @Mapping(target = "location", expression = "java(new Point(record.getLocationX(), record.getLocationY()))")
    @Mapping(target = "dimension", expression = "java(new Dimension(record.getWidth(), record.getHeight()))")
    WebElementDTO toDto(WebElementRecord record);

    @Mapping(target = "locationX", expression = "java(dto.location().getX())")
    @Mapping(target = "locationY", expression = "java(dto.location().getY())")
    @Mapping(target = "width", expression = "java(dto.dimension().width)")
    @Mapping(target = "height", expression = "java(dto.dimension().height)")
    WebElementRecord toEntity(WebElementDTO dto);
}
