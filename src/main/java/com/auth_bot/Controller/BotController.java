package com.auth_bot.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/bot")
@Controller
public class BotController {

    @GetMapping("/user/{id}")
    public String showUserPage(@PathVariable String id, Model model){

        return "user_page";
    }

}
