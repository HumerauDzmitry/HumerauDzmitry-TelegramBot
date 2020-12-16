package com.telegram.bot.service.mapper;

import com.telegram.bot.dto.AttractionDto;
import com.telegram.bot.entity.Attraction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class AttractionMapperDecorator implements AttractionMapper{

    private AttractionMapper delegate;

    @Autowired
    @Qualifier("delegate")
    public void setDelegate(AttractionMapper delegate) { this.delegate = delegate; }

    @Override
    public Attraction toEntity(AttractionDto source) { return delegate.toEntity(source); }

    @Override
    public AttractionDto toDto(Attraction source) { return delegate.toDto(source); }
}
