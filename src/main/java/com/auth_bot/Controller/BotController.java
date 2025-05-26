package com.auth_bot.Controller;

import com.auth_bot.DTO.TelegramUserDTO;
import com.auth_bot.Model.TelegramUser;
import com.auth_bot.Service.TelegramBotService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RequestMapping("/bot")
@Controller
public class BotController {

    private final TelegramBotService telegramBotService;

    public BotController(TelegramBotService telegramBotService) {
        this.telegramBotService = telegramBotService;
    }

    @GetMapping("/webapp")
    public String showInitData( ) {
            return "web_app";
    }

    @GetMapping("/user")
    public String showUserPage(@RequestParam Long id, Model model){
        model.addAttribute("user", telegramBotService.findUserById(id));
        return "user_page";
    }

    @PostMapping("/auth")
    @ResponseBody
    public Map<String, Object> authenticate(@RequestBody TelegramUserDTO user) {
        System.out.println("DTO " + user);
        TelegramUser telegramUser = telegramBotService.save(user);
        if (telegramBotService.authenticate(user.getInitData())) {
            return Map.of("redirect", "/bot/user?id=" + telegramUser.getId());
        } else {
            return Map.of("redirect", "/error");
        }
    }
}
