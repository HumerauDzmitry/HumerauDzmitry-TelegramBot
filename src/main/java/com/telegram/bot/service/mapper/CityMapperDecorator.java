package com.telegram.bot.service.mapper;

import com.telegram.bot.dto.CityDto;
import com.telegram.bot.entity.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class CityMapperDecorator implements CityMapper {

    private CityMapper delegate;

    @Autowired
    @Qualifier("delegate")
    public void setDelegate(CityMapper delegate) { this.delegate = delegate; }

    @Override
    public City toEntity(CityDto source) { return delegate.toEntity(source); }

    @Override
    public CityDto toDto(City source) {
        CityDto userDto = delegate.toDto(source);
        return userDto;
    }
}
