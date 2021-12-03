package com.yuhtin.devroom.core;

import com.yuhtin.devroom.TicketBot;
import lombok.Getter;

import java.util.Scanner;

public class Startup {

    public static void main(String[] args) {
        Bot bot = new TicketBot();
        try {
            bot.onEnable();
            bot.getLogger().info("[3/3] Registered Commands, Events, Timers and others");
        } catch (Exception exception) {
            exception.printStackTrace();
            bot.onDisable();

            return;
        }

        Runtime.getRuntime().addShutdownHook(new Thread(bot::onDisable));

        Scanner scanner = new Scanner(System.in);
        while (scanner.nextLine().equalsIgnoreCase("stop")) { // shit code to support minecraft hosts rofl
            new Thread(bot::onDisable).start();
        }
    }

}
