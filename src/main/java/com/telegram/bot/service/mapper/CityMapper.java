package com.telegram.bot.service.mapper;

import com.telegram.bot.dto.CityDto;
import com.telegram.bot.entity.City;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper( componentModel = "spring")
@DecoratedWith(CityMapperDecorator.class)
public interface CityMapper {

    City toEntity(CityDto source);

    CityDto toDto(City source);
}
