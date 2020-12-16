package com.telegram.bot.service;

import com.telegram.bot.dto.AttractionDto;

import java.util.Optional;

public interface AttractionService {

    Optional<AttractionDto> getAttractionById(Long id);

    Optional<AttractionDto> getAttractionByName(String name);

    void addAttractionAroundCity(String cityName, String attractionName);

    void deleteAttraction(String cityName, String attractionName);
}
