package com.auth_bot.Controller;

import com.auth_bot.Service.TelegramBotService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/bot")
@Controller
public class BotController {

    private final TelegramBotService telegramBotService;

    public BotController(TelegramBotService telegramBotService) {
        this.telegramBotService = telegramBotService;
    }

    @GetMapping("/user/{id}")
    public String showUserPage(@PathVariable Long id, Model model){
        model.addAttribute("telegram_user", telegramBotService.findUserById(id));
        return "user_page";
    }

}
