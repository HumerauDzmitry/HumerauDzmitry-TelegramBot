package com.telegram.bot.bot;

import com.telegram.bot.entity.City;
import com.telegram.bot.repository.CityRepository;
import com.telegram.bot.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class ResponseHandler {

    private final CityRepository cityRepository;
    private final ResponseService responseService;

    private static final Logger log = Logger.getLogger(ResponseHandler.class);

    Random random = new Random();

    Optional<String> getMessagesToSend(Update update) {
        String message = update.getMessage().getText();
        log.debug("message - " + message);
        String response;

        if (!cityRepository.findByName(message).isPresent()) {
            return Optional.of("Извините, мы пока не знаем о вашем городе");
        }
        Optional<City> city = cityRepository.findByName(message);
        int amountAttraction = city.get().getAttractionList().size();

        if (amountAttraction == 0) {
            response = "Мы знаем о городе, но пока не знаем о достопримечательностях";
        } else if(amountAttraction == 1) {
            response = city.get().getAttractionList().get(0).getName();            
        } else {
            StringBuilder stringBuilder = new StringBuilder(responseService.getResponse().get());
            StringBuilder result = new StringBuilder();

            int randomInt1 = random.nextInt(city.get().getAttractionList().size());
            result.append(Pattern.compile("#").matcher(stringBuilder).replaceFirst(city.get().getAttractionList().get(randomInt1).getName()));

            if (result.indexOf("#") != -1) {
                int randomInt2 = randomInt1;
                while (randomInt2 == randomInt1) {
                    randomInt2 = random.nextInt(city.get().getAttractionList().size());
                }
                stringBuilder.setLength(0);
                stringBuilder.append(Pattern.compile("#").matcher(result).replaceFirst(city.get().getAttractionList().get(randomInt2).getName()));
                result = stringBuilder;
            }
            response = result.toString();
        }
        log.debug(response);
        return Optional.of(response);
    }
}