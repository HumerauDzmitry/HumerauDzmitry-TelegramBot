package com.telegram.bot.service;

import com.telegram.bot.dto.CityDto;
import com.telegram.bot.entity.City;

import java.util.List;
import java.util.Optional;

public interface CityService {

    List<City> getAllCity();

    Optional<CityDto> getCityById(Long id);

    Optional<CityDto> getCityByName(String name);

    void addCity(String name);

    void deleteCity(String name);
}
