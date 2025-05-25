package com.auth_bot.Config;

import com.auth_bot.Model.TelegramUser;
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
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

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

    public boolean authenticateUser(String initData) throws NoSuchAlgorithmException, InvalidKeyException {
        String[] dataStrings = initData.split("&");
        for (int i = 0; i < dataStrings.length-1; i++) {
            if(dataStrings[i].startsWith("hash")) {
                hash = dataStrings[i].substring(5);
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <dataStrings.length ; i++) {
            if(i==dataStrings.length-1) {
                stringBuilder.append(dataStrings[i]);
            } else if (dataStrings[i].startsWith("hash")) {
                i++;
            }
            stringBuilder.append(dataStrings[i]).append("\n");
        }
        String init_data_without_hash = stringBuilder.toString();

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] secretKey = digest.digest(BOT_TOKEN.getBytes(StandardCharsets.UTF_8));

        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secretKey, "HmacSHA256"));

        String generatedHash = bytesToHex(mac.doFinal(init_data_without_hash.getBytes(StandardCharsets.UTF_8)));
        return generatedHash.equals(hash);
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
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
