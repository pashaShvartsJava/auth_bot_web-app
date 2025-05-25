package com.auth_bot.Repository;

import com.auth_bot.Config.TelegramBot;
import com.auth_bot.Model.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramBotRepository extends JpaRepository<TelegramUser, Long> {
}
