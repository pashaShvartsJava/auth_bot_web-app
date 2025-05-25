package com.auth_bot.Service;

import com.auth_bot.Config.TelegramBot;
import com.auth_bot.DTO.TelegramUserDTO;
import com.auth_bot.Model.TelegramUser;
import com.auth_bot.Repository.TelegramBotRepository;
import org.springframework.stereotype.Service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class TelegramBotService {

    private final TelegramBotRepository telegramBotRepository;

    private final TelegramBot telegramBot;

    public TelegramBotService(TelegramBotRepository telegramBotRepository, TelegramBot telegramBot) {
        this.telegramBotRepository = telegramBotRepository;
        this.telegramBot = telegramBot;
    }

    public TelegramUser findUserById(Long id) {
        return telegramBotRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public boolean authenticate(String initData) throws NoSuchAlgorithmException, InvalidKeyException {
        return telegramBot.authenticateUser(initData);
    }

    public TelegramUser save(TelegramUserDTO userDTO) {
        return telegramBotRepository.save(wrapToEntity(userDTO));
    }

    private TelegramUser wrapToEntity(TelegramUserDTO userDTO) {
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setName(userDTO.getName());
        telegramUser.setLastname(userDTO.getLastname());
        telegramUser.setUsername(userDTO.getUsername());
        telegramUser.setUser_id(userDTO.getUser_id());
        telegramUser.setAuth_date(userDTO.getAuth_date());
        telegramUser.setHash(userDTO.getHash());
        return telegramUser;
    }
}
