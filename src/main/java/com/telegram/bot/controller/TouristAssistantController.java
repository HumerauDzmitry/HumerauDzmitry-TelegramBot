package com.telegram.bot.controller;

import com.telegram.bot.dto.AttractionDto;
import com.telegram.bot.dto.CityDto;
import com.telegram.bot.entity.Attraction;
import com.telegram.bot.entity.City;
import com.telegram.bot.service.AttractionService;
import com.telegram.bot.service.CityService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Api(tags = "Интерфейс работы с api")
@RestController
@RequestMapping("/tourist-assistant")
@RequiredArgsConstructor
public class TouristAssistantController {

    private final CityService cityService;
    private final AttractionService attractionService;

    @ApiOperation(value = "Найти город по id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Запрос успешно обработан"),
            @ApiResponse(code = 204, message = "Нет содержимого для ответа на запрос"),
            @ApiResponse(code = 403, message = "Операция не позволена"),
            @ApiResponse(code = 404, message = "Сервер не может найти запрашиваемый ресурс"),
    })
    @GetMapping(value = "/find-city-by-id/{id}")
    public ResponseEntity<CityDto> getCityById(@ApiParam(name = "id города") @PathVariable Long id) {
        Optional<CityDto> optionalCity = cityService.getCityById(id);
        return optionalCity.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @ApiOperation(value = "Найти город по названию")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Запрос успешно обработан"),
            @ApiResponse(code = 204, message = "Нет содержимого для ответа на запрос"),
            @ApiResponse(code = 403, message = "Операция не позволена"),
            @ApiResponse(code = 404, message = "Сервер не может найти запрашиваемый ресурс"),
    })
    @GetMapping(value = "/find-city-by-name/{name}")
    public ResponseEntity<CityDto> getCityByName(@ApiParam("Название города") @PathVariable String name) {
        Optional<CityDto> optionalCity = cityService.getCityByName(name);
        return optionalCity.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @ApiOperation(value = "Полная информация о городах и достопримечательностях")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Запрос успешно обработан"),
            @ApiResponse(code = 400, message = "Неверные параметры запроса"),
            @ApiResponse(code = 403, message = "Операция не позволена"),
            @ApiResponse(code = 404, message = "Сервер не может найти запрашиваемый ресурс"),
    })
    @GetMapping(value = "/find-all")
    public Object[] getCityById() {
        List<City> allCity = cityService.getAllCity();
        return allCity.toArray();
    }

    @ApiOperation(value = "Найти достопримечательность по id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Запрос успешно обработан"),
            @ApiResponse(code = 204, message = "Нет содержимого для ответа на запрос"),
            @ApiResponse(code = 403, message = "Операция не позволена"),
            @ApiResponse(code = 404, message = "Сервер не может найти запрашиваемый ресурс"),
    })
    @GetMapping(value = "/find-attraction-by-id/{id}")
    public ResponseEntity<AttractionDto> getAttractionById(@ApiParam(name = "id достопримечательности") @PathVariable Long id) {
        Optional<AttractionDto> optionalAttraction = attractionService.getAttractionById(id);
        return optionalAttraction.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @ApiOperation(value = "Найти достопримечательность по названию")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Запрос успешно обработан"),
            @ApiResponse(code = 204, message = "Нет содержимого для ответа на запрос"),
            @ApiResponse(code = 403, message = "Операция не позволена"),
            @ApiResponse(code = 404, message = "Сервер не может найти запрашиваемый ресурс"),
    })
    @GetMapping(value = "/find-attraction-by-name/{name}")
    public ResponseEntity<AttractionDto> getAttractionByName(@ApiParam("Название достопримечательности")@PathVariable String name) {
        Optional<AttractionDto> optionalAttraction = attractionService.getAttractionByName(name);
        return optionalAttraction.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @ApiOperation(value = "Добавить город")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Запрос успешно обработан"),
            @ApiResponse(code = 204, message = "Запрос успешно обработан"),
            @ApiResponse(code = 400, message = "Неверные параметры запроса"),
            @ApiResponse(code = 403, message = "Операция не позволена"),
            @ApiResponse(code = 404, message = "Сервер не может найти запрашиваемый ресурс"),
    })
    @PutMapping("/add-city/")
    public ResponseEntity<Void> createCite(@ApiParam("Название города")@RequestParam String name) {
        cityService.addCity(name);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Добавить достопримечательность")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Запрос успешно обработан"),
            @ApiResponse(code = 400, message = "Неверные параметры запроса"),
            @ApiResponse(code = 403, message = "Операция не позволена"),
            @ApiResponse(code = 404, message = "Сервер не может найти запрашиваемый ресурс"),
    })
    @PutMapping("/add-attraction/")
    public ResponseEntity<Attraction> addAttractionAroundCity(@ApiParam("Название города") @RequestParam String cityName,
                                                              @ApiParam("Название достопримечательности") @RequestParam String attractionName) {
        attractionService.addAttractionAroundCity(cityName, attractionName);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Удалить достопримечательность")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Запрос успешно обработан"),
            @ApiResponse(code = 400, message = "Неверные параметры запроса"),
            @ApiResponse(code = 403, message = "Операция не позволена"),
            @ApiResponse(code = 404, message = "Сервер не может найти запрашиваемый ресурс"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @DeleteMapping("/delete-attraction/")
    public ResponseEntity<Void> deleteAttractionAroundCity(@ApiParam("Название города") @RequestParam String cityName,
                                                             @ApiParam("Название достопримечательности") @RequestParam String attractionName) {
        attractionService.deleteAttraction(cityName, attractionName);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Удалить город")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Запрос успешно обработан"),
            @ApiResponse(code = 400, message = "Неверные параметры запроса"),
            @ApiResponse(code = 403, message = "Операция не позволена"),
            @ApiResponse(code = 404, message = "Сервер не может найти запрашиваемый ресурс"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @DeleteMapping("/delete-city/")
    public ResponseEntity<Void> deleteCity(@ApiParam("Название города") @RequestParam String cityName) {
        cityService.deleteCity(cityName);
        return ResponseEntity.noContent().build();
    }
}
