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

import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegrambots.bots.username}")
    private String BOT_USERNAME;

    @Value("${telegrambots.bots.token}")
    private String BOT_TOKEN;

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
                webAppButton.setWebApp(new WebAppInfo(""));

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
