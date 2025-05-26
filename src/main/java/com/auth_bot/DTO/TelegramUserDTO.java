package com.auth_bot.DTO;


public class TelegramUserDTO {


    private String initData;

    public String getInitData() {
        return initData;
    }

    public void setInitData(String initData) {
        this.initData = initData;
    }

    @Override
    public String toString() {
        return "TelegramUserDTO{" +
                ", initData='" + initData + '\'' +
                '}';
    }
}
