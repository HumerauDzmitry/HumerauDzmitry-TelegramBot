package com.telegram.bot.service.mapper;

import com.telegram.bot.dto.AttractionDto;
import com.telegram.bot.entity.Attraction;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper( componentModel = "spring")
@DecoratedWith(AttractionMapperDecorator.class)
public interface AttractionMapper {

    Attraction toEntity(AttractionDto source);

    AttractionDto toDto(Attraction source);
}
