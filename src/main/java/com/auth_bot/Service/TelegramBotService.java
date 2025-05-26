package com.auth_bot.Service;

import com.auth_bot.Config.TelegramBot;
import com.auth_bot.DTO.TelegramUserDTO;
import com.auth_bot.Model.TelegramUser;
import com.auth_bot.Repository.TelegramBotRepository;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

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

    public boolean authenticate(String initData){
        return telegramBot.authenticateUser(initData);
    }

    public TelegramUser save(TelegramUserDTO userDTO) {
        return telegramBotRepository.save(wrapToEntity(userDTO));
    }

    public TelegramUser wrapToEntity(TelegramUserDTO userDTO) {
        TelegramUser user = new TelegramUser();
        String initData = userDTO.getInitData();
        String[] params = initData.split("&");
        for (String param : params) {
            String[] keyValue = param.split("=", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                switch (key) {
                    case "id": user.setUser_id(Long.valueOf(value)); break;
                    case "first_name": user.setName(value); break;
                    case "last_name": user.setLastname(value); break;
                    case "username": user.setUsername(value); break;
                    case "auth_date": user.setAuth_date(value); break;
                    case "hash": user.setHash(value); break;
                }
            }
        }
        return user;
    }

}
