package com.telegram.bot.service.impl;

import com.telegram.bot.repository.ResponseRepository;
import com.telegram.bot.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class ResponseServiceImpl implements ResponseService {

    private static final Logger log = Logger.getLogger(ResponseServiceImpl.class);
    private final ResponseRepository responseRepository;

    @Override
    public Optional<String> getResponse() {
        Random random = new Random();
        int randomInt = random.nextInt(responseRepository.findAll().size());
        log.debug("random = " + randomInt);
        String responseStr = responseRepository.findById((long) randomInt + 10001).get().getText();
        return Optional.of(responseStr);
    }
}
