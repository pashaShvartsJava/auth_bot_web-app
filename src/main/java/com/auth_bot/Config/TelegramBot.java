package com.auth_bot.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegrambots.bots.username}")
    private String BOT_USERNAME;

    @Value("${telegrambots.bots.token}")
    private String BOT_TOKEN;

    private String hash;

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            String text = update.getMessage().getText();
            if (text.contains("/start")) {
                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText("Нажми кнопку ниже, чтобы открыть приложение:");

                KeyboardButton webAppButton = new KeyboardButton("Открыть WebApp");
                webAppButton.setWebApp(new WebAppInfo("http://localhost:8080/bot/webapp"));

                KeyboardRow row = new KeyboardRow();
                row.add(webAppButton);

                ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
                keyboard.setKeyboard(List.of(row));
                keyboard.setResizeKeyboard(true);
                keyboard.setOneTimeKeyboard(true);

                message.setReplyMarkup(keyboard);

                executeMessage(message);
            }
        }
    }

    public  boolean authenticateUser(String initData) {
        try {
            Map<String, String> params = parseInitData(initData);
            String receivedHash = params.remove("hash");

            String dataCheckString = params.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .collect(Collectors.joining("\n"));

            byte[] secretKey = MessageDigest.getInstance("SHA-256")
                    .digest(BOT_TOKEN.getBytes(StandardCharsets.UTF_8));

            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secretKey, "HmacSHA256"));
            byte[] hmac = mac.doFinal(dataCheckString.getBytes(StandardCharsets.UTF_8));

            String calculatedHash = bytesToHex(hmac);

            return calculatedHash.equalsIgnoreCase(receivedHash);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private  Map<String, String> parseInitData(String initData) {
        Map<String, String> result = new HashMap<>();
        for (String pair : initData.split("&")) {
            int index = pair.indexOf("=");
            if (index > 0) {
                String key = pair.substring(0, index);
                String value = URLDecoder.decode(pair.substring(index + 1), StandardCharsets.UTF_8);
                result.put(key, value);
            }
        }
        return result;
    }

    private  String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b & 0xff));
        }
        return hexString.toString();
    }


    public void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}
