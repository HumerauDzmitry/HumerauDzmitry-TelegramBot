package com.telegram.bot.service.impl;

import com.telegram.bot.dto.CityDto;
import com.telegram.bot.entity.City;
import com.telegram.bot.enums.Status;
import com.telegram.bot.exception.*;
import com.telegram.bot.repository.CityRepository;
import com.telegram.bot.service.CityService;
import com.telegram.bot.service.mapper.CityMapper;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CityServiceImpl implements CityService {

    private static final Logger log = Logger.getLogger(CityServiceImpl.class);

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    @Override
    public List<City> getAllCity() {
        return cityRepository.findAll();
    }

    public Optional<CityDto> getCityById(Long id) {
        return cityRepository.findById(id).map(cityMapper::toDto);
    }

    @Override
    public Optional<CityDto> getCityByName(String name) {
        return cityRepository.findByName(name).map(cityMapper::toDto);
    }

    @Override
    public void addCity(String name) {

        Optional<City> city = cityRepository.findByName(name);
        if(!city.isPresent()) {
            city = Optional.of(this.createCity(name));
            cityRepository.save(city.get());
            log.debug("City " + name + " added to repository");
        } else {
            throw new NoSuchElementException("no city");
        }
    }

    @Override
    public void deleteCity(String name){
        System.out.println();
        try {
            cityRepository.delete(cityRepository.findByName(name).get());
        } catch (NoSuchElementException e) {
            log.error(name + " remove from repository: " + e);
            throw new NoSuchElementException(e);
        }
    }

    private City createCity(String name) {
        final String DEFAULT_USER = "ivan";
        City city = new City();
        city.setCreatedBy(DEFAULT_USER);
        city.setCreated(new Date());
        city.setUpdatedBy(DEFAULT_USER);
        city.setUpdated(new Date());
        city.setStatus(Status.ACTIVE);
        city.setName(name);
        log.debug("Created new City");
        return city;
    }
}
