package com.auth_bot.Service;

import com.auth_bot.Model.TelegramUser;
import com.auth_bot.Repository.TelegramBotRepository;
import org.springframework.stereotype.Service;

@Service
public class TelegramBotService {

    private final TelegramBotRepository telegramBotRepository;

    public TelegramBotService(TelegramBotRepository telegramBotRepository) {
        this.telegramBotRepository = telegramBotRepository;
    }

    public TelegramUser findUserById(Long id) {
        return telegramBotRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void authenticate(Long id) {
        TelegramUser telegramUser = findUserById(id);

    }
}
