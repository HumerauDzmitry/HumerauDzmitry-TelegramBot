package com.telegram.bot.service.impl;

import com.telegram.bot.dto.AttractionDto;
import com.telegram.bot.entity.Attraction;
import com.telegram.bot.entity.City;
import com.telegram.bot.enums.Status;
import com.telegram.bot.repository.AttractionRepository;
import com.telegram.bot.repository.CityRepository;
import com.telegram.bot.service.AttractionService;
import com.telegram.bot.service.mapper.AttractionMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class AttractionServiceImpl implements AttractionService {

    private static final Logger log = Logger.getLogger(AttractionServiceImpl.class);

    private final AttractionRepository attractionRepository;
    private final CityRepository cityRepository;
    private final AttractionMapper attractionMapper;

    @Override
    public Optional<AttractionDto> getAttractionById(Long id) {
        return attractionRepository.findById(id).map(attractionMapper::toDto);
    }

    @Override
    public Optional<AttractionDto> getAttractionByName(String name) {
        return attractionRepository.findByName(name).map(attractionMapper::toDto);
    }

    @SneakyThrows
    @Override
    public void addAttractionAroundCity(String cityName, String attractionName) {
        Optional<City> city = cityRepository.findByName(cityName);
        Optional<Attraction> attraction = attractionRepository.findByName(attractionName);
        List<Attraction> attractionList = new ArrayList<>();
        if (city.isPresent() && !attraction.isPresent()) {
            if (city.get().getAttractionList().size() != 0) {
                attractionList = city.get().getAttractionList();
            }
            attraction = attractionRepository.findByName(attractionName);
            if (!attraction.isPresent()) {
                attraction = Optional.of(this.createAttraction(attractionName));
                attractionList.add(attraction.get());
                cityRepository.findByName(cityName).get().setAttractionList(attractionList);
            }
        } else {
            throw new NoSuchElementException("no city");
        }
    }

    @Transactional
    @Override
    public void deleteAttraction(String cityName, String attractionName) {
        Optional<Attraction> attraction = attractionRepository.findByName(attractionName);
        try {
            cityRepository.findByName(cityName).get().getAttractionList().remove(attraction.get());
            attractionRepository.delete(attraction.get());
        } catch (NoSuchElementException e) {
            log.error(attractionName + " remove from repository: " + e);
            throw new NoSuchElementException("no city");
        }
    }

    private Attraction createAttraction(String name) {
        final String DEFAULT_USER = "ivan";
        Attraction attraction = new Attraction();
        attraction.setCreatedBy(DEFAULT_USER);
        attraction.setCreated(new Date());
        attraction.setUpdatedBy(DEFAULT_USER);
        attraction.setUpdated(new Date());
        attraction.setStatus(Status.ACTIVE);
        attraction.setName(name);
        AttractionServiceImpl.log.debug("Created new attraction");
        return attraction;
    }
}
